package com.example.healthappttt.Fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.UserData;
import com.example.healthappttt.R;
import com.example.healthappttt.interface_.ServiceApi;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class sub2Fragment extends Fragment {

    private static final String ARG_LATITUDE = "latitude";
    private static final String ARG_LONGITUDE = "longitude";
    private static final String ARG_LO_NAME = "lo_name";
    private static final String ARG_HEIGHT = "height";
    private static final String ARG_WEIGHT = "weight";
    private static final String ARG_NAME = "name";
    private static final String ARG_EMAIL = "email";

    private String email;
    private int squatValue = 0;
    private int benchValue = 0;
    private int deadliftValue = 0;
    private int height = -1;
    private int weight = -1;
    private String name;
    private double latitude ;
    private double longitude;
    private String loName;
    private TextView squatTextView;
    private TextView benchTextView;
    private TextView deadliftTextView;

    public static sub2Fragment newInstance(String email,double latitude, double longitude, String loName, int height, int weight, String name) {
        sub2Fragment fragment = new sub2Fragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_LATITUDE, latitude);
        args.putDouble(ARG_LONGITUDE, longitude);
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

        if (getArguments() != null) {
            latitude = getArguments().getDouble(ARG_LATITUDE);
            longitude = getArguments().getDouble(ARG_LONGITUDE);
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

        Button squatUpButton = view.findViewById(R.id.SquatUpBtn);
        Button squatDownButton = view.findViewById(R.id.SquatDownBtn);
        Button benchUpButton = view.findViewById(R.id.BenchUpBtn);
        Button benchDownButton = view.findViewById(R.id.BenchDownBtn);
        Button deadliftUpButton = view.findViewById(R.id.DeadUpBtn);
        Button deadliftDownButton = view.findViewById(R.id.DeadDownBtn);
        Button checkButton = view.findViewById(R.id.Check);

        squatUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                squatValue += 5;
                squatTextView.setText(String.valueOf(squatValue));
            }
        });

        squatDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                squatValue -= 5;
                squatTextView.setText(String.valueOf(squatValue));
            }
        });

        benchUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                benchValue += 5;
                benchTextView.setText(String.valueOf(benchValue));
            }
        });

        benchDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                benchValue -= 5;
                benchTextView.setText(String.valueOf(benchValue));
            }
        });

        deadliftUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deadliftValue += 5;
                deadliftTextView.setText(String.valueOf(deadliftValue));
            }
        });

        deadliftDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deadliftValue -= 5;
                deadliftTextView.setText(String.valueOf(deadliftValue));
            }
        });

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform check operation using the received data
                // You can access the latitude, longitude, name, height, and weight variables here
                // Example of using the received data
                // You can replace this with your actual logic
                String message = "Name: " + name + "\n" +
                        "Height: " + height + " cm\n" +
                        "Weight: " + weight + " kg\n" +
                        "Latitude: " + latitude + "\n" +
                        "Longitude: " + longitude + "\n" +
                        "Location Name: " + loName +
                        "Values:" + squatValue +" "+ benchValue + " " + deadliftValue;
                // Display the message
                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
    private void sendTokenToServer(String email, String name) {
        ServiceApi apiService = RetrofitClient.getClient().create(ServiceApi.class);
        UserData userData = new UserData(email, name, squatValue, benchValue, deadliftValue, height, weight, latitude, longitude, loName);

        Call<ResponseBody> call = apiService.sendData(userData);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    Log.d(TAG, "sendTokenToServer success");
                } else {
                    // 서버로 데이터 전송 실패
                    Toast.makeText(getActivity(), "로그인 실패", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "sendTokenToServer fail");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "서버로부터 응답이 없습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "sendTokenToServer error: " + t.getMessage());
            }
        });
    }

}
