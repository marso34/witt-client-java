package com.example.healthappttt.Profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.R;
import com.example.healthappttt.interface_.ServiceApi;

public class EvaluationRecdActivity extends AppCompatActivity {
    private ServiceApi apiService;
    private PreferenceHelper UserTB;
    private SQLiteUtil sqLiteUtil;

    String myPK,PK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_recd);

        Intent intent = getIntent();
        PK = intent.getStringExtra("PK");//넘겨 받은 PK
        myPK = String.valueOf(UserTB.getPK());// 로컬 내 PK

        if (PK.equals(myPK)){ //나

            sqLiteUtil = SQLiteUtil.getInstance(); // SQLiteUtil 객체 생성
//            sqLiteUtil.setInitView(this, "REVIEW_TB");//리뷰 목록 로컬 db
//            ReviewList = sqLiteUtil.SelectReviewUser();//SELECT * FROM REVIEW_TB


        }else {//상대
            apiService = RetrofitClient.getClient().create(ServiceApi.class); // create메서드로 api서비스 인터페이스의 구현제 생성

//            ReviewList = new ArrayList<>();
//            int PKI = Integer.parseInt(PK);
//            UserKey userKey = new UserKey(PKI);
//
//            getReviewList(userKey);//비동기 호출


        }
    }











}