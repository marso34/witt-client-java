package com.gwnu.witt.Routine;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.healthappttt.R;

import java.util.Calendar;
import java.util.Date;

public class RoutineActivity extends AppCompatActivity {
    ActivityRoutineBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoutineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        int code = intent.getIntExtra("code", 0);
        String name = intent.getStringExtra("name");

        binding.name.setText(name);

        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        replaceFragment(RoutineFragment.newInstance(dayOfWeek, code));
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
}