package com.gwnu.witt.Sign;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gwnu.witt.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gwnu.witt.databinding.FragmentSuInputPerfBinding;

public class SUInputPerfFragment extends Fragment {
    FragmentSuInputPerfBinding binding;

    private BottomSheetDialog bottomSheetDialog;

    private static final String ARG_BENCH = "bench";
    private static final String ARG_DEAD = "dead";
    private static final String ARG_SQUAT = "squat";

    private int squatValue = 0;
    private int benchValue = 0;
    private int deadliftValue = 0;

//    private String phoneModel = Build.MODEL;
//    private String serialNumber = Build.SERIAL;

    public static SUInputPerfFragment newInstance(int bench, int deadlift, int squat) {
        SUInputPerfFragment fragment = new SUInputPerfFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_BENCH, bench);
        args.putInt(ARG_DEAD, deadlift);
        args.putInt(ARG_SQUAT, squat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            benchValue = getArguments().getInt(ARG_BENCH);
            deadliftValue = getArguments().getInt(ARG_DEAD);
            squatValue = getArguments().getInt(ARG_SQUAT);
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

        bottomSheetDialog = new BottomSheetDialog(getContext());

        View view = inflater.inflate(R.layout.input_exercise_perf, null, false);
        NumberPicker SquatPicker = view.findViewById(R.id.squatPicker);
        NumberPicker BenchPicker = view.findViewById(R.id.benchPicker);
        NumberPicker DeadliftPicker = view.findViewById(R.id.deadliftPicker);
        TextView SelectBtn = view.findViewById(R.id.selectBtn);

        bottomSheetDialog.setContentView(view);

        SquatPicker.setMaxValue(500); //최대값
        SquatPicker.setMinValue(0); //최소값
        SquatPicker.setValue(Integer.parseInt(binding.squat.getText().toString()));// 초기값

        BenchPicker.setMaxValue(500); //최대값
        BenchPicker.setMinValue(0); //최소값
        BenchPicker.setValue(Integer.parseInt(binding.benchpress.getText().toString()));// 초기값

        DeadliftPicker.setMaxValue(500); //최대값
        DeadliftPicker.setMinValue(0); //최소값
        DeadliftPicker.setValue(Integer.parseInt(binding.deadlift.getText().toString()));// 초기값

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
            ((SignUpActivity) requireActivity()).goToSelectGym(-1);
        });

        binding.skip.setOnClickListener(v -> {
            ((SignUpActivity) requireActivity()).sendToServer(0, 0, 0);
        });

        binding.setPerf.setOnClickListener(v -> setExercisePerf());

        binding.nextBtn.setOnClickListener(v -> {
            squatValue = Integer.parseInt(binding.squat.getText().toString());
            benchValue = Integer.parseInt(binding.benchpress.getText().toString());
            deadliftValue = Integer.parseInt(binding.deadlift.getText().toString());

            ((SignUpActivity) requireActivity()).sendToServer(squatValue, benchValue, deadliftValue);
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
