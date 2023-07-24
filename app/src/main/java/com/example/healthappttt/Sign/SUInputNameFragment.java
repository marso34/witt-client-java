package com.example.healthappttt.Sign;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthappttt.R;
import com.example.healthappttt.databinding.FragmentSuInputNameBinding;

import org.w3c.dom.Text;

public class SUInputNameFragment extends Fragment {
    FragmentSuInputNameBinding binding;

//    private static final String ARG_LATITUDE = "latitude";
//    private static final String ARG_LONGITUDE = "longitude";
//    private static final String ARG_GYM_LATITUDE = "gymLatitude";
//    private static final String ARG_GYM_LONGITUDE = "gymLongitude";
//    private static final String ARG_LO_NAME = "locName";
//    private static final String ARG_EMAIL = "email";

//    private static final String Backgrount_1 = "#F2F5F9";
    private static final String Backgrount_2 = "#D1D8E2";
    private static final String Signature = "#05C78C";
    private static final String White = "#ffffff";

    private static final String ARG_NAME = "name";

    private static final int GALLERY_REQUEST_CODE = 123;


    private boolean doubleBackToExitPressedOnce = false;
//    private double latitude;
//    private double longitude;
//    private double gymLatitude;
//    private double gymLongitude;
    private String loName;
    private String name;

    boolean isTrue;



    private String email;
    private Integer rtH = -1;
    private Integer rtW = -1;;
    private EditText Name;
    private Editable NameV;
    private Button sexButton;

    private ImageButton galleryPicture;

    private boolean isMale = true;
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
//            latitude = getArguments().getDouble(ARG_LATITUDE);
//            longitude = getArguments().getDouble(ARG_LONGITUDE);
//            gymLatitude = getArguments().getDouble(ARG_GYM_LATITUDE);
//            gymLongitude = getArguments().getDouble(ARG_GYM_LONGITUDE);;
//            loName = getArguments().getString(ARG_LO_NAME);
//            email = getArguments().getString(ARG_EMAIL);
//            Log.d(TAG, "--"+String.valueOf(latitude)+"--"+
//                    String.valueOf(longitude) +"--"+
//                    String.valueOf(gymLatitude) +"--"+
//                    String.valueOf(gymLongitude) +"--"+
//                    loName+"--"+
//                    email);

        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
        binding = FragmentSuInputNameBinding.inflate(inflater);

        SharedPreferences sharedPref = requireContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        String userid = sharedPref.getString("userid", "");
        Log.d("tpsdz", userid);


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
                    binding.nextBtn.setBackground(getContext().getDrawable(R.drawable.rectangle_20dp));
                    binding.nextBtn.setTextColor(Color.parseColor(Backgrount_2));
                    isTrue = false;
                } else {
                    binding.warring.setVisibility(View.GONE);
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
                ((SignUpActivity) getActivity()).goToInputBody(binding.name.getText().toString());

//                NameV = Name.getText();
//                Bundle bundle = new Bundle();
//                bundle.putInt("height", rtH);
//                bundle.putInt("weight", rtW);
//                bundle.putString("name", NameV.toString());
//                Log.d(TAG, "sub1f"+email);
//                    ((SignUpActivity) getActivity()).replaceFragment(SUInputPerfFragment.newInstance(email, latitude, longitude, gymLatitude, gymLongitude, loName, rtH, rtW, NameV.toString()));
                // 새로 불러올 Fragment의 Instance를 Main으로 전달
            } else {
//                Toast.makeText(getActivity(), "빈칸이 있습니다", Toast.LENGTH_SHORT).show();
            }
        });

        binding.galleryPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE); // 나중에 수정
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();

            galleryPicture.setImageURI(selectedImageUri);

            SharedPreferences sharedPref = requireContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("URL", selectedImageUri.toString());
            editor.apply();
     //       uploadImageToServer(selectedImageUri);
        }
    }



}

