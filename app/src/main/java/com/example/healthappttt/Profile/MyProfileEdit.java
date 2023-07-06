package com.example.healthappttt.Profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.R;
import com.example.healthappttt.databinding.ActivityMyProfileEditBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MyProfileEdit extends AppCompatActivity {

    private ActivityMyProfileEditBinding binding;
    private PreferenceHelper prefhelper;
    ImageView edit_img;
    ImageButton elbum;
    Map<String, Object> Editeduserdata;
    String name;
    int height,weight,gender;
    int squatValue,benchValue,deadValue;
    AtomicInteger squatValue1,benchValue1,deadValue1,WeightSum1;
    Map<String, Object> UpdateDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_edit);

        binding = ActivityMyProfileEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefhelper = new PreferenceHelper("default_user_info",this);
        edit_img = findViewById(R.id.edit_img); // 내 프로필 사진
        elbum = findViewById(R.id.elbum);// 사진첩
        //3대 측정, 스쿼트, 벤치, 데드

        //초기값 넣어주기 이름, 키, 몸무게 / 성별 / 이미지 / 3대 운동량
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            height = extras.getInt("height");
            weight = extras.getInt("weight");
            gender = extras.getInt("gender");
            squatValue = extras.getInt("squatValue");
            benchValue = extras.getInt("benchValue");
            deadValue = extras.getInt("deadValue");
            }
        //AtomicInteger 변수 초기화
        squatValue1 = new AtomicInteger(squatValue);
        benchValue1 = new AtomicInteger(benchValue);
        deadValue1 = new AtomicInteger(deadValue);
        WeightSum1 = new AtomicInteger(squatValue+benchValue+deadValue);
        Log.d("MyProfileEdit 스쿼트: ", String.valueOf(squatValue1));
        Log.d("MyProfileEdit 벤치: ", String.valueOf(benchValue1));
        Log.d("MyProfileEdit 데드: ", String.valueOf(deadValue1));
        // 받은 데이터를 해당 TextView에 설정
        binding.Ename.setText(name);
        binding.Eheight.setText(String.valueOf(height));
        binding.Eweight.setText(String.valueOf(weight));
        binding.SquatText.setText(String.valueOf(squatValue));
        binding.BenchText.setText(String.valueOf(benchValue));
        binding.DeadText.setText(String.valueOf(deadValue));
        binding.Total.setText(squatValue + benchValue + deadValue + "kg");
        if(gender == 0) {
            binding.gender.setText("남자");
            binding.gender.setBackgroundColor(Color.parseColor("#0000FF")); // 파란색
        }else{
            binding.gender.setText("여자");
            binding.gender.setBackgroundColor(Color.parseColor("#FFC0CB")); // 핑크색
        }
        //앨범 버튼 onclick -> 내 앨범 가져오기

        //성별 바꾸기
        gender_click(binding.gender,gender);
        //3대 운동 kg up and down button
        setWeightUpdateListener(binding.SquatUpBtn, binding.SquatDownBtn, binding.SquatText, squatValue1);
        setWeightUpdateListener(binding.BenchUpBtn, binding.BenchDownBtn, binding.BenchText, benchValue1);
        setWeightUpdateListener(binding.DeadUpBtn,binding.DeadDownBtn,binding.DeadText, deadValue1);
        //수정하기 버튼 누르기
        UpdateMyProfile();

    }
    //수정하기 버튼
    public void UpdateMyProfile() {
        binding.move3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                /**
                 * 로컬 저장하는 로직 + 내 프로필로 돌아갔을 때 업데이트되어 보여짐
                 */
                UpdateDefault = new HashMap<>();
                UpdateDefault.put("name",binding.Ename.getText().toString());
                UpdateDefault.put("height", Integer.valueOf(String.valueOf(binding.Eheight.getText())));
                UpdateDefault.put("weight", Integer.valueOf(String.valueOf(binding.Eweight.getText())));
                UpdateDefault.put("squatValue", squatValue1.get());
                UpdateDefault.put("benchValue", benchValue1.get());
                UpdateDefault.put("deadValue", deadValue1.get());
                if(binding.gender.getText().equals("남자")) {
                    UpdateDefault.put("gender",0);
                }else{
                    UpdateDefault.put("gender",1);
                }

                prefhelper.putUserDefault(UpdateDefault);

//                intent.putExtra("name",binding.Ename.getText().toString());
//                intent.putExtra("height",Integer.valueOf(String.valueOf(binding.Eheight.getText())) );
//                intent.putExtra("weight",Integer.valueOf(String.valueOf(binding.Eweight.getText())) );
//                intent.putExtra("squatValue",squatValue1.get() );
//                intent.putExtra("benchValue",benchValue1.get() );
//                intent.putExtra("deadValue",deadValue1.get() );
//                if(binding.gender.getText().equals("남자")) {
//                    intent.putExtra("gender",0);
//                }else{
//                    intent.putExtra("gender",1);
//                }

                setResult(RESULT_OK, intent);//응답코드 -1
                finish();//스택에서 제거
            }
        });
    }

    public void setWeightUpdateListener(Button upButton, Button downButton,TextView TextValue,AtomicInteger weightValue ) {
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextValue.setText(String.valueOf(weightValue.addAndGet(5)));
                binding.Total.setText(String.valueOf(WeightSum1.addAndGet(5)));
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextValue.setText(String.valueOf(weightValue.addAndGet(-5)));
                binding.Total.setText(String.valueOf(WeightSum1.addAndGet(-5)));
            }
        });
    }

    public void gender_click(Button gender_btn, int n) {
        gender_btn.setOnClickListener(new View.OnClickListener() {
            int clickState = n;
            @Override
            public void onClick(View v){
                if (clickState == 1) {
                    // 버튼이 클릭된 상태일 때
                    gender_btn.setTextColor(Color.parseColor("#FFC0CB"));// 핑크색
                    gender_btn.setText("여자");
                    gender = 1;
                    clickState = 0;
                } else {
                    // 버튼이 클릭되지 않은 상태일 때
                    gender_btn.setTextColor(Color.parseColor("#0000FF"));// 파란색
                    gender_btn.setText("남자");
                    gender = 0;
                    clickState = 1;
                }
            }
        });
    }

    //설정하기
//    public void set_btn(View view) {
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                /**
//                 * 로컬 저장하는 로직 + 내 프로필로 돌아갔을 때 업데이트되어 보여짐
//                 */
//
//
//
//                intent.putExtra("name",binding.Ename.getText().toString());
//                intent.putExtra("height",Integer.valueOf(String.valueOf(binding.Eheight.getText())) );
//                intent.putExtra("weight",Integer.valueOf(String.valueOf(binding.Eweight.getText())) );
//                intent.putExtra("squatValue",squatValue1.get() );
//                intent.putExtra("benchValue",benchValue1.get() );
//                intent.putExtra("deadValue",deadValue1.get() );
//                if(binding.gender.getText().equals("남자")) {
//                    intent.putExtra("gender",0);
//                }else{
//                    intent.putExtra("gender",1);
//                }
//
//                setResult(RESULT_OK, intent);//응답코드 -1
//                finish();//스택에서 제거
//            }
//        });
//    };


}