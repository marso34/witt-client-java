package com.example.healthappttt.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.R;
import com.example.healthappttt.interface_.ServiceApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {

    private ServiceApi apiService;
    private PreferenceHelper UserTB;
    private CheckedTextView[] checkedTextViews; //체크박스들 모음
    private int selection, mypk,otherUserKey;
    private Button GO_RPT;
    private TextView reportname;
    private EditText txt;
    private String otherUserName;
    private Map<String, Object> RPTMap; //서버로 보내는 데이터
    private boolean isButtonClickable = true;

    ImageView cancle_report;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        UserTB = new PreferenceHelper("UserTB",this);
        apiService = RetrofitClient.getClient().create(ServiceApi.class);
        selection = 0;

        Intent intent = getIntent();
        otherUserName = intent.getStringExtra("otherUserName");
        otherUserKey = Integer.parseInt(intent.getStringExtra("otherUserKey"));
        mypk = Integer.parseInt(intent.getStringExtra("mypk"));

        Log.d("mypk", String.valueOf(mypk));

        RPTMap = new HashMap<>();
        RPTMap.put("mypk",mypk);
        RPTMap.put("otherpk",otherUserKey);


        setview();// 체크뷰 및 다른뷰 정의
        setCheckedTextViewListeners(); //체크박스 클릭 이벤트 처리
        RPTclick(); //신고하기

    }


    public void setview() {
        GO_RPT = findViewById(R.id.GO_RPT);
        txt = findViewById(R.id.RPT_txt);
        reportname = findViewById(R.id.reportname);
        reportname.setText(otherUserName + "님을");


        checkedTextViews = new CheckedTextView[] {
                findViewById(R.id.RPT_ck1),
                findViewById(R.id.RPT_ck2),
                findViewById(R.id.RPT_ck3),
                findViewById(R.id.RPT_ck4),
                findViewById(R.id.RPT_ck5),
                findViewById(R.id.RPT_ck8)
        };

        cancle_report = findViewById(R.id.crt);
        cancle_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                    checkedTextView.setTextAppearance(ReportActivity.this, R.style.report);


                }
            }
        };

        for (CheckedTextView checkedTextView : checkedTextViews) { //체크리스트 하나하나 onclick리스너 등록
            checkedTextView.setOnClickListener(checkedListener);
        }
    }

    public void RPTclick() {
        GO_RPT.setOnClickListener(new View.OnClickListener() {
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

                        disableButtonForOneSecond(); //1초간 클릭 막기

                        Toast.makeText(ReportActivity.this, "신고 내용을 하나 이상 체크해주세요", Toast.LENGTH_SHORT).show();
                    }else { //체크는 안하고 텍스트만 적은 경우 -> 이메일 전송 후 창 닫기
                        Log.d("ReportActivity : ", "체크표시를 안하고 텍스트만 적었을때");
                        v.setClickable(true);
                        sendEmail();
                    }
                } else {
                    setCheckBit(checkedTextViews);
                    if(inputText.isEmpty()){
                        //서버db에 저장 and email 전송 X
                        Log.d("ReportActivity : ","체크표시하고 텍스트 안적었을때" );
                        RPT(RPTMap, false);
                    }else {
                        //서버db에 저장 and email 전송 O
                        Log.d("ReportActivity : ","둘다 적었을때");
                        RPT(RPTMap, true);
                    }

                }
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
        RPTMap.put("CONT",selection);
    }

    public void sendEmail() {
//        Intent email = new Intent(Intent.ACTION_SEND);
//        email.setType("plane/text");
//        String[] address = {"hwstar1204@gmail.com"};
//        email.putExtra(Intent.EXTRA_EMAIL,address);
//        email.putExtra(Intent.EXTRA_SUBJECT,"Witt 신고");
//        email.putExtra(Intent.EXTRA_TEXT,txt.getText());
//        startActivity(email);

        Map<String,String> data = new HashMap<>();
        data.put("User_Email",UserTB.getEmail());
        data.put("context", String.valueOf(txt.getText()));
        data.put("OuserNM",otherUserName);

        Call<String> call = apiService.reportmail(data);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String test = response.body();
                Log.d("reportEmail 요청 성공 "," context :"+ test);
                finish();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("reportEmail","메일 요청 실패");
                finish();
            }
        });
    }

    public void RPT(Map<String, Object> RPTMap, boolean i) {

        Call<String> call = apiService.updateRPT(RPTMap);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String d = response.body();
                    Log.d("ReportActivity ","신고성공");
                    //email 전송
                    if(i){ sendEmail(); }
                    finish();
                } else {
                    // 요청이 실패했을 때의 처리 로직
                    Log.d("ReportActivity ","신고실패");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("ReportActivity ", "서버 응답 실패.");
            }
        });

    }
    private void disableButtonForOneSecond() {
        // 버튼 클릭 불가능하도록 상태 변경
        isButtonClickable = false;
        GO_RPT.setEnabled(false);

        // 1초 후에 버튼 클릭 가능하도록 상태 변경
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isButtonClickable = true;
                GO_RPT.setEnabled(true);
            }
        }, 1000); // 1000 밀리초 = 1초
    }

}
