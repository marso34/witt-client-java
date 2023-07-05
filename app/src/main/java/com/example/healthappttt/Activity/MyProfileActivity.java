package com.example.healthappttt.Activity;

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
import com.example.healthappttt.Data.UserClass;
import com.example.healthappttt.R;

import java.util.HashMap;
import java.util.Map;


public class MyProfileActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> editProfileLauncher;
    private static String name_TB = "membership";
    private PreferenceHelper prefhelper;
    private UserClass userClass;
    private SharedPreferences shared_pref;
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
        //기본 텍스트 세팅
        //prefhelper = new PreferenceHelper(name_TB);
        //userDefualt = prefhelper.getUserData(shared_pref);

        //testcase
        userDefault = new HashMap<>();
        userDefault.put("name","이형원");
        userDefault.put("gender",0);
        userDefault.put("height", 175);
        userDefault.put("weight", 87);
        userDefault.put("squatValue", 80);
        userDefault.put("benchValue", 90);
        userDefault.put("deadValue", 100);



        //버튼
        block_btn = findViewById(R.id.block_btn);
        Reviews_btn = findViewById(R.id.Reviews_Recd);
        WittHistory_btn = findViewById(R.id.WittHistory);
        PEdit = findViewById(R.id.PEdit);
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

        setDefault(userDefault);

        ViewChangeBlock(); // 화면전환 매서드
        // MyprofileEdit에서 넘어온 데이터 처리
        editProfileLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if (data != null) {
                    // 결과 데이터를 처리 - Bundle extras = getIntent().getExtras();
                    String editedName = data.getStringExtra("editedName");
                    int editedHeight = data.getIntExtra("editedHeight", 0);
                    // 결과 데이터 바인딩 해서 보여주기
                }else { Log.d("editProfileLauncher","Data 값 존재 X"); }

            }else { Log.d("result.getResultCode","결과값 코드가 맞지 않음"); }
        });





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
            gender.setBackgroundColor(Color.parseColor("#0000FF")); // 파란색
        } else{
            gender.setText("여자");
            gender.setBackgroundColor(Color.parseColor("#FFC0CB")); // 핑크색
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

