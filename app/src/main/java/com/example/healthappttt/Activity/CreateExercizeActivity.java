package com.example.healthappttt.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.healthappttt.R;

public class CreateExercizeActivity extends AppCompatActivity {
    Button exSelectBtn[] =  new Button[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercize);
    }
}