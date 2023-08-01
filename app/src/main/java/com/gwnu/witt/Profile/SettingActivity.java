package com.gwnu.witt.Profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gwnu.witt.R;
import com.gwnu.witt.Sign.LoginActivity;
import com.gwnu.witt.interface_.DataReceiverService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

public class SettingActivity extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    Intent serviceIntent;
    Button cancel_setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        cancel_setting = findViewById(R.id.cancel_setting);
        cancel_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void onClickVive(View view) { //진동 설정
        // (진동,소리..)
        Intent intent = new Intent(SettingActivity.this, SetAlarmActivity.class);
        startActivity(intent);
    }

    public void onClickLogout(View view) { // 로그아웃
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
                        clearUserLoginInfo();
                        // Clear all activities and go to LoginActivity
                        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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

    private void clearUserLoginInfo() {
        // Clear user login information from SharedPreferences or any other storage mechanism
        SharedPreferences preferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
    public void onClickDrop(View view) { //탈퇴하기
        Intent intent = new Intent(SettingActivity.this,DropUserActivity.class);
        startActivity(intent);
    }

}