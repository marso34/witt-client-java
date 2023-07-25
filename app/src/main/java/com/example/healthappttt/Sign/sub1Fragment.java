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
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.healthappttt.R;

public class sub1Fragment extends Fragment {
    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";
    private static final String ARG_GYM_LATITUDE = "gymLatitude";
    private static final String ARG_GYM_LONGITUDE = "gymLongitude";
    private static final String ARG_LO_NAME = "locName";
    private static final String ARG_EMAIL = "email";

    private static final int GALLERY_REQUEST_CODE = 123;


    private boolean doubleBackToExitPressedOnce = false;
    private double latitude;
    private double longitude;
    private double gymLatitude;
    private double gymLongitude;
    private String loName;
    private String name;
    private String email;
    private Integer rtH = -1;
    private Integer rtW = -1;;
    private EditText Name;
    private Editable NameV;
    private Button sexButton;

    private ImageButton galleryPicture;

    private boolean isMale = true;
    public static sub1Fragment newInstance(String email, double latitude, double longitude,double gymLatitude,double gymLongitude, String LoName) {
        sub1Fragment fragment = new sub1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, email);
        args.putDouble(ARG_LATITUDE, latitude);
        args.putDouble(ARG_LONGITUDE, longitude);
        args.putDouble(ARG_GYM_LATITUDE, gymLatitude);
        args.putDouble(ARG_GYM_LONGITUDE, gymLongitude);
        args.putString(ARG_LO_NAME, LoName);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            latitude = getArguments().getDouble(ARG_LATITUDE);
            longitude = getArguments().getDouble(ARG_LONGITUDE);
            gymLatitude = getArguments().getDouble(ARG_GYM_LATITUDE);
            gymLongitude = getArguments().getDouble(ARG_GYM_LONGITUDE);;
            loName = getArguments().getString(ARG_LO_NAME);
            email = getArguments().getString(ARG_EMAIL);
            Log.d(TAG, "--"+String.valueOf(latitude)+"--"+
                    String.valueOf(longitude) +"--"+
                    String.valueOf(gymLatitude) +"--"+
                    String.valueOf(gymLongitude) +"--"+
                    loName+"--"+
                    email);

        }


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
        View view = inflater.inflate(R.layout.fragment_sub1, container, false);    // Fragment로 불러올 xml파일을 view로 가져옵니다.
        Button button1 = (Button)view.findViewById(R.id.study_button);   // click시 Fragment를 전환할 event를 발생시킬 버튼을 정의합니다.
        Name = (EditText)view.findViewById(R.id.name);
        EditText H = (EditText) view.findViewById(R.id.hight);
        H.setInputType(InputType.TYPE_CLASS_NUMBER);

        SharedPreferences sharedPref = requireContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        String userid = sharedPref.getString("userid", "");
        Log.d("tpsdz", userid);

        EditText W = (EditText) view.findViewById(R.id.weight);
        W.setInputType(InputType.TYPE_CLASS_NUMBER);
        

        sexButton = view.findViewById(R.id.sex);
        sexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMale) {
                    sexButton.setText("여");
                    sexButton.setTextColor(Color.parseColor("#FF3399")); ; // 핑크색
                    isMale = false;
                } else {
                    sexButton.setText("남");
                    sexButton.setTextColor(Color.parseColor("#579EF2")); // 파란색
                    isMale = true;
                }
            }
        });

        button1.setEnabled(false);


        H.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String heightText = s.toString();
                if (!heightText.isEmpty() && !W.getText().toString().isEmpty() && Name.getText() != null) {
                    button1.setEnabled(true);
                    button1.setBackgroundResource(R.drawable.enabled_button_background);
                    button1.setTextColor(Color.parseColor("#FEFEFE"));
                } else {
                    button1.setEnabled(false);
                    button1.setBackgroundResource(R.drawable.disabled_button_background);
                    button1.setTextColor(Color.parseColor("#99A4B7"));
                }
            }
        });
        W.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String weightText = s.toString();
                if (!weightText.isEmpty() && !H.getText().toString().isEmpty() && Name.getText() != null) {
                    button1.setEnabled(true);
                    button1.setBackgroundResource(R.drawable.enabled_button_background);
                    button1.setTextColor(Color.parseColor("#FEFEFE"));

                } else {
                    button1.setEnabled(false);
                    button1.setBackgroundResource(R.drawable.disabled_button_background);
                    button1.setTextColor(Color.parseColor("#99A4B7"));
                }
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button1.isEnabled()) {
                    rtH = Integer.parseInt(H.getText().toString());
                    rtW = Integer.parseInt(W.getText().toString());
                    NameV = Name.getText();
                    Bundle bundle = new Bundle();
                    bundle.putInt("height", rtH);
                    bundle.putInt("weight", rtW);
                    bundle.putString("name", NameV.toString());
                    Log.d(TAG, "sub1f"+email);
                    ((SubActivity) getActivity()).replaceFragment(sub2Fragment.newInstance(email, latitude, longitude, gymLatitude, gymLongitude, loName, rtH, rtW, NameV.toString()));
                    // 새로 불러올 Fragment의 Instance를 Main으로 전달
                } else {
                    Toast.makeText(getActivity(), "빈칸이 있습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        galleryPicture = view.findViewById(R.id.galleryPicture);



        galleryPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });



        return view;
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

