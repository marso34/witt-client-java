package com.example.healthappttt;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.Data.Exercise.ExerciseData;
import com.example.healthappttt.Data.Exercise.RecordData;
import com.example.healthappttt.Data.Exercise.RoutineData;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Data.pkData;
import com.example.healthappttt.interface_.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private ServiceApi service;
    private SQLiteUtil sqLiteUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        service = RetrofitClient.getClient().create(ServiceApi.class);
        sqLiteUtil = SQLiteUtil.getInstance();
    }

    private void SyncRoutine(int pk) {
        service.selectAllRoutine(new pkData(pk)).enqueue(new Callback<List<RoutineData>>() {
            @Override
            public void onResponse(Call<List<RoutineData>> call, Response<List<RoutineData>> response) {
                if (response.isSuccessful()) {
                    Log.d("성공", "루틴 불러오기 성공");

                    ArrayList<RoutineData> routines = (ArrayList<RoutineData>) response.body();

                    for (int i = 0; i < response.body().size(); i++)
                        routines.get(i).setExercises(response.body().get(i).getExercises());

                    saveRoutine(routines);
//                    로컬하고 동기화
                } else {
                    Toast.makeText(SplashActivity.this, "루틴 불러오기 실패!!!", Toast.LENGTH_SHORT).show();
                    Log.d("실패", "루틴 불러오기 실패");
                }
            }

            @Override
            public void onFailure(Call<List<RoutineData>> call, Throwable t) {
                Toast.makeText(SplashActivity.this, "서버 연결 실패..", Toast.LENGTH_SHORT).show();
                Log.d("서버 연결 실패", t.getMessage());
            }
        });
    }

    private void SyncRecord(int pk) {
        service.selectAllRecord(new pkData(pk)).enqueue(new Callback<List<RecordData>>() {
            @Override
            public void onResponse(Call<List<RecordData>> call, Response<List<RecordData>> response) {
                if (response.isSuccessful()) {

                    ArrayList<RecordData> records = (ArrayList<RecordData>) response.body();

                    for (int i = 0; i < response.body().size(); i++)
                        records.get(i).setExercises(response.body().get(i).getExercises());

                    saveRecods(records);
                    // 로컬하고 동기화
                } else {
                    Toast.makeText(SplashActivity.this, "기록 불러오기 실패!!!", Toast.LENGTH_SHORT).show();
                    Log.d("실패", "기록 불러오기 실패");
                }
            }

            @Override
            public void onFailure(Call<List<RecordData>> call, Throwable t) {
                Toast.makeText(SplashActivity.this, "서버 연결 실패..", Toast.LENGTH_SHORT).show();
                Log.d("서버 연결 실패", t.getMessage());
            }
        });
    }

    private void saveRoutine(ArrayList<RoutineData> routines) {
        ArrayList<String> notDeletePk = new ArrayList<>();

        for (RoutineData r : routines) {
            notDeletePk.add(r.getID()+"");
            sqLiteUtil.setInitView(this, "RT_TB");
            sqLiteUtil.UpdateOrInsert(r);
//          루틴 동기화
            for (ExerciseData e: r.getExercises()) {
                sqLiteUtil.setInitView(this, "EX_TB");
                sqLiteUtil.UpdateOrInsert(e);
            }
        }

        sqLiteUtil.setInitView(this, "RT_TB");
        sqLiteUtil.deleteMulti(notDeletePk);
    }

    private void saveRecods(ArrayList<RecordData> records) {
        ArrayList<String> notDeletePk = new ArrayList<>();

        for (RecordData r : records) {
            notDeletePk.add(r.getID()+"");
            sqLiteUtil.setInitView(this, "RECORD_TB");
            sqLiteUtil.UpdateOrInsert(r);
//          기록 동기화
            for (ExerciseData e: r.getExercises()) {
                sqLiteUtil.setInitView(this, "EX_TB");
                sqLiteUtil.UpdateOrInsert(e);
            }
        }

        sqLiteUtil.setInitView(this, "RECORD_TB");
        sqLiteUtil.deleteMulti(notDeletePk);
    }
}