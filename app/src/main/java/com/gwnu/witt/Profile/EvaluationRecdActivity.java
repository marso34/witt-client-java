package com.gwnu.witt.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gwnu.witt.Data.PreferenceHelper;
import com.gwnu.witt.Data.RetrofitClient;
import com.gwnu.witt.Data.SQLiteUtil;
import com.gwnu.witt.Data.User.ReviewListData;
import com.gwnu.witt.Data.User.UserKey;
import com.gwnu.witt.R;
import com.gwnu.witt.databinding.ActivityEvaluationRecdBinding;
import com.gwnu.witt.interface_.ServiceApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EvaluationRecdActivity extends AppCompatActivity {
    ActivityEvaluationRecdBinding binding;
    private ServiceApi apiService;
    private PreferenceHelper UserTB;
    private SQLiteUtil sqLiteUtil;

    UserKey userKey;
    int myPK,PK;
    ArrayList<ReviewListData> EvalList;
    TextView[] GtextView,BtextView;
    TextView listCnt1_0,listCnt1_1,listCnt1_2,listCnt1_3;
    TextView listCnt0_0,listCnt0_1,listCnt0_2,listCnt0_3,listCnt0_4,listCnt0_5,listCnt0_6,listCnt0_7;
    int[] GoodEvalCNT,BadEvalCNT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_recd);

        binding = ActivityEvaluationRecdBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserTB = new PreferenceHelper("UserTB", this);
        Intent intent = getIntent();
        PK = intent.getIntExtra("PK",0);//넘겨 받은 PK
        myPK = UserTB.getPK();// 로컬 내 PK

        GoodEvalCNT = new int[] {0, 0, 0, 0};
        BadEvalCNT = new int[] {0, 0, 0, 0, 0, 0, 0, 0};

        viewsetting();

        if (PK == myPK){ //나

            sqLiteUtil = SQLiteUtil.getInstance(); // SQLiteUtil 객체 생성
            sqLiteUtil.setInitView(this, "REVIEW_TB");//리뷰 목록 로컬 db
            EvalList = sqLiteUtil.SelectEvaluation();//SELECT Check_Box FROM REVIEW_TB

            setListView(EvalList);

        }else {//상대
            userKey = new UserKey(PK);
            apiService = RetrofitClient.getClient().create(ServiceApi.class); // create메서드로 api서비스 인터페이스의 구현제 생성

            Call<ArrayList<ReviewListData>> call = apiService.getOtherEval(userKey);
            call.enqueue(new Callback<ArrayList<ReviewListData>>() {
                @Override
                public void onResponse(Call<ArrayList<ReviewListData>> call, Response<ArrayList<ReviewListData>> response) {
                    if (response.isSuccessful()) {
                        ArrayList<ReviewListData> ORuserList = response.body();
                        setListView(ORuserList);
                    }else {
                        Log.e("getOtherEval", "API 요청 실패. 응답 코드: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<ReviewListData>> call, Throwable t) {
                    Log.e("getOtherEval", "API 요청실패, 에러메세지: " + t.getMessage());
                }
            });

        }


    }

    public void viewsetting(){
        //Good
         listCnt1_0 = findViewById(R.id.listCnt1_0);
         listCnt1_1 = findViewById(R.id.listCnt1_1);
         listCnt1_2 = findViewById(R.id.listCnt1_2);
         listCnt1_3 = findViewById(R.id.listCnt1_3);
        //Bad
         listCnt0_0 = findViewById(R.id.listCnt0_0);
         listCnt0_1 = findViewById(R.id.listCnt0_1);
         listCnt0_2 = findViewById(R.id.listCnt0_2);
         listCnt0_3 = findViewById(R.id.listCnt0_3);
         listCnt0_4 = findViewById(R.id.listCnt0_4);
         listCnt0_5 = findViewById(R.id.listCnt0_5);
         listCnt0_6 = findViewById(R.id.listCnt0_6);
         listCnt0_7 = findViewById(R.id.listCnt0_7);

        GtextView = new TextView[]{listCnt1_0, listCnt1_1, listCnt1_2, listCnt1_3};
        BtextView = new TextView[]{listCnt0_0, listCnt0_1, listCnt0_2, listCnt0_3, listCnt0_4, listCnt0_5, listCnt0_6, listCnt0_7};

        binding.cancelEval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void setListView(ArrayList<ReviewListData> ReviewList) {
        for(ReviewListData Eval : ReviewList) {
            int EvalNum = Eval.getCheck_Box();

            if(EvalNum >= 32768 ){//좋은 후기
                Log.d("좋은후기  ",EvalNum+"" );

                //비트 연산으로 자리 하나씩 검사
                for(int i = 3; i>=0; i--){
                    int bit = (EvalNum >> i) & 1;
                    if(bit == 1) {
                        //Log.d("비트 연산  ",i + "번째 비트 켜짐" );
                        GoodEvalCNT[i]++;
                    } else{
                        //Log.d("비트 연산  ",i + "비트 X" );
                    }
                }
                Log.d("비트 연산 결과  ",String.valueOf(GoodEvalCNT[0])+" "+String.valueOf(GoodEvalCNT[1])+" "+String.valueOf(GoodEvalCNT[2])+" "+String.valueOf(GoodEvalCNT[3]) );


            }else { //나쁜 후기
                Log.d("나쁜후기  ",EvalNum+"" );

                //비트 연산으로 자리 하나씩 검사
                for(int i = 7; i>=0; i--){
                    int bit = (EvalNum >> i) & 1;
                    if(bit == 1) {
                        //Log.d("비트 연산  ",i + "번째 비트 켜짐" );
                        BadEvalCNT[i]++;
                    } else{
                        //Log.d("비트 연산  ",i + "비트 X" );
                    }
                }
                Log.d("비트 연산 결과  ", BadEvalCNT[0]+" "+BadEvalCNT[1]+" "+BadEvalCNT[2]+" "+BadEvalCNT[3]+" "+BadEvalCNT[4]+" "+BadEvalCNT[5]+" "+BadEvalCNT[6]+" "+BadEvalCNT[7]);
            }

        }
//---------------------------------------------------------------------
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
                //Log.d("좋은후기인데 GONE 처리  ",g+"번쨰");
            } else {
                textView.setText(String.valueOf(g));
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
                //Log.d("나쁜후기인데 GONE 처리  ",b+"번쨰");
            } else {
                textView.setText(String.valueOf(b));
            }
        }

    }

}