package com.example.healthappttt;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.Data.Chat.SocketSingleton;
import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Data.User.BlackListData;
import com.example.healthappttt.Data.User.ReviewListData;
import com.example.healthappttt.Data.User.UserKey;
import com.example.healthappttt.Data.User.UserProfile;
import com.example.healthappttt.Data.User.WittListData;
import com.example.healthappttt.interface_.ServiceApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private SocketSingleton socketSingleton;

    private ServiceApi apiService;
    private SQLiteUtil sqLiteUtil;
    private PreferenceHelper prefhelper;

    private BlackListData BlackList;
    private ReviewListData ReviewList;
    private WittListData wittList;

    UserKey userKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        apiService = RetrofitClient.getClient().create(ServiceApi.class);
        prefhelper = new PreferenceHelper("UserTB",this);
        sqLiteUtil = SQLiteUtil.getInstance();

        //한사람당 한계정 사용시 가능함 (로그아웃 mvp에서 제외 예정)
        userKey = new UserKey(prefhelper.getPK());

        getuserProfile(userKey);
        getBlackList(userKey);
        getReviewList(userKey);
        getWittHistory(userKey);
    }

    private void getuserProfile(UserKey userKey) {
        Call<List<UserProfile>> call = apiService.getuserprofile(userKey);
        call.enqueue(new Callback<List<UserProfile>>() {
            @Override
            public void onResponse(Call<List<UserProfile>> call, Response<List<UserProfile>> response) {
                if (response.isSuccessful()) {
                    List<UserProfile> profileList = response.body();
                    // 서버에서 받은 응답을 처리하는 코드를 작성합니다.
                    if (profileList != null) {   //서버에서 반환된 값이 null이 아닌 경우 처리할 코드
                        UserProfile userProfile = profileList.get(0); // 첫번째 UserProfile 객체를 가져온다.
                        prefhelper.putProfile(userProfile); // 로컬에 UserProfile 객체를 저장한다.
                        Log.d(TAG, "onResponse:ll "+userProfile.getUSER_PK());
                        socketSingleton = SocketSingleton.getInstance(getBaseContext());

                    } else {
                        Log.d("MainActivity", "Response body is null"+response.body());
                    }

                } else {
                    Toast.makeText(SplashActivity.this, "서버 연결 실패", Toast.LENGTH_SHORT).show();
                    Log.d("MainActivity", "서버 응답 실패. 상태코드:" + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<UserProfile>> call, Throwable t) {
                Log.e("API_CALL", "API호출 실패: " + t.getMessage());
            }
        });

    }
    //API 요청 후 응답을 SQLite로 차단테이블 데이터 로컬 저장
    private void getBlackList(UserKey userKey) {
        Call<List<BlackListData>> call = apiService.getBlackList(userKey);
        call.enqueue(new Callback<List<BlackListData>>() {
            @Override
            public void onResponse(Call<List<BlackListData>> call, Response<List<BlackListData>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "ssss: a");
                    List<BlackListData> BuserList = response.body();
                    Log.d(TAG, String.valueOf(BuserList));
                    if (BuserList != null) {
                        for (BlackListData Black : BuserList) {
                            // 처리 로직
                            Log.d("BlackList데이터",Black.getUser_NM());
                            int BL_PK = Black.getBL_PK();
                            String User_NM = Black.getUser_NM();
                            int OUser_FK = Black.getOUser_FK();
                            String TS = Black.getTS();
                            String User_Img = Black.getUser_Img();
                            BlackList = new BlackListData(BL_PK, User_NM, OUser_FK, TS,User_Img); //서버에서 받아온 데이터 형식으로 바꿔야함

                            SaveBlackList(BlackList);//로컬db에 차단목록 저장 매서드
                        }
                    } else { Log.d("getBlackList","리스트가 null");}
                } else {
                    Log.e("getBlackList", "API 요청 실패. 응답 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<BlackListData>> call, Throwable t) {
                Log.e("getBlackList", "API 요청실패, 에러메세지: " + t.getMessage());
            }
        });

    }

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

                            ReviewList = new ReviewListData(Review_PK, User_FK, RPT_User_FK, Text_Con, Check_Box, TS, User_NM, User_Img); //서버에서 받아온 데이터 형식으로 바꿔야함
                            SaveReviewList(ReviewList);//로컬db에 받은 후기 저장 매서드
                        }
                    } else { Log.d("getReviewList","리스트가 null");}
                }else {
                    Log.e("getReviewList", "API 요청 실패. 응답 코드: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<ReviewListData>> call, Throwable t) {
                Log.e("getReviewList", "API 요청실패, 에러메세지: " + t.getMessage());
            }
        });
    }

    private void getWittHistory(UserKey userKey) {
        Call<List<WittListData>> call = apiService.getWittHistory(userKey);
        call.enqueue(new Callback<List<WittListData>>() {
            @Override
            public void onResponse(Call<List<WittListData>> call, Response<List<WittListData>> response) {
                if (response.isSuccessful()){
                    List<WittListData> WittList = response.body();
                    if(WittList != null){
                        for(WittListData Witt : WittList){
                            Log.d("WittList데이터", String.valueOf(Witt.getUSER_FK()));
                            int RECORD_PK = Witt.getRECORD_PK();
                            int USER_FK = Witt.getUSER_FK();
                            int OUser_FK = Witt.getOUser_FK();
                            String TS = Witt.getTS();
                            String User_NM = Witt.getUser_NM();
                            String User_Img = Witt.getUser_Img();

                            wittList = new WittListData(RECORD_PK,USER_FK,OUser_FK,TS,User_NM,User_Img);
                            SaveWittList(wittList);//로컬db에 받은 후기 저장 매서드
                        }
                    } else { Log.d("getWittHistory","리스트가 null");}
                }else{
                    Log.e("getWittHistory", "API 요청 실패. 응답 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<WittListData>> call, Throwable t) {
                Log.e("getWittHistory", "API 요청실패, 에러메세지: " + t.getMessage());
            }
        });
    }

    private void SaveReviewList(ReviewListData reviewListData) {
        sqLiteUtil.setInitView(this,"REVIEW_TB");

        //중복 PK 확인
        boolean CheckStored = false;
        List<ReviewListData> reviewList = sqLiteUtil.SelectReviewUser();
        for(ReviewListData storedData : reviewList) {
            int storedPK = storedData.getReview_PK();
            if(storedPK == reviewListData.getReview_PK()){
                CheckStored = true;
                break;
            }
        }
        if (CheckStored) {
            Log.d("SaveReviewList 메서드", "중복된 PK -> 저장 X");
        } else {
            sqLiteUtil.setInitView(this,"REVIEW_TB");
            sqLiteUtil.insertRL(reviewListData);
        }
    }


    private void SaveBlackList(BlackListData blackListData) {
//        sqLiteUtil = SQLiteUtil.getInstance();
        sqLiteUtil.setInitView(this, "BLACK_LIST_TB");

        // 중복 PK 확인
        boolean CheckStored = false;
        List<BlackListData> blackList = sqLiteUtil.SelectBlackUser();
        for (BlackListData storedData : blackList) {
            int storedPK = storedData.getBL_PK();
            if (storedPK == blackListData.getBL_PK()) {
                CheckStored = true;
                break;
            }
        }

        if (CheckStored) {
            Log.d("SaveBlackList 메서드", "중복된 PK -> 저장 X");
        } else {
            sqLiteUtil.setInitView(this, "BLACK_LIST_TB");
            sqLiteUtil.insertBL(blackListData);
        }
    }
    private void SaveWittList(WittListData wittListData){
//        sqLiteUtil = SQLiteUtil.getInstance();
        sqLiteUtil.setInitView(this,"Witt_History_TB");

        // 중복 PK 확인
        boolean CheckStored = false;
        List<WittListData> WittList = sqLiteUtil.SelectWittHistoryUser();
        for (WittListData storedData : WittList) {
            int storedPK = storedData.getRECORD_PK();
            if (storedPK == wittListData.getRECORD_PK()) {
                CheckStored = true;
                break;
            }
        }

        if (CheckStored) {
            Log.d("SaveWittList 메서드", "중복된 PK -> 저장 X");
        } else {
            sqLiteUtil.setInitView(this,"Witt_History_TB");
            sqLiteUtil.insertWH(wittListData);
        }
    }
}