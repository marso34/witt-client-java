package com.example.healthappttt.Sign;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.healthappttt.R;

public class SubActivity extends AppCompatActivity
{
    String email="jjj";
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            email = intent.getStringExtra("email");
            name = intent.getStringExtra("name");
        }

        setContentView(R.layout.activity_sub);
        subFragment fragment = subFragment.newInstance(email);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ContentLayout, fragment)
                .commit();
    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ContentLayout, fragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }
}

