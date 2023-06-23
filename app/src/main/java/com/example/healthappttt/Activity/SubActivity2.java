package com.example.healthappttt.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.R;

public class SubActivity2 extends AppCompatActivity
{
    EditText  Name;
    EditText  Hight;
    EditText  Weight;
    int sex;
    Button sexBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub2);

        Button btnClick3 = findViewById(R.id.move3);
        sexBtn = findViewById(R.id.sex);
        Name = findViewById(R.id.name);
        Hight = findViewById(R.id.hight);
        Weight = findViewById(R.id.weight);

        sexBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //성별 정하는 창 띄우기.
            }
        });
        btnClick3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(Name.getText() != null && Hight.getText() !=null && Weight.getText() != null) {
                    Intent intent = new Intent(SubActivity2.this, SubActivity3.class);
                    intent.putExtra("name",Name.getText());
                    intent.putExtra("hight",Hight.getText());
                    intent.putExtra("weight",Weight.getText());
                    startActivity(intent);

                }
                else {
                    //빈칸이있다고 토스트 메세지 띄우기
                }
            }
        });
    }
}