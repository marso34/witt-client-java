package com.example.healthappttt.Sign;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

public class SignUpActivity extends AppCompatActivity
{
    String email = "jjj";
    String name;
    int platform;
    int height, weight;
    private boolean isPublic;
    int gender;
    double lat, lon;
    String gymName;
    int bench, deadlift, squat;
    double gymLat, gymLon;
    private PreferenceHelper UserTB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserTB = new PreferenceHelper("UserTB",this);
        Intent intent = getIntent();
        if (intent != null) {
            email = intent.getStringExtra("email");
            name = intent.getStringExtra("name");
            platform = intent.getIntExtra("platform", 0);
        }

        height = 170;
        weight = 60;
        gender = -1;
        bench = deadlift = squat = 100; // 데이터 초기값
        isPublic = true;

        setContentView(R.layout.activity_sign_up);

        SUInputNameFragment fragment = SUInputNameFragment.newInstance(name);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ContentLayout, fragment)
                .commit();
    }


    public void goToInputName() {
        replaceFragment(SUInputNameFragment.newInstance(name));
    }

    public void goToInputBody(String name) {
        if (name != null)
            this.name = name;

        replaceFragment(SUInputBodyFragment.newInstance(height, weight, isPublic));
    }

    public void goToSelectGender(int height, int weight, boolean isPublic) {
        if (height != -1)
            this.height = height;
        if (weight != -1) {
            this.weight = weight;
            this.isPublic = isPublic;
        }

        replaceFragment(SUSelectGenderFragment.newInstance(gender));
    }

    public void goToSelectGym(int gender) {
        if (gender != -1)
            this.gender = gender;

        replaceFragment(SUSelectGymFragment.newInstance(lat, lon, gymName));
    }

    public void goToInputPerf(double lat, double lon, String gymName, double gymLat, double gymLon) {
        if (lat != 0)
            this.lat = lat;
        if (lon != 0)
            this.lon = lon;
        if (gymName != null)
            this.gymName = gymName;

        this.gymLat = gymLat;
        this.gymLon = gymLon;

        replaceFragment(SUInputPerfFragment.newInstance(bench, deadlift, squat));
    }

    public void sendToServer(int squat, int bench, int deadlift) {
        this.squat = squat;
        this.bench = bench;
        this.deadlift = deadlift;


        sendTokenToServer();

        //            Log.d(TAG, "sub2f"+email);
//                sendTokenToServer(email,name);
//            String message = "Name: " + email + "\n" +
//                    "Height: " + height + " cm\n" +
//                    "Weight: " + weight + " kg\n" +
//                    "Latitude: " + latitude + "\n" +
//                    "Longitude: " + longitude + "\n" +
//                    "Location Name: " + loName +
//                    "Values:" + squatValue +" "+ benchValue + " " + deadliftValue;
//            // Display the message
//            Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show();
    }

//
    private UserData getUserDT() {
        return new UserData(email, platform, name,"face");
    }

    private PhoneInfo getPhoneInfo() {
        return new PhoneInfo("00000000000", "phoneModel", "serialNumber");
    }

    private MannerInfo getMannerInfo() {
        return new MannerInfo(150,150,150);
    } // 매너 테이블에 디폴트 값 넣고 그냥 생성하면 되잖아?

    private LocInfo getLocInfo(){
        return new LocInfo(lat, lon, gymName, gymLat, gymLon);
    }

    private ExPerfInfo getExPerInfo() {
        return new ExPerfInfo(bench, squat, deadlift);
    }

    private BodyInfo getBodyInfo() {
        int temp;
        if (isPublic)   temp = 1;
        else            temp = 0;

        return new BodyInfo("1999-12-27", gender, height, weight, temp);
    }
//
    private void sendTokenToServer() {
        ServiceApi apiService = RetrofitClient.getClient().create(ServiceApi.class);

        Call<Integer> call = apiService.sendData(new UserClass(getUserDT(), getPhoneInfo(), getMannerInfo(), getLocInfo(), getExPerInfo(), getBodyInfo()));

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    //shared
                    int userKey = response.body();

                    UserClass sharedUser = new UserClass(getUserDT(), getPhoneInfo(), getMannerInfo(), getLocInfo(), getExPerInfo(), getBodyInfo());

                    UserTB.putMembership(sharedUser);
                    Log.d("shared 로컬 저장 회원가입 pk:", String.valueOf(userKey));

                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    intent.putExtra("userKey",String.valueOf(userKey));
                    startActivity(intent);
                    finish();
                    Log.d(TAG, "sendTokenToServer success");
                } else {
                    // 서버로 데이터 전송 실패
//                    Toast.makeText(requireActivity(), "로그인 실패", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "sendTokenToServer fail");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
//                Toast.makeText(requireActivity(), "서버로부터 응답이 없습니다.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "sendTokenToServer error: " + t.getMessage());
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ContentLayout, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // 액티비티 종료
    }

}

