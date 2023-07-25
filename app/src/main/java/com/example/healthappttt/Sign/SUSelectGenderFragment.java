package com.example.healthappttt.Sign;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthappttt.R;
import com.example.healthappttt.databinding.FragmentSuSelectGenderBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SUSelectGenderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SUSelectGenderFragment extends Fragment {
    FragmentSuSelectGenderBinding binding;

    private static final String Backgrount_1 = "#F2F5F9";
    private static final String Backgrount_2 = "#D1D8E2";
    private static final String Blue = "#579EF2";
    private static final String Blue_Toggle = "#E6F1FD";
    private static final String Pink = "#F257AF";
    private static final String Pink_Toggle = "#fde6f3";
    private static final String Body = "#4A5567";



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_GENDER = "gender";

    // TODO: Rename and change types of parameters
    private int gender;
    private boolean isChecked;

    public SUSelectGenderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param gender Parameter 1.
     * @return A new instance of fragment SUSelectGenderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SUSelectGenderFragment newInstance(int gender) {
        SUSelectGenderFragment fragment = new SUSelectGenderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GENDER, gender);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gender = getArguments().getInt(ARG_GENDER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSuSelectGenderBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (gender == 0) {
            binding.nextBtn.setBackground(getContext().getDrawable(R.drawable.rectangle_green_20dp));
            binding.nextBtn.setTextColor(Color.parseColor("#ffffff"));
            gender = 0;
            isChecked = true;
            setMaleIcon();
        } else if (gender == 1) {
            binding.nextBtn.setBackground(getContext().getDrawable(R.drawable.rectangle_green_20dp));
            binding.nextBtn.setTextColor(Color.parseColor("#ffffff"));
            isChecked = true;
            gender = 1;
            setFemaleIcon();
        }

        binding.backBtn.setOnClickListener(v -> {
            ((SignUpActivity) getActivity()).goToInputBody(null);
        });

        binding.maleCard.setOnClickListener(v -> {
            binding.nextBtn.setBackground(getContext().getDrawable(R.drawable.rectangle_green_20dp));
            binding.nextBtn.setTextColor(Color.parseColor("#ffffff"));
            gender = 0;
            isChecked = true;
            setMaleIcon();
        });

        binding.femaleCard.setOnClickListener(v -> {
            binding.nextBtn.setBackground(getContext().getDrawable(R.drawable.rectangle_green_20dp));
            binding.nextBtn.setTextColor(Color.parseColor("#ffffff"));
            isChecked = true;
            gender = 1;
            setFemaleIcon();
        });

        binding.nextBtn.setOnClickListener(v -> {
            if (isChecked) {
                ((SignUpActivity) getActivity()).goToSelectGym(gender);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    private void setMaleIcon() {
        binding.maleImg.setAlpha(1f);
        binding.femaleImg.setAlpha(0.2f);

        binding.maleCard.setStrokeWidth(1);
        binding.femaleCard.setStrokeWidth(0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            binding.maleCard.setOutlineSpotShadowColor(Color.parseColor(Blue));
            binding.femaleCard.setOutlineSpotShadowColor(Color.parseColor(Backgrount_1));

            binding.maleTxtView.setOutlineSpotShadowColor(Color.parseColor(Blue));
            binding.femaleTxtView.setOutlineSpotShadowColor(Color.parseColor(Backgrount_1));
        }

        binding.maleTxt.setTextColor(Color.parseColor(Blue));
        binding.femaleTxt.setTextColor(Color.parseColor(Backgrount_2));
        binding.maleTxtView.setCardBackgroundColor(Color.parseColor(Blue_Toggle));
        binding.femaleTxtView.setCardBackgroundColor(Color.parseColor(Backgrount_1));
    }

    private void setFemaleIcon() {
        binding.maleImg.setAlpha(0.2f);
        binding.femaleImg.setAlpha(1f);

        binding.maleCard.setStrokeWidth(0);
        binding.femaleCard.setStrokeWidth(1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            binding.maleCard.setOutlineSpotShadowColor(Color.parseColor(Backgrount_1));
            binding.femaleCard.setOutlineSpotShadowColor(Color.parseColor(Pink));

            binding.maleTxtView.setOutlineSpotShadowColor(Color.parseColor(Backgrount_1));
            binding.femaleTxtView.setOutlineSpotShadowColor(Color.parseColor(Pink));
        }

        binding.maleTxt.setTextColor(Color.parseColor(Backgrount_2));
        binding.femaleTxt.setTextColor(Color.parseColor(Pink));
        binding.maleTxtView.setCardBackgroundColor(Color.parseColor(Backgrount_1));
        binding.femaleTxtView.setCardBackgroundColor(Color.parseColor(Pink_Toggle));
    }
}