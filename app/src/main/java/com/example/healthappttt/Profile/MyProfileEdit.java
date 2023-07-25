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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.R;
import com.example.healthappttt.databinding.ActivityMyProfileEditBinding;
import com.example.healthappttt.interface_.ServiceApi;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileEdit extends AppCompatActivity {

    private ActivityMyProfileEditBinding binding;
    private PreferenceHelper UserTB;
    private ServiceApi apiService;

    ImageView edit_img;
    ImageButton elbum;

    static String name;//초기 넘겨받은 데이터
    static int height,weight,gender; //초기 넘겨받은 데이터
    static int squatValue,benchValue,deadValue;//초기 넘겨받은 데이터

    AtomicInteger squatValue1,benchValue1,deadValue1,WeightSum1;
    Map<String, Object> UpdateDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_edit);

        binding = ActivityMyProfileEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserTB = new PreferenceHelper("UserTB",MyProfileEdit.this);
        apiService = RetrofitClient.getClient().create(ServiceApi.class);

        edit_img = findViewById(R.id.edit_img); // 내 프로필 사진
        elbum = findViewById(R.id.elbum);// 사진첩
        //

        //초기값 넣어주기 이름, 키, 몸무게 / 성별 / 이미지 / 3대 운동량
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("User_NM");
            height = extras.getInt("height");
            weight = extras.getInt("weight");
            gender = extras.getInt("gender");
            squatValue = extras.getInt("squatValue");
            benchValue = extras.getInt("benchValue");
            deadValue = extras.getInt("deadValue");
            }
        //AtomicInteger 변수 초기화 (3대 측정, 스쿼트, 벤치, 데드)
        squatValue1 = new AtomicInteger(squatValue);
        benchValue1 = new AtomicInteger(benchValue);
        deadValue1 = new AtomicInteger(deadValue);
        WeightSum1 = new AtomicInteger(squatValue+benchValue+deadValue);
        Log.d("MyProfileEdit 스쿼트: ", String.valueOf(squatValue1));
        Log.d("MyProfileEdit 벤치: ", String.valueOf(benchValue1));
        Log.d("MyProfileEdit 데드: ", String.valueOf(deadValue1));
        // 받은 데이터를 해당 TextView에 설정
        setFirstData();
        //앨범 버튼 onclick -> 내 앨범 가져오기

        //성별 바꾸기
        gender_click(binding.gender,gender);
        //3대 운동 kg up and down button
        setWeightUpdateListener(binding.SquatUpBtn, binding.SquatDownBtn, binding.SquatText, squatValue1);
        setWeightUpdateListener(binding.BenchUpBtn, binding.BenchDownBtn, binding.BenchText, benchValue1);
        setWeightUpdateListener(binding.DeadUpBtn,binding.DeadDownBtn,binding.DeadText, deadValue1);
        //수정하기 버튼 클릭
        UpdateMyProfile();
        //초기화 버튼 클릭
        ResetData();
    }

    public void ResetData() {
        binding.resetProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFirstData();
            }
        });
    }
    //초기 넘겨받은 데이터 설정
    public void setFirstData() {
        binding.Ename.setText(name);
        binding.Eheight.setText(String.valueOf(height));
        binding.Eweight.setText(String.valueOf(weight));
        binding.SquatText.setText(String.valueOf(squatValue));
        binding.BenchText.setText(String.valueOf(benchValue));
        binding.DeadText.setText(String.valueOf(deadValue));
        binding.Total.setText(squatValue + benchValue + deadValue + "kg");
        if(gender == 0) {
            binding.gender.setText("남자");
            binding.gender.setTextColor(Color.parseColor("#0000FF")); // 파란색
        }else{
            binding.gender.setText("여자");
            binding.gender.setTextColor(Color.parseColor("#FFC0CB")); // 핑크색
        }
        Log.d("성별: ", String.valueOf(gender));
    }


    //수정하기 버튼
    public void UpdateMyProfile() {
        binding.move3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                /**
                 * 로컬 저장하는 로직 + 서버db 저장 + (내 프로필로 데이터 넘겨주기) -> 돌아갔을 때 업데이트되어 보여짐
                 */
                UpdateDefault = new HashMap<>();
                UpdateDefault.put("User_NM",binding.Ename.getText().toString());
                UpdateDefault.put("height", Integer.valueOf(String.valueOf(binding.Eheight.getText())));
                UpdateDefault.put("weight", Integer.valueOf(String.valueOf(binding.Eweight.getText())));
                UpdateDefault.put("squatValue", squatValue1.get());
                UpdateDefault.put("benchValue", benchValue1.get());
                UpdateDefault.put("deadValue", deadValue1.get());
                UpdateDefault.put("totalValue",squatValue1.get()+benchValue1.get()+deadValue1.get());
                if(binding.gender.getText().equals("남자")) {
                    UpdateDefault.put("gender",0);
                }else{
                    UpdateDefault.put("gender",1);
                }

                UserTB.putUserDefault(UpdateDefault); // 로컬 저장

                UpdateDefault.put("myPK",UserTB.getPK()); // 키는 서버에서 필요해서 따로 추가
                EditProfile(UpdateDefault); //서버 db 수정

                setResult(RESULT_OK, intent);//응답코드 -1
                finish();//스택에서 제거
            }
        });
    }

    private void EditProfile(Map<String, Object> editData) {
        Call<String> call = apiService.EditProfile(editData);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String data = response.body();
                    //Log.d("서버에서 보내온 수정된 pk", data);
                    Log.d("MyProfileEdit","프로필 서버 db에서 수정성공");
                } else {
                    // 요청이 실패했을 때의 처리 로직
                    Log.d("MyProfileEdit","프로필 서버 db에서 수정실패");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(MyProfileEdit.this, "서버 연결 실패", Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", "서버 응답 실패. 상태코드:?");
            }
        });

    }

    public void setWeightUpdateListener(Button upButton, Button downButton,TextView TextValue,AtomicInteger weightValue ) {
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextValue.setText(String.valueOf(weightValue.addAndGet(5)));
                binding.Total.setText(WeightSum1.addAndGet(5) +"kg");
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextValue.setText(String.valueOf(weightValue.addAndGet(-5)));
                binding.Total.setText(WeightSum1.addAndGet(-5) +"kg");
            }
        });
    }

    public void gender_click(Button gender_btn, int n) {
        gender_btn.setOnClickListener(new View.OnClickListener() {
            int clickState = n;
            int gendercheck;
            @Override
            public void onClick(View v){
                if (clickState == 1) {
                    // 버튼이 클릭된 상태일 때
                    gender_btn.setTextColor(Color.parseColor("#0000FF"));// 파란색
                    gender_btn.setText("남자");
                    gendercheck = 0;
                    clickState = 0;
                    Log.d("성별 변경: ", "남자 "+String.valueOf(gendercheck));
                } else {
                    // 버튼이 클릭되지 않은 상태일 때
                    gender_btn.setTextColor(Color.parseColor("#FFC0CB"));// 핑크색
                    gender_btn.setText("여자");
                    gendercheck = 1;
                    clickState = 1;
                    Log.d("성별 변경: ", "여자 "+String.valueOf(gendercheck));
                }
            }
        });
    }



}