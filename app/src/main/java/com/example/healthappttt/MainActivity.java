package com.example.healthappttt;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.healthappttt.Chat.ChattingFragment;
import com.example.healthappttt.Data.Chat.SocketSingleton;
import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Data.User.BlackListData;
import com.example.healthappttt.Data.User.GetUserInfo;
import com.example.healthappttt.Data.User.ReviewListData;
import com.example.healthappttt.Data.User.UserKey;
import com.example.healthappttt.Data.User.UserProfile;
import com.example.healthappttt.Data.User.WittListData;
import com.example.healthappttt.Home.HomeFragment;
import com.example.healthappttt.Home.alarmActivity;
import com.example.healthappttt.Profile.MyProfileActivity;
import com.example.healthappttt.Record.RecordFragment;
import com.example.healthappttt.Routine.RoutineFragment;
import com.example.healthappttt.Sign.LoginActivity;
import com.example.healthappttt.WorkOut.ExerciseRecordActivity;
import com.example.healthappttt.databinding.ActivityMainBinding;
import com.example.healthappttt.interface_.AlarmRecever;
import com.example.healthappttt.interface_.DataReceiverService;
import com.example.healthappttt.interface_.ServiceApi;
import com.example.healthappttt.interface_.ServiceTracker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1001;
    ActivityMainBinding binding;
    private long backPressedTime = 0;
    private Toast toast;
    private ServiceApi apiService;
    private PreferenceHelper prefhelper;
    private BlackListData BlackList;
    private ReviewListData ReviewList;
    private WittListData wittList;
    UserKey userKey;
    private SQLiteUtil sqLiteUtil;

    private boolean isConnected = false;  // 소켓 연결 여부 확인

    private static final int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;
    private int tempItemID;

    private LoginActivity loginActivity;
    private int dayOfWeek;
    Intent serviceIntent;
    private int login;
    private SocketSingleton socketSingleton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login = 1;

        String uk = getIntent().getStringExtra("userKey");

        if(uk != null){
            userKey = new UserKey(Integer.parseInt(uk));
        }
        else Log.d(TAG, "onCreate: 유저키 없음");
        Log.d("sub2에서 받은pk:", String.valueOf(userKey.getPk()));
        createNotificationChannelAndSendNotification();
        apiService = RetrofitClient.getClient().create(ServiceApi.class); // create메서드로 api서비스 인터페이스의 구현제 생성

        sqLiteUtil = SQLiteUtil.getInstance(); //sqllite 객체
        prefhelper = new PreferenceHelper("UserTB",this);
        prefhelper.setPK(Integer.parseInt(uk));

        getuserProfile(userKey); //유저키

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // MyService 실행
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            // Foreground service permission not granted, handle it accordingly (e.g., request permission)
        } else {
            // Foreground service permission granted, start the service
            serviceIntent = new Intent(this, DataReceiverService.class);
            startService(serviceIntent);
            DataReceiverService.setNormalExit(false);
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
                        replaceFragment(RoutineFragment.newInstance(dayOfWeek, prefhelper.getPK()));
                        break;
                    case R.id.chatting:
                        binding.viewName.setText("채팅");
                        replaceFragment(new ChattingFragment());
                        break;
                    case R.id.profile:
                        binding.viewName.setText("기록");
                        replaceFragment(new RecordFragment());
                        break;
                }

            }
            int runningServices = ServiceTracker.countRunningServices(this, DataReceiverService.class);
            Log.d("MainActivity", "Running services: " + runningServices);
            return true;
        });

        binding.fab.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ExerciseRecordActivity.class);
            intent.putExtra("dayOfWeek", dayOfWeek);
            startActivity(intent);
        });

        binding.myInformation.setOnClickListener(view -> {
                //showUserInfoPopup(useremail); // 자신의 이메일 정보를 보여주는 팝업
            Intent intent = new Intent(MainActivity.this, MyProfileActivity.class);
            intent.putExtra("PK", uk);
            //Log.d("main에서 넘겨주는 myPK",uk);
            startActivity(intent);
        });
        binding.alarm.setOnClickListener(view->{
            openMyStartActivity(alarmActivity.class);
        });
        Log.d("prefhelper", "USER_PK:" + prefhelper.getPK()); //저장된 유저의 pk값 가져오기
        getBlackList(userKey);
        getReviewList(userKey);
        getWittHistory(userKey);
//         uploadImageToServer(url, userKey.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setAlarmTimer();
        if (login == 0 && serviceIntent != null) {
            stopService(serviceIntent);
            serviceIntent = null;
            Log.d(TAG, "메인 종료");
        }
    }

    public void goToRoutine(int dayOfWeekT) {
        int temp = dayOfWeek;
        dayOfWeek = dayOfWeekT;
        binding.bottomNav.setSelectedItemId(R.id.routine);
        dayOfWeek = temp;
    }

    private void setAlarmTimer() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 1);

        Intent intent = new Intent(this, AlarmRecever.class);
        // ...

        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE); // 또는 PendingIntent.FLAG_MUTABLE
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            }
        }
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
                        socketSingleton = SocketSingleton.getInstance(getBaseContext());
//             Log.d("Profile", "USER_PK: " + USER_PK + ", Email: " + Email + ", IP: " + IP + ", Platform: " + Platform + ", User_NM: " + User_NM + ", User_Img: " + User_Img + "PW: " + PW);

                    } else {
                        //서버에서 반환된 값이 null인 경우 처리할 코드
                        Log.d("MainActivity", "Response body is null"+response.body());
                    }

                } else {
                    // 서버 응답이 실패했을때
                    Toast.makeText(MainActivity.this, "서버 연결 실패", Toast.LENGTH_SHORT).show();
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


    // 알림 허용 권한을 확인하고 요청하는 메서드
    private static final String CHANNEL_ID = "my_channel_id"; // 채널 ID

    // 알림 채널 생성과 푸시 알림 전송을 위한 메서드
    private void createNotificationChannelAndSendNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null && !notificationManager.areNotificationsEnabled()) {
                // 채널 생성
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);

                // 알림 허용 권한 요청
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName())
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, NOTIFICATION_PERMISSION_REQUEST_CODE);
            } else {
                // 이미 알림 허용 권한이 있는 경우

            }
        } else {
            // Android Oreo 이전 버전은 채널 생성이 필요 없음
        }
    }


    // 권한 요청 결과를 처리하는 메서드 (onActivityResult를 사용하도록 액티비티에서 오버라이드 해야 합니다.)private void createNotificationChannelAndSendNotification() {
    //    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    //        NotificationManager notificationManager = getSystemService(NotificationManager.class);
    //        if (notificationManager != null && !notificationManager.areNotificationsEnabled()) {
    //            // 채널 생성
    //            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My Channel", NotificationManager.IMPORTANCE_HIGH);
    //            notificationManager.createNotificationChannel(channel);
    //
    //            // 알림 허용 권한 요청
    //            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
    //                    .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName())
    //                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    //            startActivityForResult(intent, NOTIFICATION_PERMISSION_REQUEST_CODE);
    //        } else {
    //            // 이미 알림 허용 권한이 있는 경우에는 알림을 보내지 않음
    //            // 또는 허용 여부를 확인하는 로직을 추가하여 필요한 경우에만 알림을 보낼 수도 있음
    //            // 예시: sendNotification() 함수를 호출하기 전에 조건을 추가하여 필요한 경우에만 알림을 보냄
    //        }
    //    } else {
    //        // Android Oreo 이전 버전은 채널 생성이 필요 없음
    //        sendNotification();
    //    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            // 알림 허용 권한 설정화면에서 돌아온 경우
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                if (notificationManager != null && notificationManager.areNotificationsEnabled()) {
                    // 사용자가 알림 허용 권한을 부여한 경우
                    // 여기서 푸시 알림을 보내는 작업을 수행하세요.
                    sendNotification();
                } else {
                    // 사용자가 알림 허용 권한을 거부한 경우 또는 아직 설정하지 않은 경우
                    // 권한을 부여해야 정상적인 동작을 하도록 안내해줄 수 있습니다.
                }
            }
        }
    }
    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.witt_logo)
                .setContentTitle("Notification Title")
                .setContentText("Notification Content")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
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
                            byte[] User_Img = Review.getUser_Img();

                            ReviewList = new ReviewListData(Review_PK, User_FK, RPT_User_FK, Text_Con, Check_Box, TS, User_NM, User_Img); //서버에서 받아온 데이터 형식으로 바꿔야함
                            Log.d("ReviewList_main에서 객체화한거", String.valueOf(Review.getReview_PK()));
                            Log.d("ReviewList_main에서 객체화한거",Review.getText_Con());
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
                            byte[] User_Img = Witt.getUser_Img();

                            wittList = new WittListData(RECORD_PK,USER_FK,OUser_FK,TS,User_NM,User_Img);
                            Log.d("WittHistory_main에서", String.valueOf(Witt.getUser_NM()));
                            Log.d("WittHistory_main에서", String.valueOf(Witt.getTS()));
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
//        sqLiteUtil = SQLiteUtil.getInstance();
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
            Log.d("SaveReviewList 메서드", "저장 완료");
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
            Log.d("SaveBlackList 메서드", "저장 완료");
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
            Log.d("SaveWittList 메서드", "저장 완료");
        }
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
            showGuide("뒤로 버튼을 한번 더 누르시면 종료됩니다.");
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






    private void openMyStartActivity(Class c) {
        // Intent를 사용하여 mystartActivity를 시작합니다.
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

}