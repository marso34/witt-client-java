package com.gwnu.witt.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gwnu.witt.Data.PreferenceHelper;
import com.gwnu.witt.Data.RetrofitClient;
import com.gwnu.witt.Data.User.LocInfo;
import com.gwnu.witt.R;
import com.gwnu.witt.Sign.SUSelectGymFragment;
import com.gwnu.witt.interface_.ServiceApi;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditGymActivity extends AppCompatActivity implements EditGymFragment.OnFragmentInteractionListener, SUSelectGymFragment.OnFragmentInteractionListener {
    private PreferenceHelper UserTB;
    private String MyName, MyGym, GymAdress;
    private ServiceApi apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gym);

        Intent intent = getIntent();
        MyName = intent.getStringExtra("MyName");
        MyGym = intent.getStringExtra("MyGym");
        GymAdress = intent.getStringExtra("GymAdress");

        apiService = RetrofitClient.getClient().create(ServiceApi.class);
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
        Map<String,Object> Gymdata = new HashMap<>();
        Gymdata.put("mypk",UserTB.getPK());
        Gymdata.put("LocInfo",locInfo);
        Call<String> call = apiService.EditGYM(Gymdata);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    String res = response.body();
                    Log.d("EditGym:", res);
                }else{
                    Log.d("EditWeight ","서버 db에서 반환 값 없음");
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("EditWeight ","서버 응답 실패");
            }
        });

//        finish(); // or

    }

    @Override
    public void onCancel() {
        replaceFragment(EditGymFragment.newInstance(MyName, MyGym, GymAdress)); // MyName, MyGym, GymAdress
    }
}