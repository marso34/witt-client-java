package com.example.healthappttt.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.example.healthappttt.R;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.Data.Exercize;
import com.example.healthappttt.Data.Set;

public class ExercizeResultActivity extends AppCompatActivity {
    private TextView title;
    private TextView runtime;
    private TextView totalWeight;
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
        title = (TextView) findViewById(R.id.title);
        runtime = (TextView) findViewById(R.id.runTime);
        totalWeight = (TextView) findViewById(R.id.totalWeight);
        name = (TextView) findViewById(R.id.name);
        categories = (TextView) findViewById(R.id.exercizeCategories);

        text = (TextView) findViewById(R.id.text);
        ttttt = (TextView) findViewById(R.id.ttttt);


        title.setText(DateConversion(record.getStartTime(), 1));
        runtime.setText(DateConversion(record.getStartTime(), 0) + " ~ " + DateConversion(record.getEndTime(), 0));
        name.setText(record.getTitle());
        categories.setText(record.getExercizeCategories());
        text.setText("\n운동 메모 :\n" + record.getNotes() + "\n");

        int sum = 0;

        String result = "";
        
        for (Exercize i : record.getExercizes()) { // 데이터 전달 여부 확인
            result += i.getTitle() + "\n";

            int cnt = 0;
            for (Set s : i.getExercizeSet()) {
                result += s.getWeight() + "Kg X " + s.getCount() + "개" + "\n";
                sum += Integer.parseInt(s.getWeight());
                cnt++;
            }

            if (cnt != 0)
                result += "시작시간 = " + DateConversion(i.getStartTime(), 0) + ", 종료시간 = " + DateConversion(i.getEndTime(), 0) + "\n";
        }

        result += "\n runtime: " + time(record.getRunTime());

        totalWeight.setText(Integer.toString(sum) + "Kg");
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

    private String DateConversion(String t, int i) {
        Date date = new Date(Long.parseLong(t));
        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat("aa h:mm:ss");

        if (i != 0)
            dateFormat = new SimpleDateFormat("M월 d일 운동");

        String d = dateFormat.format(date);

        return d;
    }
}