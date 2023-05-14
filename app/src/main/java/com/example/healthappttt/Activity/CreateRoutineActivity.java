package com.example.healthappttt.Activity;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.Data.RoutineData;
import com.example.healthappttt.Data.RoutineExerciseData;
import com.example.healthappttt.Data.RoutineResponse;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Fragment.AddExerciseFragment;
import com.example.healthappttt.Fragment.ExerciseDetailFragment;
import com.example.healthappttt.Fragment.SetRoutineTimeFragment;
import com.example.healthappttt.R;
import com.example.healthappttt.interface_.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRoutineActivity extends AppCompatActivity implements SetRoutineTimeFragment.OnFragmentInteractionListener, AddExerciseFragment.OnFragmentInteractionListener, ExerciseDetailFragment.OnFragmentInteractionListener {
    private ServiceApi service;
    private SQLiteUtil sqLiteUtil;

    private int dayOfWeek, startTime, endTime;
    private Routine routine;
    private ArrayList<Exercise> selectExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);

        service = RetrofitClient.getClient().create(ServiceApi.class);
        sqLiteUtil = SQLiteUtil.getInstance();

        routine = new Routine();
        selectExercises = new ArrayList<>();

        Intent intent = getIntent();
        dayOfWeek = intent.getIntExtra("dayOfWeek", 0);

        replaceFragment(new SetRoutineTimeFragment());
    }

    private String TimeToString(int Time) {
        if (Time >= 240) Time-= 240;

        @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d", Time/10, Time % 10 * 6, 0);

        return result;
    }

    private void SaveToDB() {
        ArrayList<RoutineExerciseData> list = new ArrayList<>();

        int index = 0, CAT = 0;
        for (Exercise e : selectExercises) {
            list.add(new RoutineExerciseData(e.getTitle(), e.getCat(), e.getCount(), e.getVolume(), e.getNum(), index));
            CAT |= e.getCat();
            index++;
        }

        int finalCAT = CAT;
        RoutineData rData = new RoutineData(5, dayOfWeek, finalCAT, TimeToString(startTime), TimeToString(endTime), list);
        service.createRoutine(rData).enqueue(new Callback<List<Integer>>() {
            @Override
            public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreateRoutineActivity.this, "루틴 생성 성공!!!", Toast.LENGTH_SHORT).show();
                    Log.d("성공", "루틴 생성 성공");

                    List<Integer> list = response.body();
                    int PID = 0, i = 0, Cat = 0;

                    for (int resultID : list) {
                        if (i == 0) {
                            PID = resultID; // 루틴ID
                            routine = new Routine(PID, TimeToString(startTime), TimeToString(endTime), finalCAT, dayOfWeek);
                        } else {
                            selectExercises.get(i-1).setParentID(PID);
                            selectExercises.get(i-1).setID(resultID);
                        }
                        i++;
                    }

                    SaveToDev();
                    Terminate(true); // 루틴 생성 액티비티 종료
                } else {
                    Toast.makeText(CreateRoutineActivity.this, "루틴 생성에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    Log.d("실패", "respone 실패");
                    Terminate(false); // 루틴 생성 액티비티 종료
                }
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {
                Toast.makeText(CreateRoutineActivity.this, "서버 연결에 실패", Toast.LENGTH_SHORT).show();
                Log.d("실패", t.getMessage());
                Terminate(false); // 루틴 생성 액티비티 종료
            }
        });
    }

    private void SaveToDev() {
        sqLiteUtil.setInitView(this, "RT_TB");
        sqLiteUtil.insert(routine);

        sqLiteUtil.setInitView(this, "EX_TB");
        for (Exercise e : selectExercises) {
            sqLiteUtil.insert(e);
        }
    }

    private void Terminate(boolean isSuccess) {
        if (isSuccess) {
            Intent intent = new Intent();
            intent.putExtra("routine", routine);
            intent.putExtra("check", 1); // 루틴 추가를 의미
            setResult(RESULT_OK, intent);
        }

        finish();
    }

    @Override
    public void onRoutineSetTime(int startTime, int endTime) {
        this.startTime = startTime;
        this.endTime = endTime;

        fragmentToAddEx();
    }  // SetRoutineTime에서 호출하는 메서드

    @Override
    public void onRoutineAddEx(ArrayList<Exercise> selectExercises) {
        this.selectExercises = selectExercises;

        fragmentToExDetail();
    } // AddExerciseFragment에서 호출하는 메서드

    @Override
    public void onRoutineExDetail(ArrayList<Exercise> exercises) {
        this.selectExercises = exercises;

        SaveToDB(); // 받은 운동 정보 토대로 DB에 루틴, 운동 생성
    } // ExerciseDetailFragment에서 호출하는 메서드

    private void replaceFragment (Fragment fragment) { //프래그먼트 설정
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if( fragment.isAdded() )
        {
            // Fragment 가 이미 추가되어 있으면 삭제한 후, 새로운 Fragment 를 생성한다.
            // 새로운 Fragment 를 생성하지 않으면 2번째 보여질 때에 Fragment 가 보여지지 않는 것 같습니다.
            fragmentTransaction.remove( fragment );
            fragment = new Fragment();
        }
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void replaceFragment (Fragment fragment, Bundle bundle) { //프래그먼트 설정
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if( fragment.isAdded() )
        {
            // Fragment 가 이미 추가되어 있으면 삭제한 후, 새로운 Fragment 를 생성한다.
            // 새로운 Fragment 를 생성하지 않으면 2번째 보여질 때에 Fragment 가 보여지지 않는 것 같습니다.
            fragmentTransaction.remove( fragment );
            fragment = new Fragment();
        }
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void fragmentToSetTime() {
        int[] schedule = new int[3];
        schedule[0] = dayOfWeek;
        schedule[1] = startTime;
        schedule[2] = endTime;

        Bundle bundle = new Bundle();
        bundle.putIntArray("schedule", schedule);
        replaceFragment(new SetRoutineTimeFragment(), bundle);
    }

    private void fragmentToAddEx() {
        int[] schedule = new int[3];
        schedule[0] = dayOfWeek;
        schedule[1] = startTime;
        schedule[2] = endTime;

        Bundle bundle = new Bundle();
        bundle.putSerializable("exercises", selectExercises);
        bundle.putIntArray("schedule", schedule);
        replaceFragment(new AddExerciseFragment(), bundle);
    }

    private void fragmentToExDetail() {
        int[] schedule = new int[3];
        schedule[0] = dayOfWeek;
        schedule[1] = startTime;
        schedule[2] = endTime;

        Bundle bundle = new Bundle();
        bundle.putSerializable("exercises", selectExercises);
        bundle.putIntArray("schedule", schedule);
        replaceFragment(new ExerciseDetailFragment(), bundle);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert_ex = new AlertDialog.Builder(this);
        alert_ex.setMessage("루틴 추가를 취소할까요?");
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
    } // 뒤로가기 버튼 눌렀을 때 루틴 입력을 취소할지, CreateRoutineActivity를 종료할 지 확인
}