package com.example.healthappttt.Profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.healthappttt.Data.Chat.SocketSingleton;
import com.example.healthappttt.Data.Exercise.GetRoutine;
import com.example.healthappttt.Data.Exercise.RoutineData;
import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Data.User.UserClass;
import com.example.healthappttt.Data.User.UserKey;
import com.example.healthappttt.Data.WittSendData;
import com.example.healthappttt.R;
import com.example.healthappttt.Routine.RoutineActivity;
import com.example.healthappttt.Routine.RoutineAdapter;
import com.example.healthappttt.databinding.ActivityMyprofileBinding;
import com.example.healthappttt.interface_.ServiceApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyProfileActivity extends AppCompatActivity {

    private ActivityMyprofileBinding binding;
    private ActivityResultLauncher<Intent> editProfileLauncher;
    private PreferenceHelper UserTB;
    private SQLiteUtil sqLiteUtil;
    private ServiceApi apiService;
    private UserClass userClass;
    private RoutineAdapter adapter;
    private WittSendData wittSendData;
  
    ImageButton block_btn,Reviews_btn,WittHistory_btn;
    Button PEdit, cancel_profile;
    ImageView ProfileImg;
    TextView Pname,Pgender,Pheight,Pweight,Plocatoin;
    TextView Psqaut,Pbench,Pdeadlift;
    Map<String,Object> userDefault;
    Map<String,Object> OuserDefault;
    String myPK,PK;
    String OtherName;

    int dayOfWeek;

    String otherUserKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        apiService = RetrofitClient.getClient().create(ServiceApi.class); // create메서드로 api서비스 인터페이스의 구현제 생성
        binding = ActivityMyprofileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Reviews_btn = findViewById(R.id.Reviews_Recd);
        //WittHistory_btn = findViewById(R.id.WittHistory);
        cancel_profile = findViewById(R.id.cancel_profile);
        //텍스트
        Pname = findViewById(R.id.name);
        Pgender = findViewById(R.id.gender);
//        Pheight = findViewById(R.id.Pheight);
//        Pweight = findViewById(R.id.Pweight);
        Psqaut = findViewById(R.id.Psqaut);
        Pbench = findViewById(R.id.Pbench);
        Pdeadlift = findViewById(R.id.Pdeadlift);
        //이미지,헬스장
        ProfileImg = findViewById(R.id.ProfileImg);
        Plocatoin = findViewById(R.id.MyLocation);
//-------------------------------------------------------------------------------------
        UserTB = new PreferenceHelper("UserTB",this);

        Intent intent = getIntent();//넘겨받은 pk를 담은 번들
        PK = intent.getStringExtra("PK");//넘겨 받은 PK

        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        dayOfWeek = intent.getIntExtra("dayOfWeek",calendar.get(Calendar.DAY_OF_WEEK) - 1);


        myPK = String.valueOf(UserTB.getPK());// 로컬 내 PK
        /** 마이 프로필*/
        if(PK.equals(myPK) ){ // 내 pk이면 마이 프로필
            Log.d("프로필에서 로컬pk와 넘겨받은pk",PK + " " + myPK);

            //block_btn = findViewById(R.id.block_btn); //내 프로필에만  존재
            //PEdit = findViewById(R.id.PEdit);//내 프로필에만 존재

            userDefault = new HashMap<>();// 회원가입시 입력했던 데이터들 로컬에서 받기
            userDefault.put("User_NM", UserTB.getUserData().get("User_NM")); //TODO getter pref에 만들기
            userDefault.put("gender", UserTB.getUserData().get("gender"));
            userDefault.put("height", UserTB.getUserData().get("height"));
            userDefault.put("weight", UserTB.getUserData().get("weight"));
            userDefault.put("squatValue", UserTB.getUserData().get("squatValue"));
            userDefault.put("benchValue", UserTB.getUserData().get("benchValue"));
            userDefault.put("deadValue", UserTB.getUserData().get("deadValue"));
            userDefault.put("totalValue", UserTB.getUserData().get("totalValue"));


            setDefault(userDefault); //로컬 데이터를를 화면에 세팅

            ViewChangeBlock(); // 화면전환 매서드

            // MyprofileEdit에서 넘어온 데이터 처리
            editProfileLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // result 에는 resultCode 가 있다.
                    // resultCode 의 값으로, 여러가지 구분해서 사용이 가능.
                    if (result.getResultCode() == RESULT_OK){

                        userDefault = UserTB.getUserData();// edit에서 저장되고 돌아온 후 로컬에서 다시 데이터 불러옴

                        //화면에 연결
                        binding.name.setText(userDefault.get("User_NM").toString());
//                        binding.Pheight.setText(userDefault.get("height").toString() + "cm");
//                        binding.Pweight.setText(userDefault.get("weight").toString() + "kg");
                        binding.Psqaut.setText(userDefault.get("squatValue").toString());
                        binding.Pbench.setText(userDefault.get("benchValue").toString());
                        binding.Pdeadlift.setText(userDefault.get("deadValue").toString());
                        if(userDefault.get("gender").equals(0)) {
                            binding.gender.setText("남자");
                            binding.gender.setTextColor(Color.parseColor("#0000FF")); // 파란색
                        }else {
                            binding.gender.setText("여자");
                            binding.gender.setTextColor(Color.parseColor("#FFC0CB")); // 핑크색
                        }


                    }else if(result.getResultCode() == Activity.RESULT_CANCELED){
                        userDefault = UserTB.getUserData();
                        setDefault(userDefault);
                        Log.d("Profile","그냥 뒤로가처리 후 기본값 설정됨");
                    }
                });
        /** 상세 프로필*/
        }else { // 내 pk가 아니면 상대 프로필
            Log.d("메인에서 넘겨받은 상대 pk: ",PK);
            int PKI = Integer.parseInt(PK);
            UserKey userKey = new UserKey(PKI);
            //상세 프로필일때 화면 구성
            setOtherProfileView();

            // 상대 pk -> 상대 프로필 정보 가져오기 + 화면에 뿌려주기
            getOtherProfile(userKey);
            getOtherRoutine(userKey.getPk()); // 상대방 루틴

            // 화면 전환
            OtherViewChangeBlock();

        }
        cancel_profile();

    }

    public void setOtherProfileView() {
        //내프로필(text+수정하기), 차단목록, 설정 탈퇴 안보이게
        binding.myprofile.setVisibility(View.GONE);
        binding.black.setVisibility(View.GONE);
        binding.set.setVisibility(View.GONE);
        //상세 프로필(text),루틴, 신고내역 오늘 루틴, 위트 보내기 보이게
        binding.Oprofile.setVisibility(View.VISIBLE);
        binding.totalRoutine.setVisibility(View.VISIBLE);
        binding.report.setVisibility(View.VISIBLE);
        binding.rt.setVisibility(View.VISIBLE);
        binding.SendWitt.setVisibility(View.VISIBLE);
        binding.view3.setVisibility(View.VISIBLE);
    }

    private void setRecyclerView(ArrayList<RoutineData> routines) {
        adapter = new RoutineAdapter(this, routines, -1);  // attribute = code가 내 코드면 0, 아니면 -1
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }

    public void getOtherProfile(UserKey userKey) {
        Call<Map<String,Object>> call = apiService.getOtherProfile(userKey);
        call.enqueue(new Callback<Map<String,Object>>() {
            @Override
            public void onResponse(Call<Map<String,Object>> call, Response<Map<String,Object>> response) {
                Map<String,Object> data = response.body();
                OuserDefault = data;
                Log.d("OuserDefault", String.valueOf(data.get("USER_NM")));
                if (data != null) {
                    // 데이터 추출
                    String User_NM = data.get("USER_NM").toString();
                    String gender = data.get("Gender").toString();
                    int height = (int) Double.parseDouble(data.get("User_HT").toString());
                    int weight = (int) Double.parseDouble(data.get("User_WT").toString());
                    int squatValue = (int) Double.parseDouble(data.get("Bench").toString());
                    int benchValue = (int) Double.parseDouble(data.get("Squat").toString());
                    int deadValue = (int) Double.parseDouble(data.get("DeadLift").toString());
                    otherUserKey =  data.get("USER_PK").toString();
                    String GYM_NM = data.get("GYM_NM").toString();

                    Log.d("getOtherProfile 이름:", User_NM);
                    OtherName = User_NM;
                    //받아온 상대 정보 뿌려주기
                    Pname.setText(User_NM);
//                    Pheight.setText(height + "cm");Pweight.setText(weight+ "kg");
//                    Psqaut.setText(String.valueOf(squatValue));Pbench.setText(String.valueOf(benchValue));
                    Pdeadlift.setText(String.valueOf(deadValue));Plocatoin.setText(GYM_NM);
                    if( gender.equals("0.0")) {
                        Pgender.setText("남자");
                        Pgender.setTextColor(Color.parseColor("#0000FF")); // 파란색
                    } else{
                        Pgender.setText("여자");
                        Pgender.setTextColor(Color.parseColor("#FFC0CB")); // 핑크색
                    }

                }else { Log.d("getOtherProfile","프로필 데이터 null");}
            }
            @Override
            public void onFailure(Call<Map<String,Object>> call, Throwable t) {
                Log.d("getOtherProfile","API 요청 실패" + t);
            }
        });
    }

    private void getOtherRoutine(int userKey) {
        apiService.selectRoutine(new GetRoutine(userKey, dayOfWeek)).enqueue(new Callback<List<RoutineData>>() {
            @Override
            public void onResponse(Call<List<RoutineData>> call, Response<List<RoutineData>> response) {
                if (response.isSuccessful()) {
                    Log.d("성공", "루틴 불러오기 성공");

                    ArrayList<RoutineData> routines = (ArrayList<RoutineData>) response.body();
                    for (int i = 0; i < response.body().size(); i++)
                        routines.get(i).setExercises(response.body().get(i).getExercises());

                    setRecyclerView(routines);
                } else {
                    Log.d("실패", "루틴 불러오기 실패");
                }
            }

            @Override
            public void onFailure(Call<List<RoutineData>> call, Throwable t) {
                Log.d("실패", t.getMessage());
            }
        });
    }

    //기본 사용자 정보 세팅
    public void setDefault( Map<String, Object> data ) {
        Pname.setText(data.get("User_NM").toString());//이름
//        Pheight.setText(data.get("height").toString() + "cm");
//        Pweight.setText(data.get("weight").toString() + "kg");
        Psqaut.setText(data.get("squatValue").toString());
        Pbench.setText(data.get("benchValue").toString());
        Pdeadlift.setText(data.get("deadValue").toString());
//        ProfileImg.setImageURI((Uri) data.get("image")); 이미지 처리 미완

        if( data.get("gender").toString().equals("0")) {
            Pgender.setText("남자");
            Pgender.setTextColor(Color.parseColor("#0000FF")); // 파란색
        } else{
            Pgender.setText("여자");
            Pgender.setTextColor(Color.parseColor("#FFC0CB")); // 핑크색
        }


        if(data.get("height").toString().equals("0")){
            binding.height.setVisibility(View.GONE); binding.weight.setVisibility(View.GONE);
            binding.cm.setText("비공개"); binding.kg.setText("비공개");
        }else {
            binding.height.setText(data.get("height").toString() );
            binding.weight.setText(data.get("weight").toString() );
        }


    }

    //화면전환(마이프로필)
    public void ViewChangeBlock() {
        //수정하기
        binding.PEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, MyProfileEdit.class);

                Bundle bundle = new Bundle();
                for (Map.Entry<String, Object> entry : userDefault.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if (value instanceof String) {
                        bundle.putString(key, (String) value);
                    } else if (value instanceof Integer) {
                        bundle.putInt(key, (Integer) value);
                    }
                    // 다른 데이터 타입에 따라 추가적인 처리
                }

                intent.putExtras(bundle);
                editProfileLauncher.launch(intent);
            }
        });

        //TODO version 1 키, 몸무게 수정하기
        binding.EditBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, EditBodyInfo.class);
                intent.putExtra("PK",UserTB.getPK());
                intent.putExtra("height",UserTB.getheight());
                intent.putExtra("weight",UserTB.getweight());
                startActivity(intent);

            }
        });
        //TODO version 1 3대 운동 수정하기
        binding.exercise3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, EditWeightVolumes.class);
                intent.putExtra("PK",UserTB.getPK());
                intent.putExtra("squatValue",UserTB.getsquatValue());
                intent.putExtra("benchValue",UserTB.getbenchValue());
                intent.putExtra("deadValue",UserTB.getdeadValue());
                startActivity(intent);
            }
        });

        //차단하기
        binding.black.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfileActivity.this, BlackActivity.class);
                startActivity(intent);
            }
        });
        //받은 후기
        binding.ReviewsReceived.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, ReviewsRecdAtivity.class);
                intent.putExtra("PK",PK);
                startActivity(intent);
            }
        });
        //위트 내역
        binding.WittHistory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, WittHistoryActivity.class);
                intent.putExtra("PK",PK);
                startActivity(intent);
            }
        });
        /** 설정 */
        binding.set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });



    }
    //화면 전환(상세 프로필)
    public void OtherViewChangeBlock() {
        /** 상대 루틴 */
        binding.totalRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, RoutineActivity.class);
                intent.putExtra("code",Integer.valueOf(PK)); //pk
                intent.putExtra("name",OtherName);//이름
                startActivity(intent);
            }
        });
        /** 받은 후기 */
        binding.ReviewsReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, ReviewsRecdAtivity.class);
                intent.putExtra("PK",PK);
                startActivity(intent);
            }
        });
        /** 위트 내역 */
        binding.WittHistory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, WittHistoryActivity.class);
                intent.putExtra("PK",PK);
                startActivity(intent);
            }
        });
        /** 신고 내역 */
        binding.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, ReportHistoryActivity.class);
                intent.putExtra("PK",PK);
                startActivity(intent);
            }
        });

        /** 위트 보내기 ! */
        binding.WittBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 내 pk, 상대방 pk, 내 이름, 상대방 이름 순서로 보내야함
//                Integer.valueOf(PK); 상대방 pk
//                OuserDefault.get("USER_NM").toString();상대방 이름
//                UserTB.getPK(); 내 pk
//                UserTB.getUser_NM();내 이름
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                wittSendData = new WittSendData(UserTB.getPK(),Integer.valueOf(PK),UserTB.getUser_NM(),OuserDefault.get("USER_NM").toString(),timestamp);
                getWittUserData(wittSendData,timestamp);


            }
        });

    }

    private void getWittUserData(WittSendData wittSendData,String ts) {
        Call<Integer> call = apiService.makeChatRoom(wittSendData);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    Toast.makeText(MyProfileActivity.this, "채팅방이 생성되었습니다!", Toast.LENGTH_SHORT).show();
                    SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();

                    int chatkey = -1;
                    String ts  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                    try {
                        try {
                            sqLiteUtil.setInitView(getApplicationContext(), "CHAT_MSG_TB");
                            chatkey = sqLiteUtil.getLastMyMsgPK(String.valueOf(response.body()), myPK);
                            chatkey = chatkey + 1;
                        } finally {
                            sqLiteUtil.setInitView(getApplicationContext(), "CHAT_MSG_TB");
                            sqLiteUtil.insert(chatkey, Integer.parseInt(myPK), 1, "!%$$#@@$%^!!~" + UserTB.getUser_NM() + "~!!^%$@@#$$%!", response.body(), 0, ts);
                            Log.d("TAG", "chatPk보내기" + chatkey + ts);
                            Log.d("TAG", "chatPk보내기" + otherUserKey);
                            //채팅방 로컬 저장 코드 넣기
                            if (chatkey != -1) {
                                sendMessageToServer("!%$$#@@$%^!!~" + UserTB.getUser_NM() + "~!!^%$@@#$$%!", response.body(), chatkey);

                            }
                        }
                    }
                    finally {
                        finish();
                    }
                } else {
                     Log.d("onResponse","실패");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }

    private void sendMessageToServer(String messageText, int chatRoomPk,int chatPk) {
        try {
            SocketSingleton socketSingleton = SocketSingleton.getInstance(this);
            JSONObject data = new JSONObject();
            data.put("myUserKey", myPK);
            data.put("otherUserKey", PK);
            data.put("chatRoomId", chatRoomPk);
            data.put("messageText", messageText);
            data.put("chatPk",chatPk);
            String ts  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            data.put("TS",ts);
            socketSingleton.sendMessage(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //뒤로가기
    private  void cancel_profile() {
        cancel_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    } //TODO 상세 프로필에 나오도록 xml 변경

    @Override
    protected void onRestart() {
        super.onRestart();
        //수정하고 뒤로갔을때 다시 보여줄 데이터
        if(UserTB.getheight() == 0){
            binding.height.setVisibility(View.GONE); binding.weight.setVisibility(View.GONE);
            binding.cm.setText("비공개"); binding.kg.setText("비공개");
        }else {
            binding.height.setText(String.valueOf(UserTB.getheight()) );
            binding.weight.setText(String.valueOf(UserTB.getweight()) );
        }

    }
    //새로 추가함
    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }
    //받은 후기
//    public void onClickReviewReceived(View view) {
//        Intent intent = new Intent(MyProfileActivity.this, ReviewsRecdAtivity.class);
//        intent.putExtra("PK",PK);
//        startActivity(intent);
//    }
//    //위트 내역
//    public void onClickWittHistory(View view) {
//        Intent intent = new Intent(MyProfileActivity.this, WittHistoryActivity.class);
//        intent.putExtra("PK",PK);
//        startActivity(intent);
//    }
//    //차단목록
//    public void onClickBlackList(View view) {
//        Intent intent = new Intent(MyProfileActivity.this, BlackActivity.class);
//        startActivity(intent);
//    }
//    //설정하기
//    public void onClickSetting(View view) {
//        Intent intent = new Intent(MyProfileActivity.this,SettingActivity.class);
//        startActivity(intent);
//    }
//    //상대 루틴
//    public void onClickRoutine(View view) {
//        Intent intent = new Intent(MyProfileActivity.this, RoutineActivity.class);
//        intent.putExtra("code",Integer.valueOf(PK)); //pk
//        intent.putExtra("name",OtherName);//이름
//        startActivity(intent);
//    }
//    //신고 내역
//    public void onClickReport(View view) {
//        Intent intent = new Intent(MyProfileActivity.this, ReportHistoryActivity.class);
//        intent.putExtra("PK",PK);
//        startActivity(intent);
//    }
//
//
//    //내 정보 수정
//    public void onClickProfileEdit(View view) {
//        Intent intent = new Intent(MyProfileActivity.this, MyProfileEdit.class);
//
//        Bundle bundle = new Bundle();
//        for (Map.Entry<String, Object> entry : userDefault.entrySet()) {
//            String key = entry.getKey();
//            Object value = entry.getValue();
//
//            if (value instanceof String) {
//                bundle.putString(key, (String) value);
//            } else if (value instanceof Integer) {
//                bundle.putInt(key, (Integer) value);
//            }
//            // 다른 데이터 타입에 따라 추가적인 처리
//        }
//
//        intent.putExtras(bundle);
//        editProfileLauncher.launch(intent);
//    }
//    //위트 보내기
//    public void onClickSendWitt(View view) {
//        Log.d("onClickSendWitt","위트보내기 눌림");
//        wittSendData = new WittSendData(UserTB.getPK(),Integer.valueOf(PK),UserTB.getUser_NM(),OuserDefault.get("USER_NM").toString());
//        getWittUserData(wittSendData);
//    }
}

