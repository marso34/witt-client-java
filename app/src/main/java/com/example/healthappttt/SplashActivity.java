package com.example.healthappttt;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences preferences = getSharedPreferences("GoSplash", MODE_PRIVATE);
        String userKey = preferences.getString("userKeyinLogin", "");

        if(userKey != null) {
            Log.d(TAG, "유저키: " + userKey);
            try {
                // 함수들 호출 및 실행
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // MainActivity 이동
                GoMain(userKey);
            }
        }

        else
            Log.d(TAG, "유저키 널값");
    }

    //함수 실행
    private void GoMain(String userKey){
        Log.d(TAG, "GoMain: "+userKey);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userKey",userKey);
        startActivity(intent);
        finish();
    }
}