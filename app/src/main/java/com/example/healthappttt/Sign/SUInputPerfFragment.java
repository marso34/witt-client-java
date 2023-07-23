package com.example.healthappttt.Sign;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.User.BodyInfo;
import com.example.healthappttt.Data.User.ExPerfInfo;
import com.example.healthappttt.Data.User.LocInfo;
import com.example.healthappttt.Data.User.MannerInfo;
import com.example.healthappttt.Data.User.PhoneInfo;
import com.example.healthappttt.Data.User.UserClass;
import com.example.healthappttt.Data.User.UserData;
import com.example.healthappttt.MainActivity;
import com.example.healthappttt.R;
import com.example.healthappttt.databinding.FragmentSuInputPerfBinding;
import com.example.healthappttt.interface_.ServiceApi;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SUInputPerfFragment extends Fragment {
    FragmentSuInputPerfBinding binding;

    private BottomSheetDialog bottomSheetDialog;


//    private static final String ARG_LATITUDE = "latitude";
//    private static final String ARG_LONGITUDE = "longitude";
//    private static final String ARG_GYM_LATITUDE = "gymLatitude";
//    private static final String ARG_GYM_LONGITUDE = "gymLongitude";
//    private static final String ARG_LO_NAME = "lo_name";
//    private static final String ARG_HEIGHT = "height";
//    private static final String ARG_WEIGHT = "weight";
//    private static final String ARG_NAME = "name";
//    private static final String ARG_EMAIL = "email";

    private static final String ARG_BENCH = "bench";
    private static final String ARG_DEAD = "dead";
    private static final String ARG_SQUAT = "squat";


    private static final int Platform = 0;


    private TextView Sum3;
    private int sum = 180;
    private String email;
    private int squatValue = 0;
    private int benchValue = 0;
    private int deadliftValue = 0;
    private int height = -1;
    private int weight = -1;
    private String name;
    private double latitude;
    private double longitude;
    private double gymLatitude;
    private double gymLongitude;
    private String loName;
    private TextView squatTextView;
    private TextView benchTextView;
    private TextView deadliftTextView;
    private String phoneModel = Build.MODEL;
    private String serialNumber = Build.SERIAL;
    private PreferenceHelper prefhelper;

    public static SUInputPerfFragment newInstance(int bench, int deadlift, int squat) {
        SUInputPerfFragment fragment = new SUInputPerfFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_BENCH, bench);
        args.putInt(ARG_DEAD, deadlift);
        args.putInt(ARG_SQUAT, squat);

//        args.putDouble(ARG_LATITUDE, latitude);
//        args.putDouble(ARG_LONGITUDE, longitude);
//        args.putDouble(ARG_GYM_LATITUDE, gymLatitude);
//        args.putDouble(ARG_GYM_LONGITUDE, gymLongitude);
//        args.putString(ARG_LO_NAME, loName);
//        args.putInt(ARG_HEIGHT, height);
//        args.putInt(ARG_WEIGHT, weight);
//        args.putString(ARG_NAME, name);
//        args.putString(ARG_EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefhelper = new PreferenceHelper("UserTB",getContext());

        if (getArguments() != null) {
            benchValue = getArguments().getInt(ARG_BENCH);
            deadliftValue = getArguments().getInt(ARG_DEAD);
            squatValue = getArguments().getInt(ARG_SQUAT);

//            latitude = getArguments().getDouble(ARG_LATITUDE);
//            longitude = getArguments().getDouble(ARG_LONGITUDE);
//            gymLatitude = getArguments().getDouble(ARG_GYM_LATITUDE);
//            gymLongitude = getArguments().getDouble(ARG_GYM_LONGITUDE);;
//            height = getArguments().getInt(ARG_HEIGHT);
//            weight = getArguments().getInt(ARG_WEIGHT);
//            name = getArguments().getString(ARG_NAME);
//            loName = getArguments().getString(ARG_LO_NAME);
//            email = getArguments().getString(ARG_EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSuInputPerfBinding.inflate(inflater);

        if (squatValue != 0 && benchValue != 0 && deadliftValue != 0) {
            binding.squat.setText(squatValue + ""); // 스트링으로 바꾸기 편하게
            binding.benchpress.setText(benchValue + "");
            binding.deadlift.setText(deadliftValue + ""); // 스트링으로 바꾸기 편하게
        }

        View view = inflater.inflate(R.layout.input_exercose_perf, null, false);
        bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(view);

        NumberPicker SquatPicker = view.findViewById(R.id.squatPicker);
        SquatPicker.setMaxValue(500); //최대값
        SquatPicker.setMinValue(0); //최소값
        SquatPicker.setValue(Integer.parseInt(binding.squat.getText().toString()));// 초기값

        NumberPicker BenchPicker = view.findViewById(R.id.benchPicker);
        BenchPicker.setMaxValue(500); //최대값
        BenchPicker.setMinValue(0); //최소값
        BenchPicker.setValue(Integer.parseInt(binding.benchpress.getText().toString()));// 초기값

        NumberPicker DeadliftPicker = view.findViewById(R.id.deadliftPicker);
        DeadliftPicker.setMaxValue(500); //최대값
        DeadliftPicker.setMinValue(0); //최소값
        DeadliftPicker.setValue(Integer.parseInt(binding.deadlift.getText().toString()));// 초기값

        TextView SelectBtn =  view.findViewById(R.id.selectBtn);
        SelectBtn.setOnClickListener(v -> {
            binding.squat.setText(SquatPicker.getValue() + ""); // 스트링으로 바꾸기 편하게
            binding.benchpress.setText(BenchPicker.getValue() + "");
            binding.deadlift.setText(DeadliftPicker.getValue() + ""); // 스트링으로 바꾸기 편하게

            bottomSheetDialog.dismiss();
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.backBtn.setOnClickListener(v -> {
            ((SignUpActivity) getActivity()).goToSelectGym(-1);
        });

        binding.skip.setOnClickListener(v -> {
            ((SignUpActivity) getActivity()).sendToServer(0, 0, 0);
        });

        binding.setPerf.setOnClickListener(v -> setExercisePerf());

        binding.nextBtn.setOnClickListener(v -> {
            squatValue = Integer.parseInt(binding.squat.getText().toString());
            benchValue = Integer.parseInt(binding.benchpress.getText().toString());
            deadliftValue = Integer.parseInt(binding.deadlift.getText().toString());

            ((SignUpActivity) getActivity()).sendToServer(squatValue, benchValue, deadliftValue);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    private void setExercisePerf() {
        bottomSheetDialog.show();
    }
}
