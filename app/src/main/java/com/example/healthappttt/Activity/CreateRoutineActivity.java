package com.example.healthappttt.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.healthappttt.R;

public class CreateRoutineActivity extends AppCompatActivity {

    private Button TestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);

        TestBtn = (Button) findViewById(R.id.testBtn);

        TestBtn.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("result", "가나다라마바사");
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}