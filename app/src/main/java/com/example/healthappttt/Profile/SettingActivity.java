package com.example.healthappttt.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.R;
import com.example.healthappttt.Sign.LoginActivity;
import com.example.healthappttt.interface_.DataReceiverService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

public class SettingActivity extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

    }

    public void onClickVive(View view) { //진동 설정
        //TODO 해야함 (진동,소리..)
        Intent intent = new Intent(SettingActivity.this, SetAlarmActivity.class);
        startActivity(intent);
    }

    public void onClickLogout(View view) { //로그아웃
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_sign_in_client_id))
                .requestEmail()
                .build();
        serviceIntent = new Intent(this, DataReceiverService.class);
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        DataReceiverService.setNormalExit(true);
                        stopService(serviceIntent);
                        // Update UI after sign out
                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("SettingActivity", "Google sign out failed", e);
                    }
                });
    }

    public void onClickDrop(View view) { //탈퇴하기
        Intent intent = new Intent(SettingActivity.this,DropUserActivity.class);
        startActivity(intent);
    }

}