package com.example.healthappttt.Activity;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.healthappttt.Data.BlackListData;
import com.example.healthappttt.Data.GetUserInfo;
import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.ReviewListData;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Data.UserKey;
import com.example.healthappttt.Data.UserProfile;
import com.example.healthappttt.Fragment.ChattingFragment;
import com.example.healthappttt.Fragment.HomeFragment;
import com.example.healthappttt.Fragment.RoutineFragment;
import com.example.healthappttt.R;
import com.example.healthappttt.databinding.ActivityMainBinding;
import com.example.healthappttt.interface_.DataReceiverService;
import com.example.healthappttt.interface_.ServiceApi;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private long backPressedTime = 0;
    private Toast toast;
    private ServiceApi apiService;
    private PreferenceHelper prefhelper;
    private BlackListData BlackList;
    private ReviewListData ReviewList;
    //유저키를 UserKey 자료형으로 받음 ( 유동적으로 로그인에서 넘겨준 pk값이 들어가야함 )
    UserKey userKey;
    private SQLiteUtil sqLiteUtil;
    private boolean isConnected = false;  // 소켓 연결 여부 확인

    private static final int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;
    private int tempItemID;
    Button mGoogleSignOutButton;
    private LoginActivity loginActivity;
    private int dayOfWeek;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String uk = getIntent().getStringExtra("userKey");
        if(uk != null){
            userKey = new UserKey(Integer.parseInt(uk));
        }
        else Log.d(TAG, "onCreate: 유저키 없음");
        Log.d(TAG, "onCreateuserkey: "+uk);
        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // MyService 실행
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            // Foreground service permission not granted, handle it accordingly (e.g., request permission)
        } else {
            // Foreground service permission granted, start the service
            Intent serviceIntent = new Intent(this, DataReceiverService.class);
            startService(serviceIntent);
        }

        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 현재 요일 정보


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_sign_in_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SharedPreferences sharedPref = getSharedPreferences("myPref", Context.MODE_PRIVATE);
        String useremail = sharedPref.getString("useremail", "");


        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( MainActivity.this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION}, 0 );
        }
        else{
            // 가장최근 위치정보 가져오기
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null) {
                String provider = location.getProvider();
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                double altitude = location.getAltitude();

                Log.d("lllllllll", "위치정보 : " + provider + "\n" +
                        "위도 : " + longitude + "\n" +
                        "경도 : " + latitude + "\n" +
                        "고도  : " + altitude);

//                DocumentReference washingtonRef = db.collection("users").document(user.getUid());
//                washingtonRef
//                        .update("lat", latitude)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Log.d(TAG, "DocumentSnapshot successfully updated!");
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.w(TAG, "Error updating document", e);
//                            }
//                        });
//                washingtonRef
//                        .update("lon", longitude)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Log.d(TAG, "DocumentSnapshot successfully updated!");
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.w(TAG, "Error updating document", e);
//                            }
//                        });
            }
            else {
                Log.d("wwwwww","qeqeqeqeqe");
            }
//            위치정보를 원하는 시간, 거리마다 갱신해준다.
//            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                    5000,
//                    4,
//                    gpsLocationListener);
//            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                    5000,
//                    4,
//                    gpsLocationListener);
                    }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tempItemID = R.id.home;
        replaceFragment(new HomeFragment());


        binding.bottomNav.setOnItemSelectedListener(item -> {
            int ItemId = item.getItemId();

            if (tempItemID != ItemId) {
                tempItemID = ItemId;

                switch (ItemId) {
                    case R.id.home:
                        binding.viewName.setText("홈");
                        replaceFragment(new HomeFragment());
                        break;
                    case R.id.routine:
                        binding.viewName.setText("루틴");
                        replaceFragment(new RoutineFragment());
                        break;
                    case R.id.chatting:
                        binding.viewName.setText("채팅");
                        replaceFragment(new ChattingFragment());
                        break;
                    case R.id.profile:
                        binding.viewName.setText("기록");
//                        replaceFragment(new ProfileFragment()); // 운동 기록 프래그먼트로 나중에 수정
                        break;
                }

            }

            return true;
        });

        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ExerciseRecordActivity.class);
            intent.putExtra("dayOfWeek", dayOfWeek);
            startActivity(intent);
        });
        binding.signOutButton.setOnClickListener(view -> {
            signOut();
        });
        binding.myInformation.setOnClickListener(view -> {
                //showUserInfoPopup(useremail);
            Intent intent = new Intent(MainActivity.this, MyProfileActivity.class);
            startActivity(intent);
        });

        //api 요청 인터페이스 가져오기
        apiService = RetrofitClient.getClient().create(ServiceApi.class); // create메서드로 api서비스 인터페이스의 구현제 생성
        prefhelper = new PreferenceHelper(this);
        sqLiteUtil = SQLiteUtil.getInstance(); //sqllite 객체
        getuserProfile(userKey); //유저키
        //로그인했을때 넘겨받는 정보를 파라미터로 넣는다.  email or phone_num 비교해서 해당하는 유저의 키를 받아온다.
        //유저의 pk를 그대로 받을수있으면 필요가 없음 다른방향( 다른유저의 키를 가져오는 느낌)으로 가야함
        Log.d("prefhelper", "USER_PK:" + prefhelper.getPK()); //저장된 유저의 pk값 가져오기
        getBlackList(userKey);//매개변수 prefhelper.getPK() 변경하여 테스팅 필요
        getReviewList(userKey);//매개변수 prefhelper.getPK() 변경하여 테스팅 필요

    }

    //API 요청 후 응답을 shared로 유저테이블 데이터 로컬 저장
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
//             Log.d("Profile", "USER_PK: " + USER_PK + ", Email: " + Email + ", IP: " + IP + ", Platform: " + Platform + ", User_NM: " + User_NM + ", User_Img: " + User_Img + "PW: " + PW);

                    } else {
                        //서버에서 반환된 값이 null인 경우 처리할 코드
                        Log.d("MainActivity", "Response body is null"+response.body());
                    }

                } else {
                    // 서버 응답이 실패했을때
                    Log.d("MainActivity", "서버 응답 실패. 상태코드:" + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<UserProfile>> call, Throwable t) {
                // API 호출에 실패한 경우 처리합니다.
                Log.d("MainActivity", "API호출 실패:");
                Log.e("API_CALL", "API call failed: " + t.getMessage());
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
                            byte[] User_Img = Black.getUser_Img();
                            BlackList = new BlackListData(BL_PK, User_NM, OUser_FK, TS,User_Img); //서버에서 받아온 데이터 형식으로 바꿔야함
                            SaveBlackList();//로컬db에 차단목록 저장 매서드
                        }
                    }
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
                            byte[] User_Img = Review.getUser_Img();

                            ReviewList = new ReviewListData(Review_PK, User_FK, RPT_User_FK, Text_Con, Check_Box, TS, User_NM, User_Img); //서버에서 받아온 데이터 형식으로 바꿔야함
                            SaveReviewList();//로컬db에 받은 후기 저장 매서드
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<List<ReviewListData>> call, Throwable t) {
                Log.e("getReviewList", "API 요청실패, 에러메세지: " + t.getMessage());
            }
        });
    }

    private void SaveReviewList() {
        sqLiteUtil.setInitView(this,"RREVIEW_TB");
        sqLiteUtil.insert(ReviewList);
        Log.d("ReviewList데이터 저장 매서드","저장완료");
    }


    private void SaveBlackList() {
        sqLiteUtil.setInitView(this, "BLACK_LIST_TB");
        sqLiteUtil.insert(BlackList);
        Log.d("BlackList데이터 저장 매서드","저장완료");
    }



    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // 위치 리스너는 위치정보를 전달할 때 호출되므로 onLocationChanged()메소드 안에 위지청보를 처리를 작업을 구현 해야합니다.
            String provider = location.getProvider();  // 위치정보
            double longitude = location.getLongitude(); // 위도
            double latitude = location.getLatitude(); // 경도
            double altitude = location.getAltitude(); // 고도
            Log.d("lllllllll", "위치정보 : " + provider + "\n" +
                    "위도 : " + longitude + "\n" +
                    "경도 : " + latitude + "\n" +
                    "고도  : " + altitude);
        } public void onStatusChanged(String provider, int status, Bundle extras) {

        } public void onProviderEnabled(String provider) {

        } public void onProviderDisabled(String provider) {

        }
    };


    private void replaceFragment (Fragment fragment){ //프래그먼트 설정
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if( fragment.isAdded() )
        {
            // Fragment 가 이미 추가되어 있으면 삭제한 후, 새로운 Fragment 를 생성한다.
            // 새로운 Fragment 를 생성하지 않으면 2번째 보여질 때에 Fragment 가 보여지지 않는 것 같습니다.
            fragmentTransaction.remove( fragment );
            fragment = new Fragment();
        }
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Update UI after sign out
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("MainActivity", "Google sign out failed", e);
                    }
                });
    }

    private void showUserInfoPopup(String userEmail) {

       // Toast.makeText(MainActivity.this, userEmail, Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.my_popup_layout, null);

        GetUserInfo.getUserInfo(userEmail, new Callback<GetUserInfo>() {
            @Override
            public void onResponse(Call<GetUserInfo> call, Response<GetUserInfo> response) {
                if (response.isSuccessful()) {
                    GetUserInfo userInfo = response.body();



                    TextView tvUserName = view.findViewById(R.id.tv_user_info_name);
                    TextView tvUserEmail = view.findViewById(R.id.tv_user_info_email);
                    Button btnClose = view.findViewById(R.id.btn_user_info_close);

                    String userName = userInfo.getUserNm();
                    String userEmail = userInfo.getEmail();

                    tvUserName.setText(userName);
                    tvUserEmail.setText(userEmail);

                    builder.setView(view);
                    AlertDialog dialog = builder.create();

                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss(); // 팝업창 닫기
                        }
                    });
                    dialog.show();


                } else {
                    // API 호출이 실패한 경우 처리
                    // ...
                }
            }

            @Override
            public void onFailure(Call<GetUserInfo> call, Throwable t) {;
            }
        });


    }



    private void getUserInfo(String userEmail) {
        GetUserInfo.getUserInfo(userEmail, new Callback<GetUserInfo>() {
            @Override
            public void onResponse(Call<GetUserInfo> call, Response<GetUserInfo> response) {
                if (response.isSuccessful()) {
                    GetUserInfo userInfo = response.body();
                    int userPk = userInfo.getUserPk();
                    String email = userInfo.getEmail();
                    String userNm = userInfo.getUserNm();


                } else {
                    // API 호출이 실패한 경우 처리
                    // ...
                }
            }

            @Override
            public void onFailure(Call<GetUserInfo> call, Throwable t) {;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backPressedTime + 2000) {
            backPressedTime = System.currentTimeMillis();
            showGuide("\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.");
            return;
        }

        if (System.currentTimeMillis() <= backPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }

    private void showGuide(String msg) {
        toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}