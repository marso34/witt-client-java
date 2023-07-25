package com.example.healthappttt.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.R;
import com.example.healthappttt.databinding.ActivityEditBodyInfoBinding;
import com.example.healthappttt.interface_.ServiceApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditBodyInfo extends AppCompatActivity {

    private ActivityEditBodyInfoBinding binding;
    private PreferenceHelper UserTB;
    private ServiceApi apiService;
    int PK;
    int height,weight; //초기에 받은 값
    int Cheight,Cweight; //수정된 값

    Map<String, Object> UpdateDefault; //서버로 보낼값

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_body_info);

        binding = ActivityEditBodyInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserTB = new PreferenceHelper("UserTB",this);
        apiService = RetrofitClient.getClient().create(ServiceApi.class);
        UpdateDefault = new HashMap<>();

        Intent intent = getIntent(); //TODO 프로필에서 intent값 넘겨주도록 하기 (비공개이면 둘다 0으로 보내기)
        PK = intent.getIntExtra("PK",0);
        height = intent.getIntExtra("height",1);
        weight = intent.getIntExtra("weight",1);
        Cheight = height;
        Cweight = weight;

        setFirstBodyInfo(); //초기값 세팅
        datachange();//데이터 바뀔때 처리 (예외처리)
        //TODO 공개하지 않을래요 클릭시 신장 체중 ->  비공개 + 0으로 처리

        //공개로 들어왔을때//비공개로 들어왔을떄

        setSecret();//비공개 버튼 클릭시 처리

        setInfo();


    }


    private void setFirstBodyInfo() {
        if (height != 0) {
            binding.height.setText(String.valueOf(height));
            binding.weight.setText(String.valueOf(weight));
        } else {
            binding.height.setText("비공개");binding.weight.setText("비공개");
            binding.checksecret.setChecked(true);
        }
        //뒤로가기 버튼
        binding.cancelEditbody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void datachange() {
        //신장, 체중 수치 텍스트 바꿀때 마다 따로 데이터 저장
        binding.height.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                        String ChangeH = binding.height.getText().toString().trim();

                        if(ChangeH.equals("")){
                            Toast.makeText(getApplicationContext(), "신장을 입력해주세요.", Toast.LENGTH_SHORT).show();
                            binding.EditBtn.setEnabled(false);
                        }else {
                            Cheight = Integer.parseInt(ChangeH);//포커스가  안될때 저장됨!
                            Log.d("바뀐 키: ", String.valueOf(Cheight));
                            binding.EditBtn.setEnabled(true);
                        }
                }
            }
        });

        binding.weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                        String ChangeW = binding.weight.getText().toString().trim();

                        if(ChangeW.equals("")){
                            Toast.makeText(getApplicationContext(), "체중을 입력해주세요.", Toast.LENGTH_SHORT).show();
                            binding.EditBtn.setEnabled(false);
                        }else{
                            Cweight = Integer.parseInt(ChangeW);
                            Log.d("바뀐 체중: ", String.valueOf(Cweight));
                            binding.EditBtn.setEnabled(true);
                        }
                }
            }
        });

    }

    private void setSecret() {
        binding.checksecret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.checksecret.isChecked()){
                    binding.height.setEnabled(false); binding.weight.setEnabled(false);
                    binding.height.setText("비공개"); binding.weight.setText("비공개");
                    Log.d("checksecret weight: ",Cweight +" 비공개" );
                }else {
                    binding.height.setEnabled(true); binding.weight.setEnabled(true);
                    binding.height.setText(String.valueOf(Cheight)); binding.weight.setText(String.valueOf(Cweight));
                    Log.d("checksecret weight: ",Cweight +" 공개" );
                    Log.d("checksecret weight: ","공개" );
                }
            }
        });
    }

    private void setInfo() {
        binding.EditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.checksecret.isChecked()){
                    //TODO 로컬에 키,몸무게 0으로 저장
                    UserTB.setBodyInfo(0,0);
                    Log.d("수정된 키: ", String.valueOf(UserTB.getheight()));
                    Log.d("수정된 키: ", String.valueOf(UserTB.getweight()));
                    Log.d("setInfo ","비공개" );
                    //TODO 서버에 키, 몸무게 0으로 저장 + is_public = 0
                    UpdateDefault.put("PK",PK);
                    UpdateDefault.put("height",0);
                    UpdateDefault.put("weight",0);
                    UpdateDefault.put("public",0);

                }else {
                    binding.height.clearFocus();
                    binding.weight.clearFocus();
                    //TODO 로컬에 키,몸무게 저장
                    UserTB.setBodyInfo(Cheight,Cweight);
                    Log.d("수정된 키: ", String.valueOf(UserTB.getheight()));
                    Log.d("수정된 키: ", String.valueOf(UserTB.getweight()));
                    Log.d("setInfo ","공개" );
                    //TODO 서버에 키, 몸무게저장 + is_public = 1
                    UpdateDefault.put("PK",PK);
                    UpdateDefault.put("height",Cheight);
                    UpdateDefault.put("weight",Cweight);
                    UpdateDefault.put("public",1);
                }
                setBodyInfo(UpdateDefault);
                finish();
            }
        });
    }

    private void setBodyInfo(Map<String,Object> userdata) {
        Call<String> call = apiService.EditBodyInfo(userdata);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    String data = response.body();
                    Log.d("setBodyInfo ","서버 db에서 수정성공 pk: "+data);
                }else{
                    Log.d("setBodyInfo ","서버 db에서 반환 값 없음");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("setBodyInfo ","서버 응답 실패");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }

}