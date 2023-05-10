package com.example.healthappttt.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.ExerciseInputAdapter;
import com.example.healthappttt.interface_.ServiceApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditRoutineActivity extends AppCompatActivity {
    private TextView RunTime, StartTime, EndTime, DeleteBtn;
    private ImageView StartTimeUP, StartTimeDown;
    private ImageView EndTimeUP, EndTimeDown;

    private RecyclerView recyclerView;
    private ExerciseInputAdapter adapter;

    private CardView NextBtn;
    private TextView NextTxt;

    private ServiceApi service;
    private SQLiteUtil sqLiteUtil;

    private Routine routine;
    private ArrayList<Exercise> exercises;
    private int runTime, startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_routine);

        RunTime = (TextView) findViewById(R.id.runTime);
        StartTime = (TextView) findViewById(R.id.startTime);
        EndTime = (TextView) findViewById(R.id.endTime);
        DeleteBtn = (TextView) findViewById(R.id.delete);

        StartTimeUP = (ImageView) findViewById(R.id.startTimeUP);
        StartTimeDown = (ImageView) findViewById(R.id.startTimeDown);
        EndTimeUP = (ImageView) findViewById(R.id.endTimeUP);
        EndTimeDown = (ImageView) findViewById(R.id.endTimeDown);

        recyclerView = findViewById(R.id.recyclerView);

        NextBtn = findViewById(R.id.nextBtn);
        NextTxt = findViewById(R.id.nextTxt);

        service = RetrofitClient.getClient().create(ServiceApi.class);
        sqLiteUtil = SQLiteUtil.getInstance();
        sqLiteUtil.setInitView(this, "RT_TB");

        Intent intent = getIntent();
        routine = (Routine) intent.getSerializableExtra("routine");
        exercises = (ArrayList<Exercise>) intent.getSerializableExtra("exercises");

        init();
        setRecyclerView();

        DeleteBtn.setOnClickListener(v -> {
            AlertDialog.Builder alert_ex = new AlertDialog.Builder(this);
            alert_ex.setMessage("루틴을 삭제할까요?");
            alert_ex.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DeleteToDB(); // "예" 클릭시 삭제
                }
            });
            alert_ex.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 아니오 누를 시 아무것도 안 함
                }
            });
            alert_ex.setTitle("루틴 삭제");

            AlertDialog alert = alert_ex.create();
            alert.show();
        });

        StartTimeUP.setOnClickListener(v -> {
            if (startTime < endTime) {
                startTime += 5;
                runTime = endTime - startTime;
                StartTime.setText(TimeToString(startTime));
                RunTime.setText(RuntimeToString(runTime));
            }
        });

        StartTimeDown.setOnClickListener(v -> {
            if (startTime > 0) {
                startTime -= 5;
                runTime = endTime - startTime;
                StartTime.setText(TimeToString(startTime));
                RunTime.setText(RuntimeToString(runTime));
            }
        });

        EndTimeUP.setOnClickListener(v -> {
            if (endTime < 240) {
                endTime += 5;
                runTime = endTime - startTime;
                EndTime.setText(TimeToString(endTime));
                RunTime.setText(RuntimeToString(runTime));
            }
        });

        EndTimeDown.setOnClickListener(v -> {
            if (endTime > startTime) {
                endTime -= 5;
                runTime = endTime - startTime;
                EndTime.setText(TimeToString(endTime));
                RunTime.setText(RuntimeToString(runTime));
            }
        });

        NextBtn.setOnClickListener(v -> {

        });
    }

    private void init() {
        startTime = TimeToString(routine.getStartTime());
        endTime = TimeToString(routine.getEndTime());

        runTime = endTime - startTime;

        StartTime.setText(TimeToString(startTime));
        EndTime.setText(TimeToString(endTime));
        RunTime.setText(RuntimeToString(runTime));
    }

    private int TimeToString(String Time) {
        String[] TimeSplit = Time.split(":");

        int hour = Integer.parseInt(TimeSplit[0]);
        int min = Integer.parseInt(TimeSplit[1]);

        return (hour * 10) + (min / 6);
    }

    private String TimeToString(int Time) {
        String am_pm = "";

        if (Time >= 240) Time-= 240;

        if (Time < 120) {
            am_pm = "오전";
            if (Time < 10) Time += 120;
        } else {
            am_pm = "오후";
            if (Time >= 130) Time-= 120;
        }

        @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d", Time/10, Time % 10 * 6);

        return am_pm + " " + result;
    }

    private String RuntimeToString(int Time) {
        @SuppressLint("DefaultLocale") String result1 = String.format("%d분", Time % 10 * 6);
        @SuppressLint("DefaultLocale") String result2 = String.format("%d시간", Time/10);
        @SuppressLint("DefaultLocale") String result3 = String.format("%d시간 %d분", Time/10, Time % 10 * 6);

        if (Time < 10)
            return result1;

        if (Time % 10 == 0)
            return result2;

        return result3;
    }

    private void setRecyclerView() {
        adapter = new ExerciseInputAdapter(exercises, true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void DeleteToDB() {
        service.deleteRoutine(routine.getID()).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful() && response.body() == 200) {
                    Toast.makeText(EditRoutineActivity.this, "루틴 삭제 성공!!!", Toast.LENGTH_SHORT).show();
                    Log.d("성공", "루틴 삭제 성공");
                    DeleteToDev();
                    Terminate(true);
                } else {
                    Toast.makeText(EditRoutineActivity.this, "루틴 삭제 실패", Toast.LENGTH_SHORT).show();
                    Log.d("실패", "루틴 삭제 실패");
                    Terminate(false);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(EditRoutineActivity.this, "서버 연결에 실패", Toast.LENGTH_SHORT).show();
                Log.d("실패", t.getMessage());
                Terminate(false);
            }
        });
    }

    private void DeleteToDev() {
        sqLiteUtil.setInitView(this, "RT_TB");
        sqLiteUtil.delete(routine.getID());
    }

    private void Terminate(boolean isSuccess) {
        if (isSuccess) {
//            Intent intent = new Intent();
//            setResult(RESULT_OK, intent);
        }

        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert_ex = new AlertDialog.Builder(this);
        alert_ex.setMessage("루틴 수정을 취소할까요?");
        alert_ex.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish(); // "예" 클릭시 종료
            }
        });
        alert_ex.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 아니오 누를 시 아무것도 안 함
            }
        });
        alert_ex.setTitle("테스트");

        AlertDialog alert = alert_ex.create();
        alert.show();
    } // 뒤로가기 버튼 눌렀을 때 루틴 입력을 취소할지, Activity를 종료할 지 확인
}