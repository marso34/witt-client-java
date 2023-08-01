package com.gwnu.witt.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gwnu.witt.Data.PreferenceHelper;
import com.gwnu.witt.Data.RetrofitClient;
import com.gwnu.witt.R;
import com.gwnu.witt.databinding.ActivityEditWeightVolumesBinding;
import com.gwnu.witt.interface_.ServiceApi;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditWeightVolumes extends AppCompatActivity {
    ActivityEditWeightVolumesBinding binding;
    private BottomSheetDialog bottomSheetDialog;
    private PreferenceHelper UserTB;
    private ServiceApi apiService;

    Map<String, Object> UpdateDefault; //서버로 보낼값
    static int Dsquat,Dbench,Ddead;

    private int squatValue = 0;
    private int benchValue = 0;
    private int deadliftValue = 0;
    private int PK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_weight_volumes);
        binding = ActivityEditWeightVolumesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserTB = new PreferenceHelper("UserTB",this);
        apiService = RetrofitClient.getClient().create(ServiceApi.class);
        UpdateDefault = new HashMap<>();

        Intent intent = getIntent();
        squatValue = intent.getIntExtra("squatValue",0);
        benchValue = intent.getIntExtra("benchValue",0);
        deadliftValue = intent.getIntExtra("deadValue",0);
        PK = intent.getIntExtra("PK",0);
        setTextWeight(squatValue,benchValue,deadliftValue); //초기값 세팅
        Dsquat = squatValue; Dbench = benchValue; Ddead = deadliftValue; //초기값 저장

                // 바텀시트다이얼로그
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.input_exercise_perf,null,false);

        NumberPicker SquatPicker = view.findViewById(R.id.squatPicker);
        NumberPicker BenchPicker = view.findViewById(R.id.benchPicker);
        NumberPicker DeadliftPicker = view.findViewById(R.id.deadliftPicker);

        setNumPicker(view,SquatPicker,BenchPicker,DeadliftPicker);//스피너 세팅

        //초기화
        binding.resetVol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextWeight(Dsquat,Dbench,Ddead);
                squatValue = Dsquat; benchValue = Dbench; deadliftValue = Ddead;
                SquatPicker.setValue(Dsquat); BenchPicker.setValue(Dbench); DeadliftPicker.setValue(Ddead);

            }
        });
        //3대 운동
        binding.volumes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
//                binding.EditBtn.setTextColor(Color.parseColor("#FFFFFF"));//텍스트 흰색
//                binding.EditBtn.setBackgroundColor(Color.parseColor("#05C78C"));//배경 시그니처
            }
        });
        //뒤로가기
        binding.cancelEditweight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.EditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로컬에 저장
                UserTB.setWeight( squatValue ,benchValue, deadliftValue );
                // 서버에 저장
                UpdateDefault.put("PK",PK);
                UpdateDefault.put("squatValue",squatValue);
                UpdateDefault.put("benchValue",benchValue);
                UpdateDefault.put("deadliftValue",deadliftValue);

                setWeight(UpdateDefault);
                finish();

            }
        });


    }

    private void setTextWeight(int s,int b,int d) {
        binding.Psqaut.setText(String.valueOf(s));
        binding.Pbench.setText(String.valueOf(b));
        binding.Pdeadlift.setText(String.valueOf(d));
    }

    private void setNumPicker(View view,NumberPicker squatPicker,NumberPicker benchPicker,NumberPicker deadPicker) {
        if (squatValue != 0 && benchValue != 0 && deadliftValue != 0) {
            binding.Psqaut.setText(squatValue + ""); // 스트링으로 바꾸기 편하게
            binding.Pbench.setText(benchValue + "");
            binding.Pdeadlift.setText(deadliftValue + ""); // 스트링으로 바꾸기 편하게
        }

        bottomSheetDialog = new BottomSheetDialog(this);

//        NumberPicker SquatPicker = view.findViewById(R.id.squatPicker);

        TextView SelectBtn =  view.findViewById(R.id.selectBtn);

        bottomSheetDialog.setContentView(view);

        squatPicker.setMaxValue(500); //최대값
        squatPicker.setMinValue(0); //최소값
        squatPicker.setValue(Integer.parseInt(binding.Psqaut.getText().toString()));// 초기값


        benchPicker.setMaxValue(500); //최대값
        benchPicker.setMinValue(0); //최소값
        benchPicker.setValue(Integer.parseInt(binding.Pbench.getText().toString()));// 초기값


        deadPicker.setMaxValue(500); //최대값
        deadPicker.setMinValue(0); //최소값
        deadPicker.setValue(Integer.parseInt(binding.Pdeadlift.getText().toString()));// 초기값


        SelectBtn.setOnClickListener(v -> {
            binding.Psqaut.setText(squatPicker.getValue() + ""); // 스트링으로 바꾸기 편하게
            binding.Pbench.setText(benchPicker.getValue() + "");
            binding.Pdeadlift.setText(deadPicker.getValue() + ""); // 스트링으로 바꾸기 편하게
            //설정값 저장
            squatValue = squatPicker.getValue();
            benchValue = benchPicker.getValue();
            deadliftValue = deadPicker.getValue();


            bottomSheetDialog.dismiss();
        });


    }

    private void setWeight(Map<String,Object> userdata){
        Call<String> call = apiService.EditWeightVol(userdata);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    String data = response.body();
                    Log.d("EditWeight ","서버 db에서 수정성공 pk: "+data);
                }else{
                    Log.d("EditWeight ","서버 db에서 반환 값 없음");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("EditWeight ","서버 응답 실패");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }
}