package com.example.healthappttt.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.ExerciseName;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.Fragment.AddExerciseFragment;
import com.example.healthappttt.Fragment.ExerciseDetailFragment;
import com.example.healthappttt.Fragment.SetRoutineTimeFragment;
import com.example.healthappttt.R;

import java.util.ArrayList;

public class CreateRoutineActivity extends AppCompatActivity implements SetRoutineTimeFragment.OnFragmentInteractionListener, AddExerciseFragment.OnFragmentInteractionListener, ExerciseDetailFragment.OnFragmentInteractionListener {
    private int dayOfWeek;

    private int startTime, endTime;
    private ArrayList<Exercise> selectExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);

        Intent intent = getIntent();
        dayOfWeek = intent.getIntExtra("dayOfWeek", 0);
        Log.d("요일 테스트", Integer.toString(dayOfWeek));

        replaceFragment(new SetRoutineTimeFragment());
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