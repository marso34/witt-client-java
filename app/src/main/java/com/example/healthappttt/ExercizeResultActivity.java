package com.example.healthappttt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ExercizeResultActivity extends AppCompatActivity {
    private TextView name;
    private TextView area;
    private TextView text;
    private Rutin record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercize_result);

        Intent intent = getIntent();
        record = (Rutin) intent.getSerializableExtra("record");
        name = (TextView) findViewById(R.id.name);
        area = (TextView) findViewById(R.id.area);
        text = (TextView) findViewById(R.id.text);

        name.setText(record.getTitle());
        area.setText(record.getExerciseArea());
        text.setText(record.getNotes());
    }
}