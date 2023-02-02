package com.example.healthappttt.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.healthappttt.R;

import java.io.File;

public class enlargementActivity extends AppCompatActivity {
    private ImageView ProImg;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enlargement);
        ProImg = findViewById(R.id.img);
        intent = getIntent();
        File f =  (File) getIntent().getSerializableExtra("post");
        Glide.with(this).load(f).into(ProImg);
    }
}