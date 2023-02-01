package com.example.healthappttt.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.healthappttt.Data.Message;
import com.example.healthappttt.R;
import com.google.android.gms.tasks.OnSuccessListener;

public class ProfileActivity extends AppCompatActivity {
    private Button wittBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //wittBtn = findViewById(R.id.wittBtn);
        //메인에서 이 상세프로필의 주인의 유저 클래스를 받는다 thisUser;
        // ui에 값들 채워넣기코드, 채워널때 루틴정보는 준이가 만든 루틴 테이블접근해서 얻을것.
        wittBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //thisUser(눌린 유저)의 witt테이블에 doucument 제목으로 current유저(즉 바로나의 uid)저장
                //witttable에 필드로 현재 시간,int connectFlag 0 저장
            }
        });
    }
}