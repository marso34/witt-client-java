package com.gwnu.witt.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gwnu.witt.Data.PreferenceHelper;
import com.gwnu.witt.Data.RetrofitClient;
import com.gwnu.witt.Data.SQLiteUtil;
import com.gwnu.witt.R;
import com.gwnu.witt.Sign.LoginActivity;
import com.gwnu.witt.interface_.ServiceApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DropUserActivity extends AppCompatActivity {
    private ServiceApi apiService;
    private SQLiteUtil sqLiteUtil;
    private PreferenceHelper UserTB;
    private CheckedTextView[] checkedTextViews; //체크박스들 모음
    private int selection, mypk;
    private String myname;
    private Button GO_Drop;
    private ImageButton cancel_drop;
    private TextView reportname;
    private EditText txt;
    private Map<String, Object> DropMap; //서버로 보내는 데이터
    private boolean isButtonClickable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_user);
        apiService = RetrofitClient.getClient().create(ServiceApi.class);
        UserTB = new PreferenceHelper("UserTB", DropUserActivity.this);
        sqLiteUtil = SQLiteUtil.getInstance();

        myname = UserTB.getUser_NM();
        mypk = UserTB.getPK();

        setview();// 체크 뷰 및 다른 뷰 정의

        selection = 0;
        DropMap = new HashMap<>();
        DropMap.put("mypk",mypk);
//        DropMap.put("mypk",36); //test
        setCheckedTextViewListeners();
        Dropclick();//탈퇴하기 누르는 경우
    }


    public void setview() {
        cancel_drop = findViewById(R.id.cancel_drop);
        backToSetting();//뒤로가기
        GO_Drop = findViewById(R.id.GO_Drop);
        txt = findViewById(R.id.Drop_txt);
        reportname = findViewById(R.id.dropname);
        reportname.setText(myname + "님이");

        checkedTextViews = new CheckedTextView[] {
                findViewById(R.id.Drop_ck0),
                findViewById(R.id.Drop_ck1),
                findViewById(R.id.Drop_ck2),
                findViewById(R.id.Drop_ck3),
                findViewById(R.id.Drop_ck4)
        };
    }

    public void setCheckedTextViewListeners() {
        View.OnClickListener checkedListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckedTextView checkedTextView = (CheckedTextView) v;
                checkedTextView.setChecked(!checkedTextView.isChecked());
                if (checkedTextView.isChecked()) {
                    checkedTextView.setTextColor(getResources().getColor(R.color.Signature));// 텍스트색
                    checkedTextView.setCheckMarkDrawable(getDrawable(R.drawable.ic_baseline_check_24));//체크표시 모양
                    checkedTextView.setBackground(getDrawable(R.drawable.border_background));//배경색


                } else {
                    checkedTextView.setCheckMarkDrawable(null);// 체크 표시 제거
                    checkedTextView.setBackground(getDrawable(R.drawable.rectangle));
                    checkedTextView.setTextAppearance(DropUserActivity.this, R.style.report);


                }
            }
        };

        for (CheckedTextView checkedTextView : checkedTextViews) { //체크리스트 하나하나 onclick리스너 등록
            checkedTextView.setOnClickListener(checkedListener);
        }
    }



    public void Dropclick() {
        GO_Drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAnyChecked = false;
                for (CheckedTextView checkedTextView : checkedTextViews) {
                    if (checkedTextView.isChecked()) {
                        isAnyChecked = true;
                        break; // 체크된 항목이 하나라도 있으면 반복문 중단
                    }
                }
                String inputText = txt.getText().toString().trim();
                if (!isAnyChecked) {//하나도 체크되지 않은 경우
                    if(inputText.isEmpty()){//체크도 안하고 텍스트도 안적었을 경우
                        disableButtonForOneSecond();
                        Toast.makeText(DropUserActivity.this, "탈퇴 사유를 하나 이상 체크해주세요", Toast.LENGTH_SHORT).show();
                    }else { //체크는 안하고 텍스트만 적은 경우 -> 이메일 전송 후 창 닫기
                        Log.d("ReportActivity : ", "체크표시를 안하고 텍스트만 적었을때");
                        DROP(DropMap, true);
                        // 서버에서 유저 PK 삭제 + 로컬 db 삭제
                    }
                } else {// 1. 선택한 체크박스 서버 FEEDBACK_TB에 저장 (PK연결은 하자만 이 투플은 삭제하지 않도록)
                        // 2. 서버에서 유저 PK 삭제 + (로컬db도 삭제)
                        // 텍스트가 널인지 아닌지 구별 못하는거 다시 검토
                    setCheckBit(checkedTextViews);
                    if(inputText.isEmpty()){
                        //서버db에 피드백 저장 and email 전송 X
                        Log.d("ReportActivity : ","체크표시하고 텍스트 안적었을때" );
                        DROP(DropMap, false);
                        //CommonDROP();

                    }else {
                        Log.d("텍스트: ", String.valueOf(txt.getText()));
                        //서버db에 피드백 저장 and email 전송 O
                        Log.d("ReportActivity : ","둘다 적었을때");
                        DROP(DropMap, true);
                        //sendEmail();
                        //CommonDROP();
                    }
                }

            }
        });
    }

    public void DROP(Map<String,Object> DropMap,boolean i) {
        //서버 디비에서 해당 키 삭제 + (CONT가 있을 경우에만 저장)
       Call<String> call =  apiService.deleteUser(DropMap);
       call.enqueue(new Callback<String>() {
           @Override
           public void onResponse(Call<String> call, Response<String> response) {
               if (response.isSuccessful()) {
                   String d = response.body();
                   Log.d("DropUserActivity ","탈퇴성공 PK: "+d);
                   //email 전송
                   if (i) {
                       Log.d("CommonDROP...sendEmail:", "이메일 실행O");
                       CommonDROP();
                       sendEmail();
                   }else {
                       Log.d("CommonDROP: ", "이메일 실행 X");
                       CommonDROP();
                   }

               } else {
                   // 요청이 실패했을 때의 처리 로직
                   Log.d("DropUserActivity ","탈퇴실패");
               }
           }

           @Override
           public void onFailure(Call<String> call, Throwable t) {
               Log.d("DropUserActivity ", "서버 응답 실패.");
           }
       });
    }


    public void CommonDROP() {

        //로컬 shared 삭제
        UserTB.deleteUserTB();
        sqLiteUtil.DropUser(DropUserActivity.this);

        Toast.makeText(DropUserActivity.this, "탈퇴완료", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DropUserActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); //쌓인 엑티비티 스택 모두 삭제
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
//        startActivityForResult(intent,10);

    }

    public void sendEmail() {
//        Intent email = new Intent(Intent.ACTION_SEND);
//        email.setType("plane/text");
//        String[] address = {"hwstar1204@gmail.com"};
//        email.putExtra(Intent.EXTRA_EMAIL,address);
//        email.putExtra(Intent.EXTRA_SUBJECT,"Witt 탈퇴 내역");
//        email.putExtra(Intent.EXTRA_TEXT,txt.getText());
//        startActivity(email);

        Map<String,String> data = new HashMap<>();
        data.put("User_Email",UserTB.getEmail());
        data.put("context", String.valueOf(txt.getText()));
        data.put("OuserNM", "탈퇴내역입니다." );
        Call<String> call = apiService.reportmail(data);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String test = response.body();
//                Log.d("DropUserEmail 요청 성공 "," context :"+ test);
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                Log.d("DropUserEmail","메일 요청 실패");
                finish();
            }
        });
    }
    /** 체크된 박스들 비트연산  */
    public void setCheckBit(CheckedTextView[] array) {
        int Num = 0;
        for (CheckedTextView checkedTextView : array) {
            if(checkedTextView.isChecked()){
                selection |= (1 << Num+1);
                Num++;
            }else {
                selection &= ~(1 << Num+1);
                Num++;
            }
        }
        selection >>= 1;
        DropMap.put("CONT",selection);
    }

    private void disableButtonForOneSecond() {
        // 버튼 클릭 불가능하도록 상태 변경
        isButtonClickable = false;
        GO_Drop.setEnabled(false);

        // 1초 후에 버튼 클릭 가능하도록 상태 변경
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isButtonClickable = true;
                GO_Drop.setEnabled(true);
            }
        }, 1000); // 1000 밀리초 = 1초
    }

    public void backToSetting() {
        cancel_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}