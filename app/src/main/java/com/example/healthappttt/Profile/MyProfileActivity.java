package com.example.healthappttt.Profile;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.healthappttt.Data.User.ImageCacheManager;
import com.example.healthappttt.Data.User.UserKey;
import com.example.healthappttt.Data.WittSendData;
import com.example.healthappttt.R;
import com.example.healthappttt.Routine.RoutineActivity;
import com.example.healthappttt.Routine.RoutineAdapter;
import com.example.healthappttt.databinding.ActivityMyprofileBinding;
import com.example.healthappttt.interface_.ServiceApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyProfileActivity extends AppCompatActivity {


    private ActivityMyprofileBinding binding;
    private PreferenceHelper UserTB;
    private SQLiteUtil sqLiteUtil;
    private ServiceApi apiService;
    private RoutineAdapter adapter;
    private WittSendData wittSendData;

    Button  cancel_Mprofile,cancel_Oprofile;
    ImageView ProfileImg;
    TextView Pname,Pgender,Plocatoin;
    TextView Psqaut,Pbench,Pdeadlift;
    Map<String,Object> userDefault;
    Map<String,Object> OuserDefault;
    int myPK,PK;
    String MyName, OtherName;
    String MyGym;
    int dayOfWeek;

    private Bitmap downloadedBitmap;
    private String imageUri;


    int[] imageResources = {R.drawable.man_3d_light, R.drawable.woman_3d_light};
    String otherUserKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        apiService = RetrofitClient.getClient().create(ServiceApi.class); // create메서드로 api서비스 인터페이스의 구현제 생성
        binding = ActivityMyprofileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cancel_Mprofile = findViewById(R.id.cancel_Mprofile);
        //텍스트

        Pname = findViewById(R.id.name);
        Pgender = findViewById(R.id.gender);
        Psqaut = findViewById(R.id.Psqaut);
        Pbench = findViewById(R.id.Pbench);
        Pdeadlift = findViewById(R.id.Pdeadlift);
        //이미지,헬스장
        ProfileImg = findViewById(R.id.ProfileImg);
        Plocatoin = findViewById(R.id.MyLocation);
//-------------------------------------------------------------------------------------
        UserTB = new PreferenceHelper("UserTB",this);

        Intent intent = getIntent();//넘겨받은 pk를 담은 번들
        PK = intent.getIntExtra("PK",0);

//        String uri = UserTB.getUser_IMG(); //이미지 가져오는 부분
//        if(uri != null)
//        {
//            String fileName = uri.substring(uri.lastIndexOf("/") + 1);
//            getImage(fileName);
//        }

        String fileName = "your_file_name.jpg"; // 저장된 이미지 파일 이름을 입력하세요.
        downloadedBitmap = ImageCacheManager.getInstance().getDownloadedBitmap();;
        if (downloadedBitmap != null) {
            // 저장된 이미지가 있을 경우 해당 이미지를 사용
            Log.d(TAG, "이미지는 캐시된 이미지를 사용 중입니다.");
            ProfileImg.setImageBitmap(downloadedBitmap);
            ProfileImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 클릭 시 이미지를 팝업 창으로 전체 화면에 띄웁니다.
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) ProfileImg.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    showFullScreenImagePopup(bitmap);
                }
            });
        } else {
            // 저장된 이미지가 없을 경우 서버에서 이미지 다운로드
            Log.d(TAG, "이미지는 서버에서 다운로드한 이미지를 사용 중입니다.");
            String uri = UserTB.getUser_IMG(); // 이미지 가져오는 부분
            if (uri != null) {
                String fileName1 = uri.substring(uri.lastIndexOf("/") + 1);
                getImage(fileName1);
            }
        }



        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        dayOfWeek = intent.getIntExtra("dayOfWeek",calendar.get(Calendar.DAY_OF_WEEK) - 1);

        myPK = UserTB.getPK();// 로컬 내 PK
        /** 마이 프로필 */
        if(PK == myPK ){ // 내 pk이면 마이 프로필
            Log.d("프로필에서 로컬pk와 넘겨받은pk",PK + " " + myPK);

            userDefault = new HashMap<>();// 회원가입시 입력했던 데이터들 로컬에서 받기
            userDefault = UserTB.getUserData();
            //userDefault.put("totalValue", UserTB.getUserData().get("totalValue"));

            setDefault(userDefault); //로컬 데이터를를 화면에 세팅
            MyName = Pname.getText().toString();
            MyGym = Plocatoin.getText().toString();

            // 화면 전환
            ViewChangeBlock();
            CommonViewChangeBlock();
        /** 상세 프로필*/
        }else { // 내 pk가 아니면 상대 프로필
            Log.d("메인에서 넘겨받은 상대 pk: ",PK+"");
            UserKey userKey = new UserKey(PK);
            //상세 프로필일때 화면 구성
            setOtherProfileView();

            // 상대 pk -> 상대 프로필 정보 가져오기 + 화면에 뿌려주기
            getOtherProfile(userKey);
            getOtherRoutine(userKey.getPk()); // 상대방 루틴

            // 화면 전환
            OtherViewChangeBlock();
            CommonViewChangeBlock();

        }

    }

    public void setOtherProfileView() {
        //내프로필(text+수정하기), 차단목록, 설정 탈퇴 안보이게
        binding.infoName.setText("상세 프로필");
        binding.black.setVisibility(View.GONE);
        binding.set.setVisibility(View.GONE);
        //상세 프로필(text),루틴, 신고내역 오늘 루틴
        binding.totalRoutine.setVisibility(View.VISIBLE);
        binding.report.setVisibility(View.VISIBLE);
        binding.rt.setVisibility(View.VISIBLE);

        //위트를 이미 보낸 사람일 경우
        sqLiteUtil = SQLiteUtil.getInstance();
        sqLiteUtil.setInitView(this, "CHAT_ROOM_TB");//리뷰 목록 로컬 db
        if(!sqLiteUtil.Wittsended(PK)){
            binding.SendWitt.setVisibility(View.VISIBLE);
            binding.view3.setVisibility(View.VISIBLE);
        }
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
                    int temp = (int) Double.parseDouble(data.get("Is_Public").toString());
                    int squatValue = (int) Double.parseDouble(data.get("Bench").toString());
                    int benchValue = (int) Double.parseDouble(data.get("Squat").toString());
                    int deadValue = (int) Double.parseDouble(data.get("DeadLift").toString());
                    otherUserKey =  data.get("USER_PK").toString();
                    String GYM_NM = data.get("GYM_NM").toString();

                    Log.d("getOtherProfile 이름:", User_NM);
                    OtherName = User_NM;
                    //받아온 상대 정보 뿌려주기
                    Pname.setText(User_NM);

                    Psqaut.setText(String.valueOf(squatValue));
                    Pbench.setText(String.valueOf(benchValue));
                    Pdeadlift.setText(String.valueOf(deadValue));

                    if(GYM_NM.equals("")){
                        Plocatoin.setText("선택된 헬스장이 없어요");
                        Plocatoin.setTextColor(Color.parseColor("#D1D8E2"));
                        binding.MapImg.setBackgroundColor(Color.parseColor("#D1D8E2"));
                    }else{
                        Plocatoin.setText(GYM_NM);
                    }


                    if( gender.equals("0.0")) {
                        Pgender.setText("남자");
                        Pgender.setTextColor(Color.parseColor("#579EF2")); // 파란색
                    } else{
                        Pgender.setText("여자");
                        Pgender.setTextColor(Color.parseColor("#F257AF")); // 핑크색
                        Pgender.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FDE6F3")));
                    }

                    if(temp == 0){
                        binding.height.setVisibility(View.GONE); binding.weight.setVisibility(View.GONE);
                        binding.cm.setText("비공개"); binding.kg.setText("비공개");
                    }else {
                        binding.height.setText(String.valueOf(height));
                        binding.weight.setText(String.valueOf(weight));
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

        Log.d(TAG,data.get("User_IMG").toString());
        Plocatoin.setText(data.get("gymNm").toString());//헬스장
        Psqaut.setText(data.get("squatValue").toString());
        Pbench.setText(data.get("benchValue").toString());
        Pdeadlift.setText(data.get("deadValue").toString());
//        ProfileImg.setImageURI((Uri) data.get("image")); 이미지 처리 미완

        if( data.get("gender").toString().equals("0")) {
            Pgender.setText("남자");
            Pgender.setTextColor(Color.parseColor("#579EF2")); // 파란색
        } else{
            Pgender.setText("여자");
            Pgender.setTextColor(Color.parseColor("#F257AF")); // 핑크색
            Pgender.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FDE6F3")));
        }

        if(data.get("temp").toString().equals("0")){
            binding.height.setVisibility(View.GONE); binding.weight.setVisibility(View.GONE);
            binding.cm.setText("비공개"); binding.kg.setText("비공개");
        }else {
            binding.height.setText(data.get("height").toString() );
            binding.weight.setText(data.get("weight").toString() );
        }

    }

    //화면전환(마이프로필)
    public void ViewChangeBlock() {
        //헬스장 수정
        binding.GYM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this,EditGymActivity.class);
                intent.putExtra("MyName",Pname.getText());
                intent.putExtra("MyGym",Plocatoin.getText());
                intent.putExtra("GymAdress",UserTB.getGYMAdress());
                startActivity(intent);
                Log.d("실행순서: ","myprofileActivity->EditActivity->EditFragment");
            }
        });

        //version 1 키, 몸무게 수정
        binding.EditBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, EditBodyInfo.class);
                intent.putExtra("PK",myPK);
                intent.putExtra("height",UserTB.getheight());
                intent.putExtra("weight",UserTB.getweight());
                intent.putExtra("temp",UserTB.gettemp());
                startActivity(intent);

            }
        });
        //version 1 3대 운동 수정
        binding.exercise3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, EditWeightVolumes.class);
                intent.putExtra("PK",myPK);
                intent.putExtra("squatValue",UserTB.getsquatValue());
                intent.putExtra("benchValue",UserTB.getbenchValue());
                intent.putExtra("deadValue",UserTB.getdeadValue());
                startActivity(intent);
            }
        });

        //차단목록
        binding.black.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfileActivity.this, BlackActivity.class);
                startActivity(intent);
            }
        });

        //설정
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
                intent.putExtra("code",PK); //pk
                intent.putExtra("name",OtherName);//이름
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

                wittSendData = new WittSendData(UserTB.getPK(),PK,UserTB.getUser_NM(),OuserDefault.get("USER_NM").toString(),timestamp);
                getWittUserData(wittSendData,timestamp);


            }
        });

    }
    //공통 목록
    public void CommonViewChangeBlock() {
        /** 받은 평가 */
        binding.evaluated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this,EvaluationRecdActivity.class);
                intent.putExtra("PK",PK);
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

        //뒤로가기
        binding.cancelMprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                            chatkey = sqLiteUtil.getLastMyMsgPK(String.valueOf(response.body()), String.valueOf(myPK));
                            chatkey = chatkey + 1;
                        } finally {
                            sqLiteUtil.setInitView(getApplicationContext(), "CHAT_MSG_TB");
                            sqLiteUtil.insert(chatkey, myPK, 1, "!%$$#@@$%^!!~" + UserTB.getUser_NM() + "~!!^%$@@#$$%!", response.body(), 0, ts);
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
    private void getImage(String imageUri) {

        if (downloadedBitmap == null) {
            this.imageUri = imageUri;

            ServiceApi apiService = RetrofitClient.getClient().create(ServiceApi.class);

            Call<ResponseBody> call = apiService.getImage(imageUri);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "이미지 다운 성공");
                        downloadedBitmap = BitmapFactory.decodeStream(response.body().byteStream());
                        ProfileImg.setImageBitmap(downloadedBitmap);

                        // 이미지를 로컬에 저장하기 위한 권한 요청
                        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                        ImageCacheManager.getInstance().setDownloadedBitmap(downloadedBitmap);

                        ProfileImg.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 클릭 시 이미지를 팝업 창으로 전체 화면에 띄웁니다.
                                BitmapDrawable bitmapDrawable = (BitmapDrawable) ProfileImg.getDrawable();
                                Bitmap bitmap = bitmapDrawable.getBitmap();
                                showFullScreenImagePopup(bitmap);
                            }
                        });
                    } else {
                        Log.d(TAG, "이미지 다운 실패");
                        int randomIndex = new Random().nextInt(imageResources.length);
                        ProfileImg.setImageResource(imageResources[randomIndex]);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d(TAG, "이미지 다운 에러");
                    int randomIndex = new Random().nextInt(imageResources.length);
                    ProfileImg.setImageResource(imageResources[randomIndex]);
                }
            });
        }
        else {
            // 이미지가 이미 다운로드된 경우에는 저장된 이미지를 사용
            ProfileImg.setImageBitmap(downloadedBitmap);

            ProfileImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 클릭 시 이미지를 팝업 창으로 전체 화면에 띄웁니다.
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) ProfileImg.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    showFullScreenImagePopup(bitmap);
                }
            });
        }
    }

    private void sendMessageToServer(String messageText, int chatRoomPk,int chatPk) {
        try {
            SocketSingleton socketSingleton = SocketSingleton.getInstance(this);
            JSONObject data = new JSONObject();
            data.put("myUserKey", String.valueOf(myPK));
            data.put("otherUserKey", String.valueOf(PK));
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


    @Override
    protected void onResume() {
        super.onResume();
        //수정하고 뒤로갔을때 다시 보여줄 데이터
        if (PK == myPK) {
            //키, 몸무게
            if (UserTB.gettemp() == 0) {
                binding.cm.setVisibility(View.VISIBLE);
                binding.kg.setVisibility(View.VISIBLE);
                binding.height.setVisibility(View.GONE);
                binding.weight.setVisibility(View.GONE);
                binding.cm.setText("비공개");
                binding.kg.setText("비공개");
                Log.d("onResume():","마이프로필 비공개");
                Log.d("temp: ", String.valueOf(UserTB.gettemp()));
            } else {
                binding.cm.setVisibility(View.GONE);
                binding.kg.setVisibility(View.GONE);
                binding.height.setVisibility(View.VISIBLE);
                binding.weight.setVisibility(View.VISIBLE);
                binding.height.setText(String.valueOf(UserTB.getheight()));
                binding.weight.setText(String.valueOf(UserTB.getweight()));
                Log.d("onResume():","마이프로필 공개");
                Log.d("temp: ", String.valueOf(UserTB.gettemp()));
            }
            //3대
            binding.Psqaut.setText(String.valueOf(UserTB.getsquatValue()));
            binding.Pbench.setText(String.valueOf(UserTB.getbenchValue()));
            binding.Pdeadlift.setText(String.valueOf(UserTB.getdeadValue()));
            //헬스장
            String GYMNM = UserTB.getGYMNM();
            if(GYMNM.equals("")){
                binding.MyLocation.setText("선택된 헬스장이 없어요");
                binding.MyLocation.setTextColor(Color.parseColor("#D1D8E2"));
                binding.MapImg.setImageTintList(getResources().getColorStateList(R.color.Background_2));
            }else{
                binding.MyLocation.setText(GYMNM);
            }
        }

    }


    //새로 추가함
    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    private void showFullScreenImagePopup(Bitmap bitmap) {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.popup_image_layout);

        ImageView fullScreenImageView = dialog.findViewById(R.id.fullScreenImageView);
        fullScreenImageView.setImageBitmap(bitmap);

        ImageView backButton = dialog.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // 뒤로가기 버튼 클릭 시 팝업 창을 닫습니다.
            }
        });

        dialog.show();
    }
    private void saveImageToInternalStorage() {
        if (downloadedBitmap != null && imageUri != null) {
            String fileName = imageUri; // 저장할 이미지 파일 이름을 입력하세요.
            try {
                FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
                downloadedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.close();

                ImageCacheManager.getInstance().setDownloadedBitmap(downloadedBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // 권한이 허용된 경우 이미지 저장 처리
                    saveImageToInternalStorage();
                } else {
                    // 권한이 거부된 경우 처리할 로직 작성
                    // 예를 들면, 사용자에게 권한이 필요하다는 안내 메시지를 보여줄 수 있습니다.
                    Toast.makeText(this, "이미지 저장 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
    );
}

