package com.example.healthappttt.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.R;



public class MyProfileActivity extends AppCompatActivity {

    ImageButton block_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        block_btn = findViewById(R.id.block_btn);
        ViewChangeBlock(); // 차단하기 클릭시 화면전환 매서드

    }

    public void ViewChangeBlock() {
        block_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfileActivity.this, BlockActivity.class);
                startActivity(intent);
            }
        });
    }


}

