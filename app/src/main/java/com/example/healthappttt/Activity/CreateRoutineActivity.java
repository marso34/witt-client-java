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
import com.example.healthappttt.Data.RoutineExerciseData;
import com.example.healthappttt.Data.RoutineResponse;
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

    private int dayOfWeek, startTime, endTime;
    private Routine routine;
    private ArrayList<Exercise> selectExercises;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);

        service = RetrofitClient.getClient().create(ServiceApi.class);
        routine = new Routine();
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

    private void SaveToDB() {
        ArrayList<RoutineExerciseData> list = new ArrayList<>();

        int index = 0;
        for (Exercise e : selectExercises) {
            list.add(new RoutineExerciseData(e.getTitle(), e.getCat(), e.getCount(), e.getVolume(), e.getNum(), index));
            index++;
        }

        RoutineData rData = new RoutineData(5, dayOfWeek,0, TimeToString(startTime), TimeToString(endTime), list);
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
                            PID = resultID;
                            routine = new Routine(resultID, dayOfWeek, 0, startTime, endTime);
                        } else {
                            selectExercises.get(i-1).setParentID(PID);
                            selectExercises.get(i-1).setID(resultID);
                            Cat |= selectExercises.get(i-1).getCat();
                        }
                        i++;
                    }
                    routine.setExerciseCategories(Cat);

                    SaveToDev();
                } else {
                    Toast.makeText(CreateRoutineActivity.this, "루틴 생성에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    Log.d("실패", "respone 실패");
                }
            }

            @Override
            public void onFailure(Call<List<Integer>> call, Throwable t) {
                Toast.makeText(CreateRoutineActivity.this, "서버 연결에 실패", Toast.LENGTH_SHORT).show();
                Log.d("실패", t.getMessage());
            }
        });
    }

    private void SaveToDev() {

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

        SaveToDB();
        // 받은 운동 정보 토대로 DB에 루틴, 운동 생성하고
        // 생성된 키 받아와서 로컬에 루틴, 운동 저장

        Intent intent = new Intent();
        intent.putExtra("routines", routine);
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