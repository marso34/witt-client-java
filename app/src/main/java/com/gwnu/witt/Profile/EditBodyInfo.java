package com.gwnu.witt.Profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gwnu.witt.Data.PreferenceHelper;
import com.gwnu.witt.Data.RetrofitClient;
import com.gwnu.witt.R;
import com.gwnu.witt.databinding.ActivityEditBodyInfoBinding;
import com.gwnu.witt.interface_.ServiceApi;
import com.google.android.material.bottomsheet.BottomSheetDialog;

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
    int temp; //is_public
    String Scolor = "#05C78C";
    String Bcolor = "#4A5567";
    Map<String, Object> UpdateDefault; //서버로 보낼값

    private BottomSheetDialog bottomSheetDialog;
    NumberPicker HeightPicker,WeightPicker;
    Button EditBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_body_info);

        binding = ActivityEditBodyInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserTB = new PreferenceHelper("UserTB",this);
        apiService = RetrofitClient.getClient().create(ServiceApi.class);
        UpdateDefault = new HashMap<>();

        Intent intent = getIntent(); // 프로필에서 intent값 넘겨주도록 하기 (비공개이면 둘다 0으로 보내기)
        PK = intent.getIntExtra("PK",0);
        height = intent.getIntExtra("height",1);
        weight = intent.getIntExtra("weight",1);
        temp = intent.getIntExtra("temp",0);
        Cheight = height;
        Cweight = weight;

        EditBtn = findViewById(R.id.EditBtn);


        // 바텀시트다이얼로그
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.input_height_weight,null,false);

        HeightPicker = view.findViewById(R.id.heightPicker);
        WeightPicker = view.findViewById(R.id.weightPicker);
        setNumPicker(view ,HeightPicker, WeightPicker);

        setFirstBodyInfo(); //초기값 세팅

        setSecret();//비공개 버튼 클릭시 처리

        setInfo();//수정하기


    }

    public void setNumPicker(View view,NumberPicker HeightPicker,NumberPicker WeightPicker) {

        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

        HeightPicker.setMaxValue(300); //최대값
        HeightPicker.setMinValue(100); //최소값
        if(temp == 0){
            HeightPicker.setValue(170);// 초기값
        }else{
            HeightPicker.setValue(height);
        }

        WeightPicker.setMaxValue(200); //최대값
        WeightPicker.setMinValue(30); //최소값
        if(temp == 0){
            WeightPicker.setValue(60);// 초기값
        }else{
            WeightPicker.setValue(weight);
        }


        TextView SelectBtn =  view.findViewById(R.id.selectBtn);
        SelectBtn.setOnClickListener(v -> {
            binding.height.setText(HeightPicker.getValue() + ""); // 스트링으로 바꾸기 편하게
            binding.weight.setText(WeightPicker.getValue() + "");
            Cheight = HeightPicker.getValue();
            Cweight = WeightPicker.getValue();
            bottomSheetDialog.dismiss();
        });

    }


    private void setFirstBodyInfo() {
        if (temp != 0) {
            binding.controlDialog.setEnabled(true);
            binding.height.setText(String.valueOf(height));
            binding.weight.setText(String.valueOf(weight));
            HeightPicker.setValue(height);
            WeightPicker.setValue(weight);
            binding.checksecret.setTextColor(Color.parseColor(Bcolor));
        } else {
            binding.controlDialog.setEnabled(false);
            binding.height.setText("비공개");binding.weight.setText("비공개");
            binding.checksecret.setTextColor(Color.parseColor(Scolor));
            binding.checksecret.setChecked(true);
        }
        // 키,몸무게 수정 다이얼로그
        // 키,몸무게 수정 다이얼로그
        binding.height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(temp != 0){
                    bottomSheetDialog.show();
                }
            }
        });
        binding.weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(temp != 0){
                    bottomSheetDialog.show();
                }
            }
        });

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
                if(!hasFocus ) {
                    String ChangeH = binding.height.getText().toString().trim();

                    if(ChangeH.equals("") | ChangeH.equals("0")){
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
                if(!hasFocus ) {
                        String ChangeW = binding.weight.getText().toString().trim();

                        if(ChangeW.equals("") | ChangeW.equals("0")){
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
                    binding.controlDialog.setEnabled(false);
                    binding.height.setText("비공개"); binding.weight.setText("비공개");
                    binding.checksecret.setTextColor(Color.parseColor(Scolor));
                    Log.d("checksecret weight: ",Cweight +" 비공개" );
                    temp = 0;
                }else {
                    binding.controlDialog.setEnabled(true);
                    binding.height.setText(String.valueOf(Cheight)); binding.weight.setText(String.valueOf(Cweight));
                    binding.checksecret.setTextColor(Color.parseColor(Bcolor));
//                    datachange();
                    Log.d("checksecret weight: ",Cweight +" 공개" );
                    Log.d("checksecret weight: ","공개" );
                    temp = 1;
                }
            }
        });
    }

    private void setInfo() {
        binding.EditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.checksecret.isChecked()){
                    // 로컬에 키,몸무게 0으로 저장
                    UserTB.setBodyInfo(height,weight,0);
                    Log.d("setInfo ","비공개 키: "+height );
                    // 서버에 키, 몸무게 초기값 저장 + is_public = 0
                    UpdateDefault.put("PK",PK);
                    UpdateDefault.put("height",0);
                    UpdateDefault.put("weight",0);
                    UpdateDefault.put("public",0);

                }else {
                    // 로컬에 키,몸무게 저장
                    UserTB.setBodyInfo(Cheight,Cweight,1);
                    Log.d("setInfo ","공개" );
                    // 서버에 키, 몸무게저장 + is_public = 1
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