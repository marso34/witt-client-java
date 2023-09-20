package com.gwnu.witt.Camera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import com.gwnu.witt.R;
import com.gwnu.witt.databinding.ActivityCameraBinding;

public class CameraActivity extends AppCompatActivity {
    ActivityCameraBinding binding;

    private int RESULT_PERMISSIONS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        requestPermissinCamera();
        replaceFragment(new CameraFragment());
    }

    private void replaceFragment (Fragment fragment){ //프래그먼트 설정
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if( fragment.isAdded() )
        {
            // Fragment 가 이미 추가되어 있으면 삭제한 후, 새로운 Fragment 를 생성한다.
            // 새로운 Fragment 를 생성하지 않으면 2번째 보여질 때에 Fragment 가 보여지지 않는 것 같습니다.
            fragmentTransaction.remove( fragment );
            fragment = new Fragment();
        }
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private boolean requestPermissinCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, RESULT_PERMISSIONS);
        }

//        int sdkVersion = Build.VERSION.SDK_INT;
//
//        if (sdkVersion >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, RESULT_PERMISSIONS);
//            } else {
//                replaceFragment(new CameraFragment());
//            }
//        } else {
////            setInit();
//            return true;
//        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_PERMISSIONS && resultCode == RESULT_OK) {

        }
    }

    //    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (RESULT_PERMISSIONS == requestCode) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                replaceFragment(new CameraFragment());
//            } else {
////                finish();
//            }
//            return;
//        }
//    }
}