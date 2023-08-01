package com.gwnu.witt.Sign;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthappttt.R;

public class SUInputNameFragment extends Fragment {
    private  FragmentSuInputNameBinding binding;

//    private static final String Backgrount_1 = "#F2F5F9";
    private static final String Backgrount_2 = "#D1D8E2";
    private static final String Signature = "#05C78C";
    private static final String White = "#ffffff";

    private static final String ARG_NAME = "name";

    private static final int GALLERY_REQUEST_CODE = 123;

    private boolean doubleBackToExitPressedOnce = false;

    private String name;
    boolean isTrue;

    private Uri tempSelectedImageUri;

    private Uri selectedImageUri;

    public static SUInputNameFragment newInstance(String name) {
        SUInputNameFragment fragment = new SUInputNameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
        binding = FragmentSuInputNameBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (name != null) {
            binding.name.setText(name);
            isTrue = true;
            binding.nextBtn.setBackground(getContext().getDrawable(R.drawable.rectangle_green_20dp));
            binding.nextBtn.setTextColor(Color.parseColor(White));
        }

        binding.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() < 2 || charSequence.toString().length() > 8) {
                    binding.warring.setVisibility(View.VISIBLE);
                    binding.name.setBackground(getContext().getDrawable(R.drawable.rectangle_16dp_stroke_p));
                    binding.nextBtn.setBackground(getContext().getDrawable(R.drawable.rectangle_20dp));
                    binding.nextBtn.setTextColor(Color.parseColor(Backgrount_2));
                    isTrue = false;
                } else {
                    binding.warring.setVisibility(View.GONE);
                    binding.name.setBackground(getContext().getDrawable(R.drawable.rectangle_16dp_stroke_g));
                    binding.nextBtn.setBackground(getContext().getDrawable(R.drawable.rectangle_green_20dp));
                    binding.nextBtn.setTextColor(Color.parseColor(White));
                    isTrue = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        binding.nextBtn.setOnClickListener(v -> {
            if (isTrue) {
                ((SignUpActivity) requireActivity()).goToInputBody(binding.name.getText().toString());

                // 사용자가 이미지를 선택한 경우에만 tempSelectedImageUri를 갱신합니다.
                if (selectedImageUri != null) {
                    ((SignUpActivity) requireActivity()).receiveImageUri(selectedImageUri);
                }
            }
        });

        binding.galleryPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data.getData();

            binding.galleryPicture.setImageURI(selectedImageUri);

            // 이미지를 저장하여 SignUpActivity로 전달합니다.
        }
    }

}

