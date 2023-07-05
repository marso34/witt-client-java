package com.example.healthappttt.Profile;

import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_witt_history);

        /**
         * 검색 매서드 WHClildFragment 관련해서 살려야함
         */

        //searchView = findViewById(R.id.black_search);
        //recyclerView = findViewById(R.id.WittHistoryRecycle);

        //filteredList = new ArrayList<>();

//        sqLiteUtil = SQLiteUtil.getInstance(); // SQLiteUtil 객체 생성
//        sqLiteUtil.setInitView(this,"Witt_History_TB");//위트내역 목록 로컬 db
//        WittList = sqLiteUtil.SelectWittHistoryUser();//SELECT * FROM Witt_History_TB ORDER BY TS DESC

        //WittHistoryAdapter = new BlockUserAdapter(BlackList,ReviewList, WittList ,this);//어뎁터에 차단 목록 생성
//        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setAdapter(WittHistoryAdapter);

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                searchFilter(newText);
//                return false;
//            }
//        });

        replaceFragment(new WHFragment());

    }


//    public void searchFilter(String searchText) {
//        filteredList.clear();
//
//        for(int i = 0; i < WittList.size(); i++){
//            if(WittList.get(i).getUser_NM().toLowerCase().contains(searchText.toLowerCase())) {
//                filteredList.add(WittList.get(i));
//            }
//        }
//        WittHistoryAdapter.filterWittList(filteredList);
//    }

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