package com.example.healthappttt.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.healthappttt.R;

public class alarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        // 내위트 테이블에있는 목록 다 확인, connectFlag 가 0인 친구만 오래된 시간순으로 오름차순으로 띄울것
        // 알람 어뎁터에 수락,거절 버튼이있는데 수락을 누르면 해당 유저의 connectFlag = 1; 채팅프레그먼티에서 띄우기
        //알람 어뎁터 만들기.ㅣ
    }
}