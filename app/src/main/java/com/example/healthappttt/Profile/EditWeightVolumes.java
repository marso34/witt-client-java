package com.example.healthappttt.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.R;
import com.example.healthappttt.databinding.ActivityEditWeightVolumesBinding;
import com.example.healthappttt.interface_.ServiceApi;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.Map;

public class EditWeightVolumes extends AppCompatActivity {
    ActivityEditWeightVolumesBinding binding;
    private BottomSheetDialog bottomSheetDialog;
    private PreferenceHelper UserTB;
    private ServiceApi apiService;

    Map<String, Object> UpdateDefault; //서버로 보낼값

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
        setFirstWeight(); //초기값 세팅

        //setNumPicker(); //스피너 세팅
        //TODO 바텀시트다이얼로그
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_input_weight,null,false);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

        binding.volumes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
            }
        });
        TextView selectBtn = view.findViewById(R.id.selectBtn);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });



    }

    private void setFirstWeight() {
        binding.Psqaut.setText(String.valueOf(squatValue) ); // String?
        binding.Pbench.setText(String.valueOf(benchValue));
        binding.Pdeadlift.setText(String.valueOf(deadliftValue));

        binding.cancelEditbody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//    private void setNumPicker() {
//        bottomSheetDialog = new BottomSheetDialog(this);
//
//        if (squatValue != 0 && benchValue != 0 && deadliftValue != 0) {
//            binding.Psqaut.setText(squatValue + ""); // 스트링으로 바꾸기 편하게
//            binding.Pbench.setText(benchValue + "");
//            binding.Pdeadlift.setText(deadliftValue + ""); // 스트링으로 바꾸기 편하게
//        }
//
//        NumberPicker SquatPicker = binding.squatPicker;
//        SquatPicker.setMaxValue(500); //최대값
//        SquatPicker.setMinValue(0); //최소값
//        SquatPicker.setValue(Integer.parseInt(binding.Psqaut.getText().toString()));// 초기값
//
//        NumberPicker BenchPicker = binding.benchPicker;
//        BenchPicker.setMaxValue(500); //최대값
//        BenchPicker.setMinValue(0); //최소값
//        BenchPicker.setValue(Integer.parseInt(binding.Pbench.getText().toString()));// 초기값
//
//        NumberPicker DeadliftPicker = binding.deadliftPicker;
//        DeadliftPicker.setMaxValue(500); //최대값
//        DeadliftPicker.setMinValue(0); //최소값
//        DeadliftPicker.setValue(Integer.parseInt(binding.Pdeadlift.getText().toString()));// 초기값
//
//        TextView SelectBtn =  binding.EditBtn;
//        SelectBtn.setOnClickListener(v -> {
//            binding.Pbench.setText(SquatPicker.getValue() + ""); // 스트링으로 바꾸기 편하게
//            binding.Pbench.setText(BenchPicker.getValue() + "");
//            binding.Pdeadlift.setText(DeadliftPicker.getValue() + ""); // 스트링으로 바꾸기 편하게
//
//            bottomSheetDialog.dismiss();
//        });
//    }


























//    private void setExercisePerf() {
//        bottomSheetDialog.show();
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }
}