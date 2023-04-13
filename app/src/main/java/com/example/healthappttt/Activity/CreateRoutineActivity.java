package com.example.healthappttt.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Button;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.Fragment.AddExerciseFragment;
import com.example.healthappttt.Fragment.ExerciseDetailFragment;
import com.example.healthappttt.Fragment.SetRoutineTimeFragment;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.CreateRoutinePagerAdapter;
import com.example.healthappttt.adapter.RoutinePagerAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateRoutineActivity extends AppCompatActivity implements SetRoutineTimeFragment.OnFragmentInteractionListener, AddExerciseFragment.OnFragmentInteractionListener, ExerciseDetailFragment.OnFragmentInteractionListener {
    private int dayOfWeek;

    private ViewPager2 viewPager;
    private CreateRoutinePagerAdapter pagerAdapter;

    private int startTime, endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // 뷰페이저2 말고 다른 방식 사용할 것!!!!!!!!!!!!!!!!
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);

        Intent intent = getIntent();

        dayOfWeek = (int) intent.getSerializableExtra("dayOfWeek");

        viewPager = findViewById(R.id.view_pager);

        pagerAdapter = new CreateRoutinePagerAdapter(this, dayOfWeek);
        pagerAdapter.createFragment(0);
        pagerAdapter.createFragment(1);
        pagerAdapter.createFragment(2);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setUserInputEnabled(false);
    }

    @Override
    public void onRoutineSetTime(int startTime, int endTime) {
        this.startTime = startTime;
        this.endTime = endTime;

        int position = viewPager.getCurrentItem();
        viewPager.setCurrentItem(position + 1);
    }

    @Override
    public void onRoutineAddEx(ArrayList<Exercise> exercises) {
        int position = viewPager.getCurrentItem();
        viewPager.setCurrentItem(position + 1);
    }

    @Override
    public void onRoutineExDetail(int startTime, int endTime) {
        Intent intent = new Intent();
        intent.putExtra("result", "가나다라마바사");
        setResult(RESULT_OK, intent);
        finish();
    }
}