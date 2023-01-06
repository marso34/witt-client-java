package com.example.healthappttt.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;



public class CalenderActivity extends AppCompatActivity {

    public String fname=null;
    public String str=null;
    public CalendarView calendarView; //캘린더 뷰
    public Button cha_Btn,del_Btn,save_Btn; //수정,삭제,저장 버튼
    public TextView diaryTextView,textView2,textView3; // 날짜뜨는 뷰, 메모한 뷰,
    public EditText contextEditText; //메모 텍스트
    Button btn_sub, btn_backmain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        // 메인으로 가기 (뒤로가기)
        btn_backmain = findViewById(R.id.button_back_main);
        btn_backmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { finish(); }
            });


        calendarView=findViewById(R.id.calendarView); //캘린더뷰
        diaryTextView=findViewById(R.id.diaryTextView);
        save_Btn=findViewById(R.id.save_Btn);
        del_Btn=findViewById(R.id.del_Btn);
        cha_Btn=findViewById(R.id.cha_Btn);
        textView2=findViewById(R.id.textView2);
        textView3=findViewById(R.id.textView3);
        contextEditText=findViewById(R.id.contextEditText);
        //로그인 및 회원가입 엑티비티에서 이름을 받아옴
        Intent intent=getIntent();
        String name=intent.getStringExtra("userName");
        final String userID=intent.getStringExtra("userID");
        textView3.setText(name+" 님의 달력");



//화면전환 버튼 (운동추가하기)
        Button btn_sub;
        btn_sub = findViewById(R.id.button_sub);

        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myStartActivity(CalenderSubActivity.class);

            }
        });
//화면전환 버튼 (뒤록가기) 메인엑티비티로
        Button btn_back_main;


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //달력 날짜가 선택되면
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                diaryTextView.setVisibility(View.VISIBLE); // 해당 날짜가 뜨는 textview가 활성화
                save_Btn.setVisibility(View.VISIBLE); //저장 버튼이 활성화
                contextEditText.setVisibility(View.VISIBLE); // EditText가 활성화
                textView2.setVisibility(View.INVISIBLE); // 저장된 일기 textview가 비활성화
                cha_Btn.setVisibility(View.INVISIBLE); //수정버튼 비활성화
                del_Btn.setVisibility(View.INVISIBLE); //삭제버튼 비활성화
                diaryTextView.setText(String.format("%d / %d / %d",year,month+1,dayOfMonth)); //날짜를 보여주는 텍스트에 해당 날짜를 넣는다.
                contextEditText.setText(""); //EditText에 공백값 넣기
                checkDay(year,month,dayOfMonth,userID); //cheakDay 메소드 호출
            }
        });
        save_Btn.setOnClickListener(new View.OnClickListener() { //저장 버튼이 클릭되면
            @Override
            public void onClick(View view) {
                saveDiary(fname); //saveDiary 매소드 호출
                str=contextEditText.getText().toString(); //str변수에 edittext내용을 tostring형으로 저장
                textView2.setText(str); //textview에 str 출력
                save_Btn.setVisibility(View.INVISIBLE); //저장버튼 비활성화
                cha_Btn.setVisibility(View.VISIBLE); //수정버튼 활성화
                del_Btn.setVisibility(View.VISIBLE); //삭제버튼 활성화
                contextEditText.setVisibility(View.INVISIBLE); // 메모 텍스트 비활성화
                textView2.setVisibility(View.VISIBLE); //메모한 뷰 활성화

            }
        });
    }

    //-------------------------------checkDay 매소드(78~138)-----------------------------------------
    public void  checkDay(int cYear,int cMonth,int cDay,String userID){
        fname=""+userID+cYear+"-"+(cMonth+1)+""+"-"+cDay+".txt";//저장할 파일 이름설정 ex) 2019-01-20.txt
        FileInputStream fis=null;//FileStream fis 변수

        try{
            fis=openFileInput(fname); //선택된 날짜.txt에 읽을 변수 fis 선언

            byte[] fileData=new byte[fis.available()]; // filedata에 바이트 형식으로 저장
            fis.read(fileData); // filedata 읽음
            fis.close();

            str=new String(fileData); //str 변수에 filedata를 저장

            contextEditText.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.VISIBLE);
            textView2.setText(str); //텍스트뷰에 str 출력

            save_Btn.setVisibility(View.INVISIBLE);  //저장 버튼 비활성화 / 수정,삭제 버튼 활성화
            cha_Btn.setVisibility(View.VISIBLE);
            del_Btn.setVisibility(View.VISIBLE);

            cha_Btn.setOnClickListener(new View.OnClickListener() { //수정버튼 누르면
                @Override
                public void onClick(View view) {
                    contextEditText.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText(str); // 그전에 써놓은 텍스트 출력

                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    textView2.setText(contextEditText.getText()); //수정된 텍스트 출력
                }

            });
            del_Btn.setOnClickListener(new View.OnClickListener() { //삭제버튼 누르면
                @Override
                public void onClick(View view) {
                    textView2.setVisibility(View.INVISIBLE);
                    contextEditText.setText("");
                    contextEditText.setVisibility(View.VISIBLE);
                    save_Btn.setVisibility(View.VISIBLE);
                    cha_Btn.setVisibility(View.INVISIBLE);
                    del_Btn.setVisibility(View.INVISIBLE);
                    removeDiary(fname);
                }
            });
            if(textView2.getText()==null){
                textView2.setVisibility(View.INVISIBLE);
                diaryTextView.setVisibility(View.VISIBLE);
                save_Btn.setVisibility(View.VISIBLE);
                cha_Btn.setVisibility(View.INVISIBLE);
                del_Btn.setVisibility(View.INVISIBLE);
                contextEditText.setVisibility(View.VISIBLE);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }//---------------------------------------------------------------------------------------------
    @SuppressLint("WrongConstant")
    public void removeDiary(String readDay){
        FileOutputStream fos=null;

        try{
            fos=openFileOutput(readDay,MODE_NO_LOCALIZED_COLLATORS);
            String content="";
            fos.write((content).getBytes());
            fos.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @SuppressLint("WrongConstant")
    public void saveDiary(String readDay){
        FileOutputStream fos=null;

        try{
            fos=openFileOutput(readDay,MODE_NO_LOCALIZED_COLLATORS);
            String content=contextEditText.getText().toString();
            fos.write((content).getBytes());
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void myStartActivity(Class c) { //화면전환 함수
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}