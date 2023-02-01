package com.example.healthappttt.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.R;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.adapter.setExerciseAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SetExerciseActivity extends AppCompatActivity {
    private ImageView OptionBtn; // 옵션
    private RecyclerView recyclerView;
    private setExerciseAdapter adapter;
    private Button StartBtn;  // 시작 버튼

    private Routine routine;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;// 파이어베이스 유저관련 접속하기위한 변수
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_exercise);

        OptionBtn = (ImageView) findViewById(R.id.optionBtn); // 옵션
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        StartBtn = (Button) findViewById(R.id.startBtn);  // 시작 버튼

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        routine = new Routine("", "");
        createRoutine();

        OptionBtn.setOnClickListener(v -> _option(v)); // 옵션 버튼 눌렀을 때
        StartBtn.setOnClickListener((v -> _start(v))); // 운동 시작하기 눌렀을 때
    }

    private void createRoutine() {
        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        String str = "";

        switch (dayOfWeek) {
            case 0: str = "sun"; break;
            case 1: str = "mon"; break;
            case 2: str = "tue"; break;
            case 3: str = "wed"; break;
            case 4: str = "thu"; break;
            case 5: str = "fri"; break;
            case 6: str = "sat"; break;
        }

        String d = str;

        db.collection("routines").document(mAuth.getCurrentUser().getUid() +"_" + str).
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "Document exists!");
                                routine = new Routine(
                                        document.getData().get("title").toString(),
                                        document.getData().get("exerciseCategories").toString(),
                                        document.getData().get("startTime").toString(),
                                        document.getData().get("endTime").toString()
                                );

                                setExercises(dayOfWeek);

                            } else {
                                routine = new Routine("", "");
                                Log.d(TAG, "Document does not exist!");
                            }
                        } else {
                            Log.d(TAG, "Failed with: ", task.getException());
                        }
                    }
                });
    } // DB 연동 전까지 사용할 루틴 만드는 메서드 -> 추후 수정 필요

    private void setExercises(int dayOfWeek) {

        String str = "";

        switch (dayOfWeek) {
            case 0: str = "sun"; break;
            case 1: str = "mon"; break;
            case 2: str = "tue"; break;
            case 3: str = "wed"; break;
            case 4: str = "thu"; break;
            case 5: str = "fri"; break;
            case 6: str = "sat"; break;
        }

        String d = str;

        db.collection("routines").document(mAuth.getCurrentUser().getUid() +"_" + str).
                collection("exercises").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                routine.addExercise(new Exercise(
                                        document.getData().get("title").toString(),
                                        document.getData().get("state").toString(),
                                        Integer.parseInt(document.getData().get("count").toString()),
                                        Integer.parseInt(document.getData().get("volume").toString())
                                ));
                            }
                            setRecyclerView();

                        } else {

                        }
                    }
                });
    }



    private void _start(View v) {
        Intent intent = new Intent(getApplicationContext(), ExerciseRecordActivity.class);
        intent.putExtra("routine", routine);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish(); // 운동 결과 페이지 보여주고 종료
    }

    private void _option(View v) {
    } // 이건 나중에


    private void setRecyclerView() {
        adapter = new setExerciseAdapter(routine);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        if (adapter != null) {
            adapter.setOnExerciseClickListener(new setExerciseAdapter.OnExerciseClick() { // 어댑터 데이터를 전송 받기 위한 인터페이스 콜백
                @Override
                public void onExerciseClick(int postion) { // 운동 기록과 운동 메모를 전달 받아
                    adapter.notifyDataSetChanged();
                    adapter.removeItem(postion);
                }
            });
        }
    }
}