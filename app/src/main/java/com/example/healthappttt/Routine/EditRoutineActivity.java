package com.example.healthappttt.Routine;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.Exercise.ExerciseData;
import com.example.healthappttt.Data.Exercise.RoutineData;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Data.pkData;
import com.example.healthappttt.R;
import com.example.healthappttt.databinding.ActivityEditRoutineBinding;
import com.example.healthappttt.interface_.ServiceApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditRoutineActivity extends AppCompatActivity {
    ActivityEditRoutineBinding binding;
    private ActivityResultLauncher<Intent> startActivityResult;
    private ExerciseInputAdapter adapter;

    private ServiceApi service;
    private SQLiteUtil sqLiteUtil;

    private RoutineData routine;
    private ArrayList<ExerciseData> exercises;
    private int runTime, startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditRoutineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        startActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult data) {
                if (data.getResultCode() == Activity.RESULT_OK && data.getData() != null) {
                    Intent intent = data.getData();
                    RoutineData r = (RoutineData) intent.getSerializableExtra("routine");
                    routine.setExercises(r.getExercises());
                    exercises = routine.getExercises();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        service = RetrofitClient.getClient().create(ServiceApi.class);
        sqLiteUtil = SQLiteUtil.getInstance();
        sqLiteUtil.setInitView(this, "RT_TB");

        Intent intent = getIntent();
        routine = (RoutineData) intent.getSerializableExtra("routine");

        init();

        Log.d(this.toString(), "테스트");

        if (routine != null) {
            exercises = routine.getExercises();
            setRecyclerView();
        }

        binding.dayOfWeek.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                switch (checkedID) {
                    case R.id.sun: routine.setDayOfWeek(0); break;
                    case R.id.mon: routine.setDayOfWeek(1); break;
                    case R.id.tue: routine.setDayOfWeek(2); break;
                    case R.id.wed: routine.setDayOfWeek(3); break;
                    case R.id.thu: routine.setDayOfWeek(4); break;
                    case R.id.fri: routine.setDayOfWeek(5); break;
                    case R.id.sat: routine.setDayOfWeek(6); break;
                }
            }
        });

        binding.startTimeUP.setOnClickListener(v -> {
            if (startTime < endTime) {
                startTime += 5;
                runTime = endTime - startTime;
                binding.startTime.setText(TimeToString(startTime));
                binding.runTime.setText(RuntimeToString(runTime));
            }
        });

        binding.startTimeDown.setOnClickListener(v -> {
            if (startTime > 0) {
                startTime -= 5;
                runTime = endTime - startTime;
                binding.startTime.setText(TimeToString(startTime));
                binding.runTime.setText(RuntimeToString(runTime));
            }
        });

        binding.endTimeUP.setOnClickListener(v -> {
            if (endTime < 240) {
                endTime += 5;
                runTime = endTime - startTime;
                binding.endTime.setText(TimeToString(endTime));
                binding.runTime.setText(RuntimeToString(runTime));
            }
        });

        binding.endTimeDown.setOnClickListener(v -> {
            if (endTime > startTime) {
                endTime -= 5;
                runTime = endTime - startTime;
                binding.endTime.setText(TimeToString(endTime));
                binding.runTime.setText(RuntimeToString(runTime));
            }
        });

        binding.addRoutine.setOnClickListener(v -> {

        });

        binding.completeBtn.setOnClickListener(v -> {
            if (runTime <= 0) {
                Toast.makeText(this, "시간을 다시 설정해주세요", Toast.LENGTH_SHORT).show();
            } else if (exercises.size() <= 0) {
                Toast.makeText(this, "운동이 없어요", Toast.LENGTH_SHORT).show();
            } else {
                routine.setStartTime(TimeToStringD(startTime));
                routine.setEndTime(TimeToStringD(endTime));
                UpdateToDB();
            }
        });
    }

    private void init() {
        if (routine != null) {
            startTime = TimeToString(routine.getStartTime());
            endTime = TimeToString(routine.getEndTime());
            runTime = endTime - startTime;
        }

        binding.startTime.setText(TimeToString(startTime));
        binding.endTime.setText(TimeToString(endTime));
        binding.runTime.setText(RuntimeToString(runTime));

        switch (routine.getDayOfWeek()) {
            case 0: binding.sun.setChecked(true); break;
            case 1: binding.mon.setChecked(true); break;
            case 2: binding.tue.setChecked(true); break;
            case 3: binding.wed.setChecked(true); break;
            case 4: binding.thu.setChecked(true); break;
            case 5: binding.fri.setChecked(true); break;
            case 6: binding.sat.setChecked(true); break;
        }
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

    private String TimeToStringD(int Time) {
        if (Time >= 240) Time-= 240;
        @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d", Time/10, Time % 10 * 6, 0);

        return result;
    }

    private void setRecyclerView() {
        adapter = new ExerciseInputAdapter(exercises, true);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void UpdateToDB() {
        routine.setExercises(exercises);
        service.updateRoutine(routine).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditRoutineActivity.this, "수정 성공", Toast.LENGTH_SHORT).show();
                    UpdateToDev();
                    Terminate(true, 0); // 루틴 수정을 의미
                } else {
                    Toast.makeText(EditRoutineActivity.this, "서버 연결에 실패", Toast.LENGTH_SHORT).show();
                    Log.d("실패", "respone 실패");
                    Terminate(false); // 루틴 수정 액티비티 종료
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(EditRoutineActivity.this, "서버 연결에 실패", Toast.LENGTH_SHORT).show();
                Log.d("실패", t.getMessage());
                Terminate(false); // 루틴 수정 액티비티 종료
            }
        });
    }

    private void UpdateToDev() {
        sqLiteUtil.setInitView(this, "RT_TB");
        sqLiteUtil.Update(routine);
        sqLiteUtil.setInitView(this, "EX_TB");

        for (ExerciseData e: routine.getExercises())
            sqLiteUtil.Update(e);
    }

    private void Terminate(boolean isSuccess) {
        if (isSuccess) {
            Intent intent = new Intent();
            intent.putExtra("routine", routine);
            intent.putExtra("check", 0);
            setResult(RESULT_OK, intent);
        }

        finish();
    }

    private void Terminate(boolean isSuccess, int check) {
        if (isSuccess) {
            Intent intent = new Intent();
            intent.putExtra("routine", routine);
            intent.putExtra("check", check); // 2면 루틴 삭제
            setResult(RESULT_OK, intent);
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