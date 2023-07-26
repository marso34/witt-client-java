package com.example.healthappttt.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.R;
import com.example.healthappttt.databinding.ActivityEditGymBinding;

public class EditGymActivity extends AppCompatActivity {

    private ActivityEditGymBinding binding;
    private PreferenceHelper UserTB;

    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gym);

        binding = ActivityEditGymBinding.inflate(getLayoutInflater());
        UserTB = new PreferenceHelper("UserTB",this);

        Intent intent = getIntent();
        name = intent.getStringExtra("MyName");

        //변경하기 클릭시
        binding.editGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();

        binding.MyName.setText(name + "님의");//이름
        binding.Gym.setText(UserTB.getGYMNM());//헬스장 이름
//        binding.locDetail.setText(); //헬스장 주소
    }



}