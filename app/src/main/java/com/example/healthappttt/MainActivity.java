package com.example.healthappttt;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.ActionBar;

public class MainActivity extends AppCompatActivity {

    private Button StartBtn;
    private Button Routine;

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
        Routine = (Button) findViewById(R.id.AddEx);
        Routine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                AddExercise();
            }
        });
    }

    private void AddExercise() {
        myStartActivity(CreateRoutineActivity.class);
    }

    private void myStartActivity(Class c) {// loginactivity페이지에서 mainactivity페이지로 넘기는 코드
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
}


