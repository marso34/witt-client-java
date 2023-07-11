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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.User.UserClass;
import com.example.healthappttt.R;
import com.example.healthappttt.databinding.ActivityMyprofileBinding;

import java.util.HashMap;
import java.util.Map;


public class MyProfileActivity extends AppCompatActivity {

    private ActivityMyprofileBinding binding;
    private ActivityResultLauncher<Intent> editProfileLauncher;
    private static final String name_TB = "membership";
    private PreferenceHelper membership;
    private PreferenceHelper UserTB;
    private UserClass userClass;
    ImageButton block_btn,Reviews_btn,WittHistory_btn;
    Button PEdit;
    ImageView ProfileImg;
    TextView name,gender,Pheight,Pweight;
    TextView Psqaut,Pbench,Pdeadlift;
    Map<String, Object> userDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        binding = ActivityMyprofileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //prefhelper = new PreferenceHelper("default_user_info",this);

        Reviews_btn = findViewById(R.id.Reviews_Recd);
        WittHistory_btn = findViewById(R.id.WittHistory);
        //텍스트
        name = findViewById(R.id.name);
        gender = findViewById(R.id.gender);
        Pheight = findViewById(R.id.Pheight);
        Pweight = findViewById(R.id.Pweight);
        Psqaut = findViewById(R.id.Psqaut);
        Pbench = findViewById(R.id.Pbench);
        Pdeadlift = findViewById(R.id.Pdeadlift);
        //이미지
        ProfileImg = findViewById(R.id.ProfileImg);
//-------------------------------------------------------------------------------------
        UserTB = new PreferenceHelper("UserTB",this);
        membership = new PreferenceHelper("membership",this);

        Intent intent = getIntent();//넘겨받은 pk를 담은 번들
        String PK = intent.getStringExtra("PK");//넘겨 받은 PK
        String myPK = String.valueOf(UserTB.getPK());// 로컬 내 PK
        /** 마이 프로필*/
        if(PK.equals(myPK) ){ // 내 pk이면 마이 프로필
            Log.d("프로필에서 로컬pk와 넘겨받은pk",PK + " " + myPK);

            block_btn = findViewById(R.id.block_btn); //내 프로필에만  존재
            PEdit = findViewById(R.id.PEdit);//내 프로필에만 존재

            userDefault = new HashMap<>();// 회원가입시 입력했던 데이터들 로컬에서 받기
            userDefault.put("name",membership.getUserData().get("name"));
            userDefault.put("gender",membership.getUserData().get("gender"));
            userDefault.put("height", membership.getUserData().get("height"));
            userDefault.put("weight", membership.getUserData().get("weight"));
            userDefault.put("squatValue",membership.getUserData().get("squatValue"));
            userDefault.put("benchValue", membership.getUserData().get("benchValue"));
            userDefault.put("deadValue", membership.getUserData().get("deadValue"));
            userDefault.put("totalValue",membership.getUserData().get("totalValue"));


            setDefault(userDefault); //로컬 데이터를를 화면에 세팅

            ViewChangeBlock(); // 화면전환 매서드

            // MyprofileEdit에서 넘어온 데이터 처리
            editProfileLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        // result 에는 resultCode 가 있다.
                        // resultCode 의 값으로, 여러가지 구분해서 사용이 가능.
                        if (result.getResultCode() == RESULT_OK){
//                        String Ename = result.getData().getStringExtra("name");
//                        int Eheight = result.getData().getIntExtra("height",0);
//                        int Eweight = result.getData().getIntExtra("weight",0);
//                        int EsquatValue = result.getData().getIntExtra("squatValue",0);
//                        int EbenchValue = result.getData().getIntExtra("benchValue",0);
//                        int EdeadValue = result.getData().getIntExtra("deadValue",0);
//                        int Egender = result.getData().getIntExtra("gender",0);
                            userDefault = membership.getUserData();// edit에서 저장되고 돌아온 후 로컬에서 다시 데이터 불러옴

                            //화면에 연결
                            binding.name.setText(userDefault.get("name").toString());
                            binding.Pheight.setText(userDefault.get("height").toString() + "cm");
                            binding.Pweight.setText(userDefault.get("weight").toString() + "kg");
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
                            userDefault = membership.getUserData();
                            setDefault(userDefault);
                            Log.d("Profile","그냥 뒤로가처리 후 기본값 설정됨");
                        }
                    });
        /** 상세 프로필*/
        }else { // 내 pk가 아니면 상대 프로필
            Log.d("메인에서 넘겨받은 pk: ",PK);

            //상세 프로필일때 화면 구성
            setOtherProfileView();

            //TODO 상대 프로필 정보 가져오기 + 화면에 뿌려주기

        }

        //testcase----------------------//
//        userDefault = new HashMap<>();
//        userDefault.put("name","이형원");
//        userDefault.put("gender",0);
//        userDefault.put("height", 175);
//        userDefault.put("weight", 87);
//        userDefault.put("squatValue", 80);
//        userDefault.put("benchValue", 90);
//        userDefault.put("deadValue", 100);
//        userDefault.put("totalValue",270);
//
//        membership.putUserDefault(userDefault);
        //----------------------testcase//

        //버튼



    }

    public void setOtherProfileView() {
        //내프로필(text+수정하기), 차단목록, 설정 탈퇴 안보이게
        binding.myprofile.setVisibility(View.INVISIBLE);
        binding.black.setVisibility(View.INVISIBLE);
        binding.set.setVisibility(View.INVISIBLE);
        binding.secession.setVisibility(View.INVISIBLE);
        //상세 프로필(text),루틴, 신고내역 오늘 루틴, 위트 보내기 보이게
        binding.Oprofile.setVisibility(View.VISIBLE);
        binding.routineTb.setVisibility(View.VISIBLE);
        binding.report.setVisibility(View.VISIBLE);
        binding.todayRoutine.setVisibility(View.VISIBLE);
        binding.SendWitt.setVisibility(View.VISIBLE);
    }

    //기본 사용자 정보 세팅
    public void setDefault( Map<String, Object> data ) {
        name.setText(data.get("name").toString());//이름
        Pheight.setText(data.get("height").toString() + "cm");
        Pweight.setText(data.get("weight").toString()+ "kg");
        Psqaut.setText(data.get("squatValue").toString());
        Pbench.setText(data.get("benchValue").toString());
        Pdeadlift.setText(data.get("deadValue").toString());
//        ProfileImg.setImageURI((Uri) data.get("image")); 이미지 처리 미완

        if( data.get("gender").toString().equals("0")) {
            gender.setText("남자");
            gender.setTextColor(Color.parseColor("#0000FF")); // 파란색
        } else{
            gender.setText("여자");
            gender.setTextColor(Color.parseColor("#FFC0CB")); // 핑크색
        }

    }

    //화면전환
    public void ViewChangeBlock() {
        //수정하기
        PEdit.setOnClickListener(new View.OnClickListener() {
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

        //차단하기
        block_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfileActivity.this, BlackActivity.class);
                startActivity(intent);
            }
        });
        //받은 후기
        Reviews_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, ReviewsRecdAtivity.class);
                startActivity(intent);
            }
        });
        //위트 내역
        WittHistory_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, WittHistoryActivity.class);
                startActivity(intent);
            }
        });

    }



}

