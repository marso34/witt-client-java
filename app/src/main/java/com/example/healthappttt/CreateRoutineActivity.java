package com.example.healthappttt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class CreateRoutineActivity extends AppCompatActivity {
    Button exSelectBtn[] =  new Button[9],btnAddEx;

    boolean t[] = new boolean[]{false,false,false,false,false,false,false,false,false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);
        exSelectBtn[0] = (Button) findViewById(R.id.exercisePart0);
        exSelectBtn[1] = (Button) findViewById(R.id.exercisePart1);
        exSelectBtn[2] = (Button) findViewById(R.id.exercisePart2);
        exSelectBtn[3] = (Button) findViewById(R.id.exercisePart3);
        exSelectBtn[4] = (Button) findViewById(R.id.exercisePart4);
        exSelectBtn[5] = (Button) findViewById(R.id.exercisePart5);
        exSelectBtn[6] = (Button) findViewById(R.id.exercisePart6);
        exSelectBtn[7] = (Button) findViewById(R.id.exercisePart7);
        exSelectBtn[8] = (Button) findViewById(R.id.exercisePart8);
        btnAddEx = (Button) findViewById(R.id.AddEx);
        for(int i=0;i<9;++i){
            ExercizeareaSelect(exSelectBtn[i],i);
        }
    }
    private void myStartActivity(Class c) {// loginactivity페이지에서 mainactivity페이지로 넘기는 코드
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    public void ExercizeareaSelect(Button btn, int i){
        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                t[i] = !t[i];
                if(t[i] == true)
                    view.setBackgroundColor(Color.parseColor("#afe3ff"));
                else
                    view.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        });
    }
}