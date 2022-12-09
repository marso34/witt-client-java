package com.example.healthappttt;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ExercizeResultActivity extends AppCompatActivity {
    private TextView name;
    private TextView categories;
    private TextView ttttt;

    private TextView text;
    private Routine record;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercize_result);

        Intent intent = getIntent();
        record = (Routine) intent.getSerializableExtra("record");
        name = (TextView) findViewById(R.id.name);
        categories = (TextView) findViewById(R.id.exercizeCategories);
        text = (TextView) findViewById(R.id.text);
        ttttt = (TextView) findViewById(R.id.ttttt);

        name.setText(record.getTitle());
        categories.setText(record.getExercizeCategories());
        text.setText("\n운동 메모 :\n" + record.getNotes() + "\n");



        String result = "";
        
        for (Exercize i : record.getExercizes()) { // 데이터 전달 여부 확인
            result += i.getTitle() + "\n";

            for (Set s : i.getExercizeSet())
                result += s.getWeight() + "Kg X " + s.getCount() + "개" + "\n";

            result += "시작시간 = " + i.getStartTime() + ", 종료시간 = " + i.getEndTime() + "\n";
        }

        result += "\n루틴 시작 시간 : "  + record.getStartTime() + " 루틴 종료 시간 : " + record.getEndTime() + " runtime: " + time(record.getRunTime());

        ttttt.setText(result);
    }

    private String time(String t) {
        int runTime = Integer.parseInt(t);
        int mSec = runTime % 1000 / 10;
        int sec = (runTime / 1000) % 60;
        int min = (runTime / 1000) / 60 % 60;
        int hour = (runTime / 1000) / (60 * 60);

        @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d", hour, min, sec);

        return result;
    }
}