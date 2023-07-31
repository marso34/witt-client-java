package com.example.healthappttt.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.User.LocInfo;
import com.example.healthappttt.R;
import com.example.healthappttt.Sign.SUSelectGymFragment;

public class EditGymActivity extends AppCompatActivity implements EditGymFragment.OnFragmentInteractionListener, SUSelectGymFragment.OnFragmentInteractionListener {
    private PreferenceHelper UserTB;
    private String MyName, MyGym, GymAdress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gym);

        Intent intent = getIntent();
        MyName = intent.getStringExtra("MyName");
        MyGym = intent.getStringExtra("MyGym");
        GymAdress = intent.getStringExtra("GymAdress");

        UserTB = new PreferenceHelper("UserTB",this);

        Log.d("테스트1", MyGym + "   ddd");

        EditGymFragment fragment = EditGymFragment.newInstance(MyName, MyGym, GymAdress);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ContentLayout2, fragment)
                .commit();
    }
    //필요시 사용
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ContentLayout2, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }

    @Override
    public void EditGymInfo() {
        replaceFragment(SUSelectGymFragment.newInstance(0, 0, UserTB.getGYMNM(), true));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // 액티비티 종료
    }

    @Override
    public void onSaveLocation(double userLat, double userLon, double gymLat, double gymLon, String gymName, String gymAdress) {
        Log.d("위치 정보", userLat + " " + userLon);
        Log.d("위치 정보", gymName + " " + gymAdress);
        Log.d("위치 정보", gymLat + " " + gymLon);
        LocInfo locInfo = new LocInfo(userLat,userLon,gymName,gymLat,gymLon,gymAdress);
        // 저장
        UserTB.setLoc(locInfo);
        replaceFragment(EditGymFragment.newInstance(MyName, MyGym, GymAdress)); // MyName, MyGym, GymAdress
//        finish(); // or

    }

    @Override
    public void onCancel() {
        replaceFragment(EditGymFragment.newInstance(MyName, MyGym, GymAdress)); // MyName, MyGym, GymAdress
    }
}