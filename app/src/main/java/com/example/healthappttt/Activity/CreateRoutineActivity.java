package com.example.healthappttt.Activity;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.example.healthappttt.Activity.CreateExerciseActivity;
import com.example.healthappttt.R;

public class CreateRoutineActivity extends AppCompatActivity {
    Button exSelectBtn[] =  new Button[9],btnAddEx;//운동부위선택 버튼들, 운동추가 액티비티로넘어가는 버튼
    boolean t[] = new boolean[]{false,false,false,false,false,false,false,false,false};// 버튼 색 바꿀 때 눌렸나 안눌렸나 체크.
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
        btnAddEx.setOnClickListener(new Button.OnClickListener(){
          @Override
            public void onClick(View view){
              myStartActivity(CreateExerciseActivity.class);
          }
        });
    }

    private void myStartActivity(Class c) {// loginactivity페이지에서 mainactivity페이지로 넘기는 코드
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//메뉴 사이드바 추가...
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    public void ExercizeareaSelect(Button btn, int i){// 운동 버튼이눌렸을 때 색 하늘색으로 바꾸고 취소되면 하얀색으로 바꾸기.
        btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                t[i] = !t[i];
                if(t[i] == true) {
                    view.setBackgroundResource(R.drawable.round_button_blue);
                    Log.d(TAG, "afe3ff ");
                }
                else {
                    view.setBackgroundResource(R.drawable.round_button_white);
                    Log.d(TAG, "fffff ");
                }
            }
        });
    }
}