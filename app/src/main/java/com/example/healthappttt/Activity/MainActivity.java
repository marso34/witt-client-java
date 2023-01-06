package com.example.healthappttt.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.healthappttt.Activity.CalenderActivity;
import com.example.healthappttt.Activity.CreateRoutineActivity;
import com.example.healthappttt.Activity.ExercizeRecordActivity;
import com.example.healthappttt.R;

public class MainActivity extends AppCompatActivity {
    private Button StartBtn;
    private Button RoutineBtn;
    private Button btn_main;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ActionBar bar = getSupportActionBar();
//        bar.hide();

        StartBtn = (Button) findViewById(R.id.startBtn); // 시작 버튼

        StartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStartActivity(ExercizeRecordActivity.class);
            }
        });

        RoutineBtn = (Button) findViewById(R.id.AddEx);

        RoutineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                AddExercise();
            }
        });

        //화면전환 버튼
        btn_main = findViewById(R.id.calender);

        btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStartActivity(CalenderActivity.class);
            }
        });
    //화면전환 함수
    }

    private void AddExercise() {
        myStartActivity(CreateRoutineActivity.class);
    }

    private void myStartActivity(Class c) {// loginactivity페이지에서 mainactivity페이지로 넘기는 코드
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}


