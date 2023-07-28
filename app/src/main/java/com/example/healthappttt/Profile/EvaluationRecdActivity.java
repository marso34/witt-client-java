package com.example.healthappttt.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Data.User.ReviewListData;
import com.example.healthappttt.R;
import com.example.healthappttt.databinding.ActivityEvaluationRecdBinding;
import com.example.healthappttt.interface_.ServiceApi;

import java.util.ArrayList;

public class EvaluationRecdActivity extends AppCompatActivity {
    ActivityEvaluationRecdBinding binding;
    private ServiceApi apiService;
    private PreferenceHelper UserTB;
    private SQLiteUtil sqLiteUtil;

    String myPK,PK;
    ArrayList<ReviewListData> EvalList;
    TextView[] GtextView,BtextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_recd);

        binding = ActivityEvaluationRecdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        UserTB = new PreferenceHelper("UserTB", this);

        Intent intent = getIntent();
        PK = intent.getStringExtra("PK");//넘겨 받은 PK
        myPK = String.valueOf(UserTB.getPK());// 로컬 내 PK

        if (PK.equals(myPK)){ //나

            sqLiteUtil = SQLiteUtil.getInstance(); // SQLiteUtil 객체 생성
            sqLiteUtil.setInitView(this, "REVIEW_TB");//리뷰 목록 로컬 db
            EvalList = sqLiteUtil.SelectEvaluation();//SELECT Check_Box FROM REVIEW_TB

            viewsetting();

            int[] GoodEvalCNT = {0,0,0,0};  int[] BadEvalCNT = {0,0,0, 0,0,0, 0,0};

            for(ReviewListData Eval : EvalList) {
                int EvalNum = Eval.getCheck_Box();

                if(EvalNum >= 32768 ){//좋은 후기
                    Log.d("좋은후기  ",EvalNum+"" );

                    int j = 0;
                    //비트 연산으로 자리 하나씩 검사
                    for(int i = 3; i>=0; i--){
                        int bit = (EvalNum >> i) & 1;
                        if(bit == 1) {
                            Log.d("비트 연산  ",i + "번째 비트 켜짐" );
                            GoodEvalCNT[j]++;
                        } else{
                            Log.d("비트 연산  ",i + "비트 X" );
                        }
                        j++;
                    }
//                    if(EvalNum == 0x8001){
//                        GoodEvalCNT[0]++;   // 친절하고 매너가 좋아요
//                    }if(EvalNum == 0x8002){
//                        GoodEvalCNT[1]++;   // 시간약속을 잘 지켜요
//                    }if(EvalNum == 0x8004){
//                        GoodEvalCNT[2]++;    // 응답이 빨라요
//                    }if(EvalNum == 0x8008){
//                        GoodEvalCNT[3]++;   // 운동을 잘 알려줘요
                }else { //나쁜 후기
                    Log.d("나쁜후기  ",EvalNum+"" );

                    int j = 0;
                    //비트 연산으로 자리 하나씩 검사
                    for(int i = 7; i>=0; i--){
                        int bit = (EvalNum >> i) & 1;
                        if(bit == 1) {
                            Log.d("비트 연산  ",i + "번째 비트 켜짐" );
                            BadEvalCNT[j]++;
                        } else{
                            Log.d("비트 연산  ",i + "비트 X" );
                        }
                        j++;
                    }
//                    if(EvalNum == 0x01){
//                        BadEvalCNT[0]++;   // 원하지 않는 운동을 계속 강요해요
//                    }if(EvalNum == 0x02){
//                        BadEvalCNT[1]++;   // 시간약속을 안 지켜요
//                    }if(EvalNum == 0x04){
//                        BadEvalCNT[2]++;    // 채팅 메시지를 읽고도 답장이 없어요
//                    }if(EvalNum == 0x08){
//                        BadEvalCNT[3]++;    // 약속 시간을 명확하게 알려주지 않아요
//                    }if(EvalNum == 0x10){
//                        BadEvalCNT[4]++;   // 약속 시간과 장소를 정한 후 운동 직전 취소했어요
//                    }if(EvalNum == 0x20){
//                        BadEvalCNT[5]++;   // 약속 장소에 나타나지 않았어요
//                    }if(EvalNum == 0x40){
//                        BadEvalCNT[6]++;   // 반말을 사용해요
//                    }if(EvalNum == 0x80){
//                        BadEvalCNT[7]++;   // 불친절해요
//                    }

                }


            }
            for(int i=0; i<8; i++) {
                int b = BadEvalCNT[i];
                TextView textView = BtextView[i];
                View view = binding.getRoot().findViewById(
                        i == 0 ? R.id.r0_0 :
                        i == 1 ? R.id.r0_1 :
                        i == 2 ? R.id.r0_2 :
                        i == 3 ? R.id.r0_3 :
                        i == 4 ? R.id.r0_4 :
                        i == 5 ? R.id.r0_5 :
                        i == 6 ? R.id.r0_6 :
                                 R.id.r0_7
                );
                if (b == 0) {
                    view.setVisibility(View.GONE);
                } else {
                    textView.setText(String.valueOf(b));
                }
            }
            for(int i=0; i<4; i++) {
                int g = GoodEvalCNT[i];
                TextView textView = GtextView[i];
                View view = binding.getRoot().findViewById(
                    i == 0 ? R.id.r1_0 :
                    i == 1 ? R.id.r1_1 :
                    i == 2 ? R.id.r1_2 :
                             R.id.r1_3
                );
                if (g == 0) {
                    view.setVisibility(View.GONE);
                } else {
                    textView.setText(String.valueOf(g));
                }
            }



        }else {//상대
            apiService = RetrofitClient.getClient().create(ServiceApi.class); // create메서드로 api서비스 인터페이스의 구현제 생성

//            ReviewList = new ArrayList<>();
//            int PKI = Integer.parseInt(PK);
//            UserKey userKey = new UserKey(PKI);
//
//            getReviewList(userKey);//비동기 호출


        }




    }

    public void viewsetting(){
        //Good
        TextView listCnt1_0 = findViewById(R.id.listCnt1_0);
        TextView listCnt1_1 = findViewById(R.id.listCnt1_1);
        TextView listCnt1_2 = findViewById(R.id.listCnt1_2);
        TextView listCnt1_3 = findViewById(R.id.listCnt1_3);
        //Bad
        TextView listCnt0_0 = findViewById(R.id.listCnt0_0);
        TextView listCnt0_1 = findViewById(R.id.listCnt0_1);
        TextView listCnt0_2 = findViewById(R.id.listCnt0_2);
        TextView listCnt0_3 = findViewById(R.id.listCnt0_3);
        TextView listCnt0_4 = findViewById(R.id.listCnt0_4);
        TextView listCnt0_5 = findViewById(R.id.listCnt0_5);
        TextView listCnt0_6 = findViewById(R.id.listCnt0_6);
        TextView listCnt0_7 = findViewById(R.id.listCnt0_7);

        GtextView = new TextView[]{listCnt1_0, listCnt1_1, listCnt1_2, listCnt1_3};
        BtextView = new TextView[]{listCnt0_0, listCnt0_1, listCnt0_2, listCnt0_3, listCnt0_4, listCnt0_5, listCnt0_6, listCnt0_7};


    }









}