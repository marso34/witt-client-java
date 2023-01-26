package com.example.healthappttt.Activity;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.healthappttt.Activity.CreateExercizeActivity;
import com.example.healthappttt.Data.Exercize;
import com.example.healthappttt.Data.ExercizeName;
import com.example.healthappttt.Data.User;
import com.example.healthappttt.R;

import java.util.ArrayList;

public class CreateRoutineActivity extends AppCompatActivity {
    Button exSelectBtn[] =  new Button[8],btnAddR;//운동부위선택 버튼들, 운동추가 액티비티로넘어가는 버튼
    private ArrayList<ExercizeName> ExercizeNameList;// 디비에서 불러올것
    private ArrayList<Exercize> ExercizeList;// 디비에 저장할것들
    boolean t[] = new boolean[]{false,false,false,false,false,false,false,false,false};// 버튼 색 바꿀 때 눌렸나 안눌렸나 체크.
    EditText RName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);
        RName = (EditText) findViewById(R.id.RName);
        exSelectBtn[0] = (Button) findViewById(R.id.ChestBtn);
        exSelectBtn[1] = (Button) findViewById(R.id.ShoulderBtn);
        exSelectBtn[2] = (Button) findViewById(R.id.EtcBtn);
        exSelectBtn[3] = (Button) findViewById(R.id.exercisePart3);
        exSelectBtn[4] = (Button) findViewById(R.id.exercisePart4);
        exSelectBtn[5] = (Button) findViewById(R.id.exercisePart6);
        exSelectBtn[6] = (Button) findViewById(R.id.exercisePart7);
        exSelectBtn[7] = (Button) findViewById(R.id.exercisePart8);
        btnAddR = (Button) findViewById(R.id.AddR);
        for(int i=0;i<8;++i){
            ExercizeareaSelect(exSelectBtn[i],i);
        }

        btnAddR.setOnClickListener(new Button.OnClickListener(){
          @Override
            public void onClick(View view){
              //파이어스토어로 저장시키는 코드 쓸것.

              SaveRoutine();
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
    public void SaveRoutine(){
        //디비로저장시키기.
    }
    public void LoadExercize(){
    //운동이름들 db에 저장그리고 불러올것. 이거 데이터 클래스 만들다가 끝났음.
    }
}