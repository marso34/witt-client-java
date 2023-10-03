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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

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

    @Nullable private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    @Nullable private Camera camera;
    @Nullable private ImageAnalysis analysisUseCase;
    @Nullable private VisionImageProcessor imageProcessor;

    private GraphicOverlay graphicOverlay;
    private CameraSelector cameraSelector;
    private int lensFacing = CameraSelector.LENS_FACING_BACK;
    private boolean needUpdateGraphicOverlayImageSourceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {

        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); // 화면 가득차게, 스테이터스, 네비게이션 포함

        cameraSelector = new CameraSelector.Builder().requireLensFacing(lensFacing).build();
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.facingSwitch.setOnCheckedChangeListener(this);

        supportRequestWindowReature();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { // 카메라 전환
        if (cameraProviderFuture == null)
            return;

        int newLensFacing =
                lensFacing == CameraSelector.LENS_FACING_FRONT
                        ? CameraSelector.LENS_FACING_BACK
                        : CameraSelector.LENS_FACING_FRONT;

        CameraSelector newCameraSelector =
                new CameraSelector.Builder().requireLensFacing(newLensFacing).build();

        try {
            ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

            if (cameraProvider.hasCamera(newCameraSelector)) {
                lensFacing = newLensFacing;
                cameraSelector = newCameraSelector;
                cameraProvider.unbindAll();
                bindPreview();
//                bindPoseDetector();
            }
        } catch (ExecutionException | InterruptedException | CameraInfoUnavailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
            cameraProvider.unbindAll();

            bindPreview();
//            bindPoseDetector();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
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

    private void setUpCamera() {
        if (cameraProviderFuture == null)
            return;

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                cameraProvider.unbindAll();

                bindPreview();
//                bindPoseDetector();

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview() throws ExecutionException, InterruptedException {
        if (cameraProviderFuture == null)
            return;

        ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());

        camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview);
    }

    private void bindPoseDetector() throws ExecutionException, InterruptedException {
        if (cameraProviderFuture == null)
            return;

        ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

        if (analysisUseCase != null)
            cameraProvider.unbind(analysisUseCase);

        // 아래 값들은 나중에 SharedPreferences(PreferenceHelper)로 저장

        PoseDetectorOptionsBase poseDetectorOptions = getPoseDetectorOptionsForLivePreview();
        boolean shouldShowInFrameLikelihood = true; // or false
        boolean visualizeZ = true; // or false
        boolean rescaleZ = true; // or false
        boolean runClassification = true; // or false
        imageProcessor =
                new PoseDetectorProcessor(
                        this,
                        poseDetectorOptions,
                        shouldShowInFrameLikelihood,
                        visualizeZ,
                        rescaleZ,
                        runClassification,
                        /* isStreamMode = */ true);

        ImageAnalysis.Builder builder = new ImageAnalysis.Builder();
        Size targetResolution = null; //PreferenceUtils.getCameraXTargetResolution(this, lensFacing);

        if (targetResolution != null) {
            builder.setTargetResolution(targetResolution);
        }
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

    private static PoseDetectorOptionsBase getPoseDetectorOptionsForLivePreview() {
        int performanceMode = POSE_DETECTOR_PERFORMANCE_MODE_FAST;
        boolean preferGPU = true;

        if (performanceMode == POSE_DETECTOR_PERFORMANCE_MODE_FAST) {
            PoseDetectorOptions.Builder builder =
                    new PoseDetectorOptions.Builder().setDetectorMode(PoseDetectorOptions.STREAM_MODE);
            if (preferGPU) {
                builder.setPreferredHardwareConfigs(PoseDetectorOptions.CPU_GPU);
            }
            return builder.build();
        } else {
            AccuratePoseDetectorOptions.Builder builder =
                    new AccuratePoseDetectorOptions.Builder().setDetectorMode(AccuratePoseDetectorOptions.STREAM_MODE);
            if (preferGPU) {
                builder.setPreferredHardwareConfigs(AccuratePoseDetectorOptions.CPU_GPU);
            }
            return builder.build();
        }
    }

    private void supportRequestWindowReature() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            setUpCamera();
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
                setUpCamera();
                // 카메라 권한이 허용되었습니다.
                // 카메라를 사용하는 코드를 실행할 수 있습니다.
            } else {
                finish();
                // 사용자가 권한을 거부했거나 설정에서 권한을 변경할 수 있도록 안내합니다.
            }
        }
    }
}