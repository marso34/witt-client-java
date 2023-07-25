package com.example.healthappttt.Profile;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthappttt.databinding.FragmentInputWeightBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InputWeightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputWeightFragment extends Fragment {
    FragmentInputWeightBinding binding;

    private BottomSheetDialog bottomSheetDialog;

    private static final String ARG_BENCH = "bench";
    private static final String ARG_DEAD = "dead";
    private static final String ARG_SQUAT = "squat";


    private int squatValue = 0;
    private int benchValue = 0;
    private int deadliftValue = 0;

    public static InputWeightFragment newInstance(int bench, int deadlift, int squat) {
        InputWeightFragment fragment = new InputWeightFragment();
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



//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        binding = FragmentInputWeightBinding.inflate(inflater);
//
//        if (squatValue != 0 && benchValue != 0 && deadliftValue != 0) {
//            binding.squat.setText(squatValue + ""); // 스트링으로 바꾸기 편하게
//            binding.benchpress.setText(benchValue + "");
//            binding.deadlift.setText(deadliftValue + ""); // 스트링으로 바꾸기 편하게
//        }
//
//        View view = inflater.inflate(R.layout.fragment_input_weight, null, false);
//        bottomSheetDialog = new BottomSheetDialog(getContext());
//        bottomSheetDialog.setContentView(view);
//
//        NumberPicker SquatPicker = view.findViewById(R.id.squatPicker);
//        SquatPicker.setMaxValue(500); //최대값
//        SquatPicker.setMinValue(0); //최소값
//        SquatPicker.setValue(Integer.parseInt(binding.squat.getText().toString()));// 초기값
//
//        NumberPicker BenchPicker = view.findViewById(R.id.benchPicker);
//        BenchPicker.setMaxValue(500); //최대값
//        BenchPicker.setMinValue(0); //최소값
//        BenchPicker.setValue(Integer.parseInt(binding.benchpress.getText().toString()));// 초기값
//
//        NumberPicker DeadliftPicker = view.findViewById(R.id.deadliftPicker);
//        DeadliftPicker.setMaxValue(500); //최대값
//        DeadliftPicker.setMinValue(0); //최소값
//        DeadliftPicker.setValue(Integer.parseInt(binding.deadlift.getText().toString()));// 초기값
//
//        TextView SelectBtn = view.findViewById(R.id.selectBtn);
//        SelectBtn.setOnClickListener(v -> {
//            binding.squat.setText(SquatPicker.getValue() + ""); // 스트링으로 바꾸기 편하게
//            binding.benchpress.setText(BenchPicker.getValue() + "");
//            binding.deadlift.setText(DeadliftPicker.getValue() + ""); // 스트링으로 바꾸기 편하게
//
//            bottomSheetDialog.dismiss();
//        });
//
//        return binding.getRoot();
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        binding.backBtn.setOnClickListener(v -> {
//            ((SignUpActivity) getActivity()).goToSelectGym(-1);
//        });
//
//        binding.skip.setOnClickListener(v -> {
//            ((SignUpActivity) getActivity()).sendToServer(0, 0, 0);
//        });
//
//        binding.setPerf.setOnClickListener(v -> setExercisePerf());
//
//        binding.nextBtn.setOnClickListener(v -> {
//            squatValue = Integer.parseInt(binding.squat.getText().toString());
//            benchValue = Integer.parseInt(binding.benchpress.getText().toString());
//            deadliftValue = Integer.parseInt(binding.deadlift.getText().toString());
//
//            ((SignUpActivity) getActivity()).sendToServer(squatValue, benchValue, deadliftValue);
//        });
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