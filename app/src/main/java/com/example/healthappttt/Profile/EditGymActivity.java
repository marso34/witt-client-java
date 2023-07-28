package com.example.healthappttt.Profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.healthappttt.R;

public class EditGymActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gym);

        Intent intent = getIntent();
        String MyName = intent.getStringExtra("MyName");
        String MyGym = intent.getStringExtra("MyGym");


        EditGymFragment fragment = EditGymFragment.newInstance(MyName, MyGym);
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
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // 액티비티 종료
    }



}