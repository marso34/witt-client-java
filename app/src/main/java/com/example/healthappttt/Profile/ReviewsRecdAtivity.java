package com.example.healthappttt.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Data.User.BlackListData;
import com.example.healthappttt.Data.User.ReviewListData;
import com.example.healthappttt.Data.User.UserKey;
import com.example.healthappttt.Data.User.WittListData;
import com.example.healthappttt.R;
import com.example.healthappttt.User.BlockUserAdapter;
import com.example.healthappttt.interface_.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsRecdAtivity extends AppCompatActivity {
    private ServiceApi apiService;
    private PreferenceHelper UserTB;
    private SQLiteUtil sqLiteUtil;

    androidx.appcompat.widget.SearchView searchView;
    TextView listCnt;
    ArrayList<BlackListData> BlackList;
    ArrayList<ReviewListData> ReviewList, filteredList;
    ReviewListData ReviewListdata;
    ArrayList<WittListData> WittList;
    BlockUserAdapter ReviewAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    int myPK,PK;
    String ReviewCnt;
    Button cancel_review;
    TextView nullreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_recd);

        listCnt = findViewById(R.id.listCnt);
        //searchView = findViewById(R.id.review_search);
        recyclerView = findViewById(R.id.Review_recycle);
        filteredList = new ArrayList<>();
        UserTB = new PreferenceHelper("UserTB", this);


        Intent intent = getIntent();
        PK = intent.getIntExtra("PK",0);//넘겨 받은 PK
        myPK =UserTB.getPK();// 로컬 내 PK

        if (PK == myPK) { //나의 받은 후기
//            Log.d("나의 받은 후기 ", PK + " - " + myPK);

            sqLiteUtil = SQLiteUtil.getInstance(); // SQLiteUtil 객체 생성
            sqLiteUtil.setInitView(this, "REVIEW_TB");//리뷰 목록 로컬 db
            ReviewList = sqLiteUtil.SelectReviewUser();//SELECT * FROM REVIEW_TB

            setView();



        } else {//상대가 받은 후기
//            Log.d("상대가 받은 후기 상대 pk: ", PK + " - " + myPK);
            apiService = RetrofitClient.getClient().create(ServiceApi.class); // create메서드로 api서비스 인터페이스의 구현제 생성
            ReviewList = new ArrayList<>();
            UserKey userKey = new UserKey(PK);

            getReviewList(userKey);//비동기 호출

        }
        cancel_review = findViewById(R.id.cancel_review);
        cancel_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


//        //검색 관련 매서드
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                // 검색 버튼을 누를 때 호출됨
//                // s 변수에 검색창에 입력된 텍스트가 담겨있습니다.
//                // 이곳에서 원하는 동작을 수행하면 됩니다.
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                // 검색창의 텍스트가 변경될 때마다 호출됨
//                // s 변수에 검색창에 입력된 텍스트가 담겨있습니다.
//                // 이곳에서 원하는 동작을 수행하면 됩니다.
//                searchFilter(s);
//                return false;
//            }
//        });
    }


    public void setView() {
        ReviewAdapter = new BlockUserAdapter(BlackList,ReviewList,WittList ,this);//어뎁터에 차단 목록 생성

        ReviewCnt = String.valueOf(ReviewList.size());//리뷰 리스트 개수 표시
        Log.d("리뷰리스트 개수: ", ReviewCnt);
        listCnt.setText(ReviewCnt);

        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(ReviewAdapter);


        ReviewAdapter.notifyDataSetChanged(); //변경점을 어뎁터에 알림
    }

    //검색창에서 텍스트가 바뀔때마다 해당 텍스트에 해당하는 요소를 보여주는 매서드
//    public void searchFilter(String searchText) {
//        filteredList.clear();
//
//        for(int i = 0; i < ReviewList.size(); i++){
//            if(ReviewList.get(i).getUser_NM().toLowerCase().contains(searchText.toLowerCase())) {
//                filteredList.add(ReviewList.get(i));
//            }
//        }
//        ReviewAdapter.filterReviewList(filteredList);
//    }

    //API 요청 후 응답을 SQLite로 받은후기 데이터 로컬 저장
    private void getReviewList(UserKey userKey){
        Call<List<ReviewListData>> call = apiService.getReviewList(userKey);
        call.enqueue(new Callback<List<ReviewListData>>() {
            @Override
            public void onResponse(Call<List<ReviewListData>> call, Response<List<ReviewListData>> response) {
                if (response.isSuccessful()) {
                    List<ReviewListData> RuserList = response.body();
                    if (RuserList != null) {
                        for (ReviewListData Review : RuserList) {
                            // 처리 로직
                            Log.d("ReviewList데이터",Review.getUser_NM());
                            int Review_PK = Review.getReview_PK();
                            int User_FK = Review.getUser_FK();
                            int RPT_User_FK = Review.getRPT_User_FK();
                            String Text_Con = Review.getText_Con();
                            int Check_Box = Review.getCheck_Box();
                            String TS = Review.getTS();
                            String User_NM = Review.getUser_NM();
                            String User_Img = Review.getUser_Img();

                            ReviewListdata = new ReviewListData(Review_PK, User_FK, RPT_User_FK, Text_Con, Check_Box, TS, User_NM, User_Img); //서버에서 받아온 데이터 형식으로 바꿔야함
                            Log.d("Review에서 객체화한거", String.valueOf(Review.getReview_PK()));
                            Log.d("Review에서 객체화한거",Review.getText_Con());

                            ReviewList.add(ReviewListdata);
                        }
                        setView();
                    } else {
                        Log.d("getReviewList","리스트가 null");
                            nullreview.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                    }
                }else {
                    Log.e("getReviewList", "응답 오류 응답 코드: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<ReviewListData>> call, Throwable t) {
                Log.e("getReviewList", "API 요청실패, 에러메세지: " + t.getMessage());
            }
        });
    }
}