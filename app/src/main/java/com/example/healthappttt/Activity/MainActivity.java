package com.example.healthappttt.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.healthappttt.Fragment.HomeFragment;
import com.example.healthappttt.Fragment.ProflieFragment;
import com.example.healthappttt.Fragment.ChattingFragment;
import com.example.healthappttt.Fragment.RoutineFragment;
import com.example.healthappttt.R;
import com.example.healthappttt.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    binding.viewName.setText("홈");
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.routine:
                    binding.viewName.setText("루틴");
                    replaceFragment(new RoutineFragment());
                    break;
                case R.id.chatting:
                    binding.viewName.setText("채팅");
                    replaceFragment(new ChattingFragment());
                    break;
                case R.id.profile:
                    binding.viewName.setText("프로필");
                    replaceFragment(new ProflieFragment());
                    break;
            }

            return true;
        });

        binding.fab.setOnClickListener (view -> {
            Intent intent = new Intent(getApplicationContext(), SetExerciseActivity.class);
            startActivity(intent);
        });
    }

    private void replaceFragment (Fragment fragment){ //프래그먼트 설정
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}