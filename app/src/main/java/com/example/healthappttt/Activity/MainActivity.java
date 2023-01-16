package com.example.healthappttt.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.healthappttt.Fragment.HomeFragment;
import com.example.healthappttt.Fragment.ProflieFragment;
import com.example.healthappttt.Fragment.SettingFragment;
import com.example.healthappttt.R;
import com.example.healthappttt.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private Button StartBtn;
    private Button RoutineBtn;
    private Button btn_main;
    private Button loginBtn;


    ActivityMainBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
//        ActionBar bar = getSupportActionBar();
//        bar.hide();

//        StartBtn = (Button) findViewById(R.id.startBtn); // 운동 시작 버튼
//        RoutineBtn = (Button) findViewById(R.id.AddEx);
//        btn_main = findViewById(R.id.calender);
//        loginBtn = findViewById(R.id.login);


        binding.bottomnav.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProflieFragment());
                    break;
                case R.id.setting:
                    replaceFragment(new SettingFragment());
                    break;
            }

            return true;
        });


//        StartBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myStartActivity(ExercizeRecordActivity.class);
//            }
//        });
//
//        RoutineBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                AddExercise();
//            }
//        });
//
//        //화면전환 버튼
//        btn_main.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myStartActivity(CalenderActivity.class);
//            }
//        });
//
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myStartActivity(LoginActivity.class);
//            }
//        });
//    //화면전환 함수
//    }
//
//    private void AddExercise() {
//        myStartActivity(CreateRoutineActivity.class);
//    }
//w
//    private void myStartActivity(Class c) {// loginactivity페이지에서 mainactivity페이지로 넘기는 코드
//        Intent intent = new Intent(this, c);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//    }


    }
    private void replaceFragment (Fragment fragment){ //프래그먼트 설정
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
