package com.gwnu.witt.WorkOut;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.healthappttt.R;
import com.gwnu.witt.Data.Exercise.RoutineData;
import com.gwnu.witt.Data.Exercise.ExerciseData;

public class ExerciseResultActivity extends AppCompatActivity {
    private TextView title;
    private TextView runtime;
    private TextView totalWeight;
    private TextView name;
    private TextView categories;
    private TextView ttttt;

    private TextView StartTime, EndTime, RunTime, RestTime;
    private TextView Weight, Min;
    private Button EndBtn;

    private RecyclerView recyclerView;
//    private testAdapter adapter;

    private RoutineData record;
    private ArrayList<ExerciseData> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_result);

        Intent intent = getIntent();

        record = (RoutineData) intent.getSerializableExtra("record");

        StartTime = (TextView) findViewById(R.id.startTime);
        EndTime = (TextView) findViewById(R.id.endTime);
        RunTime = (TextView) findViewById(R.id.runTime);
        RestTime = (TextView) findViewById(R.id.restTime);
        Weight = (TextView) findViewById(R.id.weight);
        Min = (TextView) findViewById(R.id.min);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        setRecyclerView();

        EndBtn = (Button) findViewById(R.id.endBtn);

//        StartTime.setText(DateConversion(record.getStartTime()));
//        EndTime.setText(DateConversion(record.getEndTime()));
//        RunTime.setText(time(record.getRunTime()));
////        RestTime.setText();
//
//        exercises = record.getExercises();

        int sum = 0;
        int min = 0;

        for (ExerciseData e : exercises) {
            if (e.getStrCat().equals("유산소")) {
                min += e.getSetOrTime();
                continue;
            }

            sum += (e.getVolume() * e.getSetOrTime());
        }

        Weight.setText(sum + " kg");
        Min.setText(min + " 분");


        EndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        title = (TextView) findViewById(R.id.title);
//        runtime = (TextView) findViewById(R.id.runTime);
//        totalWeight = (TextView) findViewById(R.id.totalWeight);
//        name = (TextView) findViewById(R.id.name);
//        categories = (TextView) findViewById(R.id.exerciseCategories);
//        ttttt = (TextView) findViewById(R.id.ttttt);
//        title.setText(DateConversion(record.getStartTime(), 1));
//        runtime.setText(DateConversion(record.getStartTime(), 0) + " ~ " + DateConversion(record.getEndTime(), 0));
//        name.setText(record.getTitle());
//        categories.setText(record.getExerciszeCategories());
//        text.setText("\n운동 메모 : 없음\n" + "\n");
//
//        int sum = 0;
//
//        String result = "";
//
//        for (Exercise i : record.getExercises()) { // 데이터 전달 여부 확인
//            result += i.getTitle() + "\n";
//
//            int cnt = 0;
//
//            String weight, Set;
//
//            weight = i.getCount().substring(0, i.getCount().lastIndexOf(":"));
//            Set = i.getCount().substring(i.getCount().lastIndexOf(":")+1);
//
//            result += weight + "Kg " + Set + "세트" + "\n";
//
//            if (cnt != 0)
//                result += "시작시간 = " + DateConversion(i.getStartTime(), 0) + ", 종료시간 = " + DateConversion(i.getEndTime(), 0) + "\n";
//        }
//
//        result += "\n runtime: " + time(record.getRunTime());
//
//        totalWeight.setText(Integer.toString(sum) + "Kg");
//        ttttt.setText(result);
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

    private String DateConversion(String t) {
        Date date = new Date(Long.parseLong(t));
        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat("aa hh:mm");


        String d = dateFormat.format(date);

        return d;
    }
    private void setRecyclerView() {
//        adapter = new testAdapter(record);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
    }
}