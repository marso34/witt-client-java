package com.example.healthappttt.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Exercize;
import com.example.healthappttt.R;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.adapter.setExercizeAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

public class SetExercizeActivity extends AppCompatActivity {
    private ImageView OptionBtn; // 옵션
    private RecyclerView recyclerView;
    private setExercizeAdapter adapter;
    private Button StartBtn;  // 시작 버튼

    private Routine routine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_exerciae);

        OptionBtn = (ImageView) findViewById(R.id.optionBtn); // 옵션
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        StartBtn = (Button) findViewById(R.id.startBtn);  // 시작 버튼

        routine = createRoutine();
        setRecyclerView();

        OptionBtn.setOnClickListener(v -> _option(v)); // 옵션 버튼 눌렀을 때
        StartBtn.setOnClickListener((v -> _start(v))); // 운동 시작하기 눌렀을 때
    }

    private Routine createRoutine() {
        // 여기부터
        ArrayList<Exercize> exercizes = new ArrayList<>();
        exercizes.add(new Exercize("팔굽혀펴기", "가슴", 4, 0));
        exercizes.add(new Exercize("스쿼트", "하체", 4, 70));
        exercizes.add(new Exercize("턱걸이", "등", 4, 0));
        exercizes.add(new Exercize("딥스", "가슴", 4, 0));
        exercizes.add(new Exercize("달리기", "유산소", 30, 3));
        exercizes.add(new Exercize("대충 유산소", "유산소", 20, 3));
        exercizes.add(new Exercize("이것도 유산소", "유산소", 20, 3));
        exercizes.add(new Exercize("이것도 유산소2", "유산소", 20, 3));

        Routine routine = new Routine("기본 루틴", "전신", exercizes); // 여기까지 클래스 테스트

        return routine;
    } // DB 연동 전까지 사용할 루틴 만드는 메서드 -> 추후 수정 필요

    private void _start(View v) {
        Intent intent = new Intent(getApplicationContext(), ExercizeRecordActivity.class);
        intent.putExtra("routine", routine);
        startActivity(intent);
        finish(); // 운동 결과 페이지 보여주고 종료
    }

    private void _option(View v) {
    } // 이건 나중에


    private void setRecyclerView() {
        adapter = new setExercizeAdapter(routine);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}