package com.example.healthappttt.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.CreateRoutinePagerAdapter;
import com.example.healthappttt.adapter.RoutinePagerAdapter;

public class CreateRoutineActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private CreateRoutinePagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);

        viewPager = findViewById(R.id.view_pager);

        pagerAdapter = new CreateRoutinePagerAdapter(this);
        pagerAdapter.createFragment(0);
        pagerAdapter.createFragment(1);
        pagerAdapter.createFragment(2);

        viewPager.setAdapter(pagerAdapter);
        viewPager.setUserInputEnabled(false);

//        Routine routine = new Routine();

//        TestBtn.setOnClickListener(view -> {
//            Intent intent = new Intent();
//            intent.putExtra("result", "가나다라마바사");
//            setResult(RESULT_OK, intent);
//            finish();
//        });
    }
}