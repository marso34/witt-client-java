package com.example.healthappttt.Activity;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.Data.RoutineData;
import com.example.healthappttt.Data.RoutineResponse;
import com.example.healthappttt.Fragment.AddExerciseFragment;
import com.example.healthappttt.Fragment.ExerciseDetailFragment;
import com.example.healthappttt.Fragment.SetRoutineTimeFragment;
import com.example.healthappttt.R;
import com.example.healthappttt.interface_.ServiceApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRoutineActivity extends AppCompatActivity implements SetRoutineTimeFragment.OnFragmentInteractionListener, AddExerciseFragment.OnFragmentInteractionListener, ExerciseDetailFragment.OnFragmentInteractionListener {
    private ServiceApi service;

    private int dayOfWeek, startTime, endTime;
    private ArrayList<Exercise> selectExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);

        service = RetrofitClient.getClient().create(ServiceApi.class);
        selectExercises = new ArrayList<>();

        Intent intent = getIntent();
        dayOfWeek = intent.getIntExtra("dayOfWeek", 0);

        replaceFragment(new SetRoutineTimeFragment());
    }

    private String TimeToString(int Time) {
        String am_pm = "";

        if (Time >= 240) Time-= 240;

        @SuppressLint("DefaultLocale") String result = String.format("%2d:%02d:%02d", Time/10, Time % 10 * 6, 0);

        return am_pm + " " + result;
    }

    private void InsertRoutine() {
        RoutineData rData = new RoutineData(5, dayOfWeek,0, TimeToString(startTime), TimeToString(endTime));
        service.createRoutine(rData).enqueue(new Callback<RoutineResponse>() {
            @Override
            public void onResponse(Call<RoutineResponse> call, Response<RoutineResponse> response) {
                RoutineResponse result = response.body();
                Log.d("성공", "키 " + result.getID());
            }

            @Override
            public void onFailure(Call<RoutineResponse> call, Throwable t) {
                Log.d("실패", "실패.....");
            }
        });
    }

    @Override
    public void onRoutineSetTime(int startTime, int endTime) {
        this.startTime = startTime;
        this.endTime = endTime;

        int[] schedule = new int[3];
        schedule[0] = dayOfWeek;
        schedule[1] = startTime;
        schedule[2] = endTime;

        Bundle bundle = new Bundle();
        bundle.putIntArray("schedule", schedule);
        replaceFragment(new AddExerciseFragment(), bundle);
    }

    @Override
    public void onRoutineAddEx(ArrayList<Exercise> selectExercises) {
        this.selectExercises = selectExercises;

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
    public void onRoutineExDetail(ArrayList<Exercise> exercises) {
        this.selectExercises = exercises;

        InsertRoutine();

        // 받은 운동 정보 토대로 DB에 루틴, 운동 생성하고
        // 생성된 키 받아와서 로컬에 루틴, 운동 저장

        Intent intent = new Intent();
        intent.putExtra("routines", new Routine(0, dayOfWeek, 0, startTime, endTime));
        setResult(RESULT_OK, intent);
        finish();
    }

    private void replaceFragment (Fragment fragment){ //프래그먼트 설정
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

    private void replaceFragment (Fragment fragment, Bundle bundle){ //프래그먼트 설정
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
}