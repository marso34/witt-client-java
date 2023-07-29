package com.example.healthappttt.Profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.User.BlackListData;
import com.example.healthappttt.Data.User.ReviewListData;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Data.User.WittListData;
import com.example.healthappttt.R;
import com.example.healthappttt.User.BlockUserAdapter;

import java.util.ArrayList;

public class WittHistoryActivity extends AppCompatActivity {

    SQLiteUtil sqLiteUtil;

    androidx.appcompat.widget.SearchView searchView;
    ArrayList<BlackListData> BlackList;
    ArrayList<ReviewListData> ReviewList;
    ArrayList<WittListData> WittList,filteredList;
    BlockUserAdapter WittHistoryAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Button cancel_WHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_witt_history);

        // TODO 검색 매서드 WHClildFragment 살리기


        replaceFragment(new WHFragment());

        cancel_WHistory = findViewById(R.id.cancel_WHistory);
        cancel_WHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    private void replaceFragment (Fragment fragment){ //프래그먼트 설정
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if( fragment.isAdded() )
        {
            // Fragment 가 이미 추가되어 있으면 삭제한 후, 새로운 Fragment 를 생성한다.
            // 새로운 Fragment 를 생성하지 않으면 2번째 보여질 때에 Fragment 가 보여지지 않는 것 같습니다.
            fragmentTransaction.remove( fragment );
            fragment = new Fragment();
        }
        fragmentTransaction.replace(R.id.WH_frame_layout, fragment);
        fragmentTransaction.commit();
    }


}