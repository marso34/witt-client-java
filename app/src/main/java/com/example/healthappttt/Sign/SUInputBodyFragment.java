package com.example.healthappttt.Sign;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.healthappttt.R;
import com.example.healthappttt.databinding.FragmentSuInputBodyBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SUInputBodyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SUInputBodyFragment extends Fragment {
    FragmentSuInputBodyBinding binding;

    private BottomSheetDialog bottomSheetDialog;

    private static final String Backgrount_1 = "#F2F5F9";
    private static final String Backgrount_2 = "#D1D8E2";
    private static final String Body = "#4A5567";
    private static final String Signature = "#05C78C";
    private static final String White = "#ffffff";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_HEIGHT = "height";
    private static final String ARG_WEIGHT = "weight";
    private static final String ARG_PUBLIC = "isPublic";


    // TODO: Rename and change types of parameters
    private int height;
    private int weight;
    private boolean isPublic;

    public SUInputBodyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param height Parameter 1.
     * @param weight Parameter 2.
     * @return A new instance of fragment SUIput_BodyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SUInputBodyFragment newInstance(int height, int weight, boolean isPublic) {
        SUInputBodyFragment fragment = new SUInputBodyFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_HEIGHT, height);
        args.putInt(ARG_WEIGHT, weight);
        args.putBoolean(ARG_PUBLIC, isPublic);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            height = getArguments().getInt(ARG_HEIGHT);
            weight = getArguments().getInt(ARG_WEIGHT);
            isPublic = getArguments().getBoolean(ARG_PUBLIC);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSuInputBodyBinding.inflate(inflater);

        View view = inflater.inflate(R.layout.input_height_weight, null, false);
        bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(view);

        NumberPicker HeightPicker = view.findViewById(R.id.heightPicker);
        HeightPicker.setMaxValue(300); //최대값
        HeightPicker.setMinValue(100); //최소값
        HeightPicker.setValue(Integer.parseInt(binding.heightTxt.getText().toString()));// 초기값

        NumberPicker WeightPicker = view.findViewById(R.id.weightPicker);
        WeightPicker.setMaxValue(200); //최대값
        WeightPicker.setMinValue(30); //최소값
        WeightPicker.setValue(Integer.parseInt(binding.weightTxt.getText().toString()));// 초기값

        TextView SelectBtn =  view.findViewById(R.id.selectBtn);
        SelectBtn.setOnClickListener(v -> {
            binding.heightTxt.setText(HeightPicker.getValue() + ""); // 스트링으로 바꾸기 편하게
            binding.weightTxt.setText(WeightPicker.getValue() + "");

            bottomSheetDialog.dismiss();
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.heightTxt.setText(height + "");
        binding.weightTxt.setText(weight + "");

        binding.backBtn.setOnClickListener(v -> {
            ((SignUpActivity) getActivity()).goToInputName();
        });

        binding.height.setOnClickListener(v -> setHeightAndWeight());
        binding.weight.setOnClickListener(v -> setHeightAndWeight());

        binding.isPublic.setOnClickListener(v -> {
            if (binding.isPublic.isChecked()) {
                binding.isPublic.setTextColor(Color.parseColor(Signature));
                isPublic = true;
            } else {
                binding.isPublic.setTextColor(Color.parseColor(Body));
                isPublic = false;
            }
        });

        binding.nextBtn.setOnClickListener(v -> {
            String height = binding.heightTxt.getText().toString() , weight =  binding.weightTxt.getText().toString();

            ((SignUpActivity) getActivity()).goToSelectGender(Integer.parseInt(height), Integer.parseInt(weight), isPublic);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    private void setHeightAndWeight() {
        bottomSheetDialog.show();
    }
}