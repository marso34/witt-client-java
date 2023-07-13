package com.example.healthappttt.Sign;

import static android.content.ContentValues.TAG;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.healthappttt.interface_.ServiceApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class sub2Fragment extends Fragment {

    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";
    private static final String ARG_GYM_LATITUDE = "gymLatitude";
    private static final String ARG_GYM_LONGITUDE = "gymLongitude";
    private static final String ARG_LO_NAME = "lo_name";
    private static final String ARG_HEIGHT = "height";
    private static final String ARG_WEIGHT = "weight";
    private static final String ARG_NAME = "name";
    private static final String ARG_EMAIL = "email";
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

    public static sub2Fragment newInstance(String email,double latitude, double longitude,double gymLatitude,double gymLongitude, String loName, int height, int weight, String name) {
        sub2Fragment fragment = new sub2Fragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_LATITUDE, latitude);
        args.putDouble(ARG_LONGITUDE, longitude);
        args.putDouble(ARG_GYM_LATITUDE, gymLatitude);
        args.putDouble(ARG_GYM_LONGITUDE, gymLongitude);
        args.putString(ARG_LO_NAME, loName);
        args.putInt(ARG_HEIGHT, height);
        args.putInt(ARG_WEIGHT, weight);
        args.putString(ARG_NAME, name);
        args.putString(ARG_EMAIL, email);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefhelper = new PreferenceHelper("UserTB",getContext());

        if (getArguments() != null) {
            latitude = getArguments().getDouble(ARG_LATITUDE);
            longitude = getArguments().getDouble(ARG_LONGITUDE);
            gymLatitude = getArguments().getDouble(ARG_GYM_LATITUDE);
            gymLongitude = getArguments().getDouble(ARG_GYM_LONGITUDE);;
            height = getArguments().getInt(ARG_HEIGHT);
            weight = getArguments().getInt(ARG_WEIGHT);
            name = getArguments().getString(ARG_NAME);
            loName = getArguments().getString(ARG_LO_NAME);
            email = getArguments().getString(ARG_EMAIL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub2, container, false);

        squatTextView = view.findViewById(R.id.SquatValue);
        benchTextView = view.findViewById(R.id.BebchValue);
        deadliftTextView = view.findViewById(R.id.DeadValue);

//        prefhelper = new PreferenceHelper("UserTB", getContext());

        Button squatUpButton = view.findViewById(R.id.SquatUpBtn);
        Button squatDownButton = view.findViewById(R.id.SquatDownBtn);
        Button benchUpButton = view.findViewById(R.id.BenchUpBtn);
        Button benchDownButton = view.findViewById(R.id.BenchDownBtn);
        Button deadliftUpButton = view.findViewById(R.id.DeadUpBtn);
        Button deadliftDownButton = view.findViewById(R.id.DeadDownBtn);
        Button checkButton = view.findViewById(R.id.Check);
        Sum3 = view.findViewById(R.id.sum3);
        updateSum3();


        squatUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                squatValue += 5;
                squatTextView.setText(String.valueOf(squatValue));
                updateSum3();
            }
        });

        squatDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(squatValue > 0)
                    squatValue -= 5;
                squatTextView.setText(String.valueOf(squatValue));
                updateSum3();
            }
        });

        benchUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                benchValue += 5;
                benchTextView.setText(String.valueOf(benchValue));
                updateSum3();
            }
        });

        benchDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(benchValue > 0)
                    benchValue -= 5;
                benchTextView.setText(String.valueOf(benchValue));
                updateSum3();
            }
        });

        deadliftUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deadliftValue += 5;
                deadliftTextView.setText(String.valueOf(deadliftValue));
                updateSum3();
            }
        });

        deadliftDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deadliftValue > 0)
                    deadliftValue -= 5;
                deadliftTextView.setText(String.valueOf(deadliftValue));
                updateSum3();
            }
        });

        Sum3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String Sum1 = s.toString();
                if (Sum1 != "0" || Sum1 != null) {
                    checkButton.setEnabled(true);
                    checkButton.setBackgroundResource(R.drawable.enabled_button_background);
                    checkButton.setTextColor(Color.parseColor("#FEFEFE"));

                } else {
                    checkButton.setEnabled(false);
                    checkButton.setBackgroundResource(R.drawable.disabled_button_background);
                    checkButton.setTextColor(Color.parseColor("#99A4B7"));
                }
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform check operation using the received data
                // You can access the latitude, longitude, name, height, and weight variables here
                // Example of using the received data
                // You can replace this with your actual logic
                Log.d(TAG, "sub2f"+email);
                sendTokenToServer(email,name);
                String message = "Name: " + email + "\n" +
                        "Height: " + height + " cm\n" +
                        "Weight: " + weight + " kg\n" +
                        "Latitude: " + latitude + "\n" +
                        "Longitude: " + longitude + "\n" +
                        "Location Name: " + loName +
                        "Values:" + squatValue +" "+ benchValue + " " + deadliftValue;
                // Display the message
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

                /**
                 * TO DO : 같은 핸드폰(Device model)이면 로컬저장 X
                 */
                //if(getPhoneInfo().getDeviceId() == )
                    //shared 로컬 저장
//                Log.d("shared 로컬 저장 내 이메일:",getUserDT().getEmail());
//                UserClass sharedUser = new UserClass(getUserDT(),getPhoneInfo(),getMannerInfo(),getLocInfo(),getExPerInfo(),getBodyInfo());
//                prefhelper.putMembership(sharedUser);

//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);

            }
        });



        return view;
    }
    private UserData getUserDT(){
        return new UserData(email,Platform,name,"face");
    }
    private PhoneInfo getPhoneInfo(){
        return new PhoneInfo("00000000000",phoneModel,serialNumber);
    }
    private MannerInfo getMannerInfo(){
        return new MannerInfo(150,150,150);
    }
    private LocInfo getLocInfo(){
        return new LocInfo(latitude,longitude,loName,gymLatitude,gymLongitude);
    }
    private ExPerfInfo getExPerInfo(){
        return new ExPerfInfo(benchValue,squatValue,deadliftValue);
    }
    private BodyInfo getBodyInfo(){
        return new BodyInfo("1999-12-27",0,height,weight);
    }

    private void sendTokenToServer(String email, String name) {
        ServiceApi apiService = RetrofitClient.getClient().create(ServiceApi.class);

        Call<Integer> call = apiService.sendData(new UserClass(getUserDT(),getPhoneInfo(),getMannerInfo(),getLocInfo(),getExPerInfo(),getBodyInfo()));

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    //shared
                    int userKey = response.body();

                    UserClass sharedUser = new UserClass(getUserDT(),getPhoneInfo(),getMannerInfo(),getLocInfo(),getExPerInfo(),getBodyInfo());

                    prefhelper.putMembership(sharedUser);
                    Log.d("shared 로컬 저장 회원가입 pk:", String.valueOf(userKey));

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("userKey",String.valueOf(userKey));
                    startActivity(intent);
                    Activity activity = getActivity();
                    activity.finish();
                    Log.d(TAG, "sendTokenToServer success");
                } else {
                    // 서버로 데이터 전송 실패
                    Toast.makeText(getActivity(), "로그인 실패", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "sendTokenToServer fail");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getActivity(), "서버로부터 응답이 없습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "sendTokenToServer error: " + t.getMessage());
            }
        });
    }

    private void updateSum3() {
        sum = squatValue + benchValue + deadliftValue;
        Sum3.setText(String.valueOf(sum) + " KG");
    }



}
