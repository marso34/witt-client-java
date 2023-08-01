package com.gwnu.witt.Routine;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gwnu.witt.Data.RetrofitClient;
import com.gwnu.witt.Data.Exercise.ExerciseData;
import com.gwnu.witt.Data.Exercise.RoutineData;
import com.gwnu.witt.Data.SQLiteUtil;
import com.gwnu.witt.R;
import com.gwnu.witt.interface_.ServiceApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditRoutineActivity extends AppCompatActivity {
    ActivityEditRoutineBinding binding;

    private static final String Background_1 = "#F2F5F9";
    private static final String Background_2 = "#D1D8E2";
    private static final String Background_3 = "#9AA5B8";
    private static final String Signature = "#05C78C";
    private static final String Orange = "#FC673F";
    private static final String Yellow = "#F2BB57";
    private static final String Blue = "#579EF2";
    private static final String Purple = "#8C5AD8";

    private ActivityResultLauncher<Intent> startActivityResult;
    private ExerciseInputAdapter adapter;

    private ServiceApi service;
    private SQLiteUtil sqLiteUtil;

    private RoutineData routine;
    private ArrayList<Integer> deletePk;

    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditRoutineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        routine = (RoutineData) intent.getSerializableExtra("routine");

        startActivityResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult data) {
                if (data.getResultCode() == Activity.RESULT_OK && data.getData() != null) {
                    Intent intent = data.getData();
                    Log.d("운동 갯수", routine.getExercises().size() + "");

                    RoutineData r = (RoutineData) intent.getSerializableExtra("routine");

                    for (int i = routine.getExercises().size(); i < r.getExercises().size(); i++)
                        routine.getExercises().add(r.getExercises().get(i));

                    adapter.notifyDataSetChanged();
                }
            }
        });

        service = RetrofitClient.getClient().create(ServiceApi.class);
        sqLiteUtil = SQLiteUtil.getInstance();
        sqLiteUtil.setInitView(this, "RT_TB");

        deletePk = new ArrayList<>();

        init();

        if (routine != null)
            setRecyclerView();

        setTime(routine.getTime());

        binding.backBtn.setOnClickListener(v -> {});

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

        binding.morning.setOnClickListener(v -> setTime(0));

        binding.afternoon.setOnClickListener(v -> setTime(1));

        binding.evening.setOnClickListener(v -> setTime(2));

        binding.dawn.setOnClickListener(v -> setTime(3));

        binding.addExercises.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, AddExerciseActivity.class);
            if (routine != null) {
                RoutineData r = new RoutineData(routine);
                intent1.putExtra("routine", r);
                startActivityResult.launch(intent1);
            }
        });

        binding.completeBtn.setOnClickListener(v -> {
            if (routine.getExercises().size() <= 0) {
                Toast.makeText(this, "운동이 없어요", Toast.LENGTH_SHORT).show();
            } else {
                int cnt = 0;

                for (ExerciseData e: routine.getExercises()) {
                    if (e.getSetOrTime() == 0) {
                        Toast.makeText(this, "운동 정보를 입력해주세요", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    cnt++;
                }

                if (cnt == routine.getExercises().size())
                    UpdateToDB();
            }
        });
    }

    private void init() {
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

    private void setTime(int t) {
        routine.setTime(t);
//  ------------------------------------------------------------------------------------------------
        binding.morning.setStrokeWidth(0);
        binding.afternoon.setStrokeWidth(0);
        binding.evening.setStrokeWidth(0);
        binding.dawn.setStrokeWidth(0);
//  ------------------------------------------------------------------------------------------------
        binding.morningIcon.setColorFilter(Color.parseColor(Background_3));
        binding.afternoonIcon.setColorFilter(Color.parseColor(Background_3));
        binding.eveningIcon.setColorFilter(Color.parseColor(Background_3));
        binding.dawnIcon.setColorFilter(Color.parseColor(Background_3));
//  ------------------------------------------------------------------------------------------------
        binding.morningTxt.setTextColor(Color.parseColor(Background_3));
        binding.afternoonTxt.setTextColor(Color.parseColor(Background_3));
        binding.eveningTxt.setTextColor(Color.parseColor(Background_3));
        binding.dawnTxt.setTextColor(Color.parseColor(Background_3));
//  ------------------------------------------------------------------------------------------------
        binding.morningDetail.setTextColor(Color.parseColor(Background_3));
        binding.afternoonDetail.setTextColor(Color.parseColor(Background_3));
        binding.eveningDetail.setTextColor(Color.parseColor(Background_3));
        binding.dawnDetail.setTextColor(Color.parseColor(Background_3));
//  ------------------------------------------------------------------------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            binding.morning.setOutlineSpotShadowColor(Color.parseColor(Background_1));
            binding.afternoon.setOutlineSpotShadowColor(Color.parseColor(Background_1));
            binding.evening.setOutlineSpotShadowColor(Color.parseColor(Background_1));
            binding.dawn.setOutlineSpotShadowColor(Color.parseColor(Background_1));
        }
//  ---------------------------전부 끄고------------------------------------------------------------
        switch (t) { // 필요한 것만 켜기
            case 0:
                binding.morning.setStrokeWidth(2);
                binding.morningIcon.setColorFilter(Color.parseColor(Orange));
                binding.morningTxt.setTextColor(Color.parseColor(Orange));
                binding.morningDetail.setTextColor(Color.parseColor(Orange));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    binding.morning.setOutlineSpotShadowColor(Color.parseColor(Orange));
                break;
            case 1:
                binding.afternoon.setStrokeWidth(2);
                binding.afternoonIcon.setColorFilter(Color.parseColor(Yellow));
                binding.afternoonTxt.setTextColor(Color.parseColor(Yellow));
                binding.afternoonDetail.setTextColor(Color.parseColor(Yellow));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    binding.afternoon.setOutlineSpotShadowColor(Color.parseColor(Yellow));
                break;
            case 2:
                binding.evening.setStrokeWidth(2);
                binding.eveningIcon.setColorFilter(Color.parseColor(Blue));
                binding.eveningTxt.setTextColor(Color.parseColor(Blue));
                binding.eveningDetail.setTextColor(Color.parseColor(Blue));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    binding.evening.setOutlineSpotShadowColor(Color.parseColor(Blue));
                break;
            case 3:
                binding.dawn.setStrokeWidth(2);
                binding.dawnIcon.setColorFilter(Color.parseColor(Purple));
                binding.dawnTxt.setTextColor(Color.parseColor(Purple));
                binding.dawnDetail.setTextColor(Color.parseColor(Purple));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    binding.dawn.setOutlineSpotShadowColor(Color.parseColor(Purple));
                break;
            default: break;
        }
    }

    private void setRecyclerView() {
        adapter = new ExerciseInputAdapter(this, routine.getExercises(), deletePk,true);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    private void UpdateToDB() {
        int cat = 0;

        for (ExerciseData e : routine.getExercises())
            cat |= e.getCat();

        routine.setCat(cat);

        service.updateRoutine(routine).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
//                    Toast.makeText(EditRoutineActivity.this, "수정 성공", Toast.LENGTH_SHORT).show();

                    int id = response.body(), cnt = 0;

                    for (ExerciseData e : routine.getExercises()) {
                        if (e.getID() == 0) {
                            Log.d("반환 pk", id + "");
                            e.setID(id++);
                            e.setParentID(routine.getID());
                        }
                    }

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

        for (ExerciseData e: routine.getExercises()) {
            sqLiteUtil.setInitView(this, "EX_TB");
            sqLiteUtil.UpdateOrInsert(e);
        }

        for (int pk: deletePk) {
            Log.d("삭제할 것들", pk + " 테스트");
            sqLiteUtil.setInitView(this, "EX_TB");
            sqLiteUtil.delete(pk);
        }
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
        alert_ex.setTitle("루틴 수정 취소");

        AlertDialog alert = alert_ex.create();
        alert.show();
    } // 뒤로가기 버튼 눌렀을 때 루틴 입력을 취소할지, Activity를 종료할 지 확인
}