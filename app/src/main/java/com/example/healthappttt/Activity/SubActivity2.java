package com.example.healthappttt.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.R;

public class SubActivity2 extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub2);

        Button btnClick3 = findViewById(R.id.move3);

        btnClick3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(SubActivity2.this, SubActivity3.class);
                startActivity(intent);
                finish();
            }
        });
    }
}