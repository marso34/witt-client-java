package com.gwnu.witt.PoseCorrection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.util.Size;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.common.MlKitException;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.PoseDetectorOptionsBase;
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions;
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions;
import com.gwnu.witt.PoseCorrection.posedetector.PoseDetectorProcessor;
import com.gwnu.witt.databinding.ActivityCameraBinding;
import com.gwnu.witt.interface_.VisionImageProcessor;

import java.util.concurrent.ExecutionException;

public class CameraActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "CameraActivity";
    private static final String POSE_DETECTION_MODEL = "Pose Detection";
    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private static final int POSE_DETECTOR_PERFORMANCE_MODE_FAST = 1;

    ActivityCameraBinding binding;

    private PreviewView previewView;
    private GraphicOverlay graphicOverlay;

    @Nullable private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    @Nullable private ProcessCameraProvider cameraProvider;
    @Nullable private Camera camera;
    @Nullable private Preview previewUseCase;
    @Nullable private ImageAnalysis analysisUseCase;
    @Nullable private VisionImageProcessor imageProcessor;
    private boolean needUpdateGraphicOverlayImageSourceInfo;

    private int lensFacing = CameraSelector.LENS_FACING_BACK;
    private CameraSelector cameraSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {

        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); // 화면 가득차게, 스테이터스, 네비게이션 포함

        supportRequestWindowReature();

        cameraSelector = new CameraSelector.Builder().requireLensFacing(lensFacing).build();

//        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        previewView = binding.previewView;
        graphicOverlay = binding.graphicOverlay;


        binding.facingSwitch.setOnCheckedChangeListener(this);

        new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()))
                .get(CameraViewModel.class)
                .getProcessCameraProvider()
                .observe(
                        this,
                        provider -> {
                            cameraProvider = provider;
                            bindAllCameraUseCases();
                        }
                );
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { // 카메라 전환
        if (cameraProvider == null)
            return;

        int newLensFacing =
                lensFacing == CameraSelector.LENS_FACING_FRONT
                        ? CameraSelector.LENS_FACING_BACK
                        : CameraSelector.LENS_FACING_FRONT; // 카메라 렌즈 선택

        CameraSelector newCameraSelector = new CameraSelector.Builder().requireLensFacing(newLensFacing).build();

        try {
            if (cameraProvider.hasCamera(newCameraSelector)) {
                Log.d(TAG, "Set facing to " + newLensFacing);
                lensFacing = newLensFacing;
                cameraSelector = newCameraSelector;
                bindAllCameraUseCases();
                return;
            }
        } catch (CameraInfoUnavailableException e) {
            e.printStackTrace();
        }

        Toast.makeText(
                        getApplicationContext(),
                        "This device does not have lens with facing: " + newLensFacing,
                        Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindAllCameraUseCases();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (imageProcessor != null) {
            imageProcessor.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (imageProcessor != null) {
            imageProcessor.stop();
        }
    }


    private void bindAllCameraUseCases() {
        if (cameraProvider != null) {
            // As required by CameraX API, unbinds all use cases before trying to re-bind any of them.
            cameraProvider.unbindAll();
            bindPreviewUseCase();
            bindAnalysisUseCase();
        }
    }

    private void bindPreviewUseCase() {
        if (cameraProvider == null) {
            return;
        }
        if (previewUseCase != null) {
            cameraProvider.unbind(previewUseCase);
        }

        Preview.Builder builder = new Preview.Builder();
        Size targetResolution = null; //PreferenceUtils.getCameraXTargetResolution(this, lensFacing);

        if (targetResolution != null) {
            builder.setTargetResolution(targetResolution);
        }
        previewUseCase = builder.build();
        previewUseCase.setSurfaceProvider(previewView.getSurfaceProvider());
        camera = cameraProvider.bindToLifecycle(/* lifecycleOwner= */ this, cameraSelector, previewUseCase);
    }

    private void bindAnalysisUseCase() {
        if (cameraProvider == null) {
            return;
        }
        if (analysisUseCase != null) {
            cameraProvider.unbind(analysisUseCase);
        }
        if (imageProcessor != null) {
            imageProcessor.stop();
        }

        PoseDetectorOptionsBase poseDetectorOptions =
                new AccuratePoseDetectorOptions.Builder()
                        .setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE)
                        .setPreferredHardwareConfigs(AccuratePoseDetectorOptions.CPU_GPU)
                        .build();
        boolean shouldShowInFrameLikelihood = true;
        boolean visualizeZ = true;
        boolean rescaleZ = true;
        boolean runClassification = true;
        imageProcessor =
                new PoseDetectorProcessor(
                        this,
                        poseDetectorOptions,
                        shouldShowInFrameLikelihood,
                        visualizeZ,
                        rescaleZ,
                        runClassification,
                        true);


        ImageAnalysis.Builder builder = new ImageAnalysis.Builder();
        Size targetResolution = null;
        if (targetResolution != null)
            builder.setTargetResolution(targetResolution);

        analysisUseCase = builder.build();

        needUpdateGraphicOverlayImageSourceInfo = true;
        analysisUseCase.setAnalyzer(
                // imageProcessor.processImageProxy will use another thread to run the detection underneath,
                // thus we can just runs the analyzer itself on main thread.
                ContextCompat.getMainExecutor(this),
                imageProxy -> {
                    if (needUpdateGraphicOverlayImageSourceInfo) {
                        boolean isImageFlipped = lensFacing == CameraSelector.LENS_FACING_FRONT;
                        int rotationDegrees = imageProxy.getImageInfo().getRotationDegrees();
                        if (rotationDegrees == 0 || rotationDegrees == 180) {
                            graphicOverlay.setImageSourceInfo(
                                    imageProxy.getWidth(), imageProxy.getHeight(), isImageFlipped);
                        } else {
                            graphicOverlay.setImageSourceInfo(
                                    imageProxy.getHeight(), imageProxy.getWidth(), isImageFlipped);
                        }
                        needUpdateGraphicOverlayImageSourceInfo = false;
                    }
                    try {
                        imageProcessor.processImageProxy(imageProxy, graphicOverlay);
                    } catch (MlKitException e) {
                        Log.e(TAG, "Failed to process image. Error: " + e.getLocalizedMessage());
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                });

        cameraProvider.bindToLifecycle(/* lifecycleOwner= */ this, cameraSelector, analysisUseCase);
    }

    private void supportRequestWindowReature() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//            setUpCamera();
            // 카메라 권한이 이미 허용되었습니다.
            // 카메라를 사용하는 코드를 실행할 수 있습니다.
        } else {
            // 카메라 권한을 요청합니다.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        }

//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 아래는 카메라 앱 실행 방식
//
//        if (cameraIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
//            startActivityResult.launch(cameraIntent);
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                setUpCamera();
                // 카메라 권한이 허용되었습니다.
                // 카메라를 사용하는 코드를 실행할 수 있습니다.
            } else {
                finish();
                // 사용자가 권한을 거부했거나 설정에서 권한을 변경할 수 있도록 안내합니다.
            }
        }
    }
}