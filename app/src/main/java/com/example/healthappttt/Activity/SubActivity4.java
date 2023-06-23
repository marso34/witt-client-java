package com.example.healthappttt.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.R;

public class SubActivity4 extends AppCompatActivity {
    TextView BenchValue, SquatValue, DeadValue;
    Integer Bench=0, Squat=0, Dead=0;
    Button Check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub4);
        BenchValue = findViewById(R.id.BebchValue);
        SquatValue = findViewById(R.id.SquatValue);
        DeadValue = findViewById(R.id.DeadValue);
        View parentLayout = findViewById(R.id.ClickSpace);
        Check = findViewById(R.id.Check);
        Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubActivity4.this, SignUpLastActivity.class);
                intent.putExtra("bench",Bench.toString());
                intent.putExtra("squat",Squat.toString());
                intent.putExtra("dead",Dead.toString());
                startActivity(intent);

            }
        }
        );


        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.SquatUpBtn &&Squat < 496){
                    Squat += 5;
                    SquatValue.setText(Squat.toString());
                }
                else if(v.getId() == R.id.SquatDownBtn&&Squat > 4){
                    Squat -= 5;
                    SquatValue.setText(Squat.toString());
                }
                else if(v.getId() == R.id.BenchUpBtn&&Bench < 296){
                    Bench += 5;
                    BenchValue.setText(Bench.toString());
                }
                else if(v.getId() == R.id.BenchDownBtn &&Bench >4){
                    Bench -= 5;
                    BenchValue.setText(Bench.toString());
                }
                else if(v.getId() == R.id.DeadUpBtn &&Dead < 496){
                    Dead += 5;
                    DeadValue.setText(Dead.toString());
                }
                else if(v.getId() == R.id.DeadUpBtn&&Dead > 4){
                    Dead -= 5;
                    DeadValue.setText(Dead.toString());
                }

            }
        });

    }


}