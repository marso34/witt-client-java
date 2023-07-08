package com.example.healthappttt.Profile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private PreferenceHelper prefhelper;
    private SharedPreferences sharedPreferences;
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
        prefhelper = new PreferenceHelper("UserTB",this);
        Intent intent = getIntent();//넘겨받은 pk를 담은 번들
        if(intent.getStringExtra("myPK").equals( String.valueOf(prefhelper.getPK()) )){ //내 pk이면 마이 프로필

        Log.d("프로필에서 membership으로 받기O",String.valueOf(prefhelper.getPK()));
        Log.d("프로필에서 membership으로 받기O",intent.getStringExtra("myPK"));

        }else{ // 내 pk가 아니면 상대 프로필
            Log.d("프로필에서 membership으로 받기X",String.valueOf(prefhelper.getPK()));
            Log.d("프로필에서 membership으로 받기X",intent.getStringExtra("myPK"));
        }

        //기본 텍스트 세팅 TODO 이걸로 추후에 바꿔서 연결해야함
        //prefhelper = new PreferenceHelper(name_TB);
        //userDefualt = prefhelper.getUserData(shared_pref);
        prefhelper = new PreferenceHelper("membership",this);
        Log.d("membership", String.valueOf(prefhelper.getUserData().get("name")));
        // TODO 테스트케이스 -> 진짜로 받아온 데이터를 userDefault에 넣어 놓아야함
        // TODO 단, putUserDefault로 따로 저장 X (테스트하려고 했기때문)
        // TODO 수정하기 버튼 눌렀을때 참조 로컬 이름도 membership으로 바꿔야함
        // TODO 수정하기 플로우 다시 한번 확인 해야할듯

        //testcase----------------------//
        userDefault = new HashMap<>();
        userDefault.put("name","이형원");
        userDefault.put("gender",0);
        userDefault.put("height", 175);
        userDefault.put("weight", 87);
        userDefault.put("squatValue", 80);
        userDefault.put("benchValue", 90);
        userDefault.put("deadValue", 100);
        userDefault.put("totalValue",270);

        prefhelper.putUserDefault(userDefault);
        //----------------------testcase//

        //버튼
        block_btn = findViewById(R.id.block_btn);

        PEdit = findViewById(R.id.PEdit);


        setDefault(userDefault);

        ViewChangeBlock(); // 화면전환 매서드

        // TODO MyprofileEdit에서 넘어온 데이터 처리
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
                        userDefault = prefhelper.getUserData();

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
                        userDefault = prefhelper.getUserData();
                        setDefault(userDefault);
                        Log.d("Profile","그냥 뒤로가처리 후 기본값 설정됨");
                    }
                });



    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            if (data != null) {
//                Bundle bundle = data.getExtras();
//                if (bundle != null) {
//                    String result = bundle.getString("result");
//                    // 결과 데이터를 사용하여 필요한 작업 수행
//                }
//            }
//        }
//    }


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
                    // 다른 데이터 타입에 따라 추가적인 처리를 할 수 있습니다.
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

