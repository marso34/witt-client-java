package com.example.healthappttt.Profile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Data.User.BlackListData;
import com.example.healthappttt.Data.User.ReportHistory;
import com.example.healthappttt.Data.User.ReviewListData;
import com.example.healthappttt.Data.User.UserKey;
import com.example.healthappttt.Data.User.WittListData;
import com.example.healthappttt.R;
import com.example.healthappttt.User.BlockUserAdapter;
import com.example.healthappttt.interface_.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlackActivity extends AppCompatActivity {

    SQLiteUtil sqLiteUtil;
    private ServiceApi apiService;
    private PreferenceHelper UserTB;
    private BlackListData BlackListdata;
    androidx.appcompat.widget.SearchView searchView;
    ArrayList<BlackListData> BlackList, filteredList;
    ArrayList<ReviewListData> ReviewList;
    ArrayList<WittListData> WittList;
    ArrayList<ReportHistory> ReportList;
    BlockUserAdapter BlackAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Button cancel_black;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);
        apiService = RetrofitClient.getClient().create(ServiceApi.class);
        UserTB = new PreferenceHelper("UserTB",this);
        searchView = findViewById(R.id.black_search);
        recyclerView = findViewById(R.id.blockrecycle);

        filteredList = new ArrayList<>();

        sqLiteUtil = SQLiteUtil.getInstance(); // SQLiteUtil 객체 생성
        sqLiteUtil.setInitView(this,"BLACK_LIST_TB");//차단 목록 로컬 db
        //SELECT * FROM BLACK_LIST_TB

        UserKey userKey = new UserKey(UserTB.getPK());
        getBlackList(userKey);





        //검색 관련 매서드
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // 검색 버튼을 누를 때 호출됨
                // s 변수에 검색창에 입력된 텍스트가 담겨있습니다.
                // 이곳에서 원하는 동작을 수행하면 됩니다.
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // 검색창의 텍스트가 변경될 때마다 호출됨
                // s 변수에 검색창에 입력된 텍스트가 담겨있습니다.
                // 이곳에서 원하는 동작을 수행하면 됩니다.
                searchFilter(s);
                return false;
            }
            });

      //  BlockList.add(new UserProfile("dkh")); //리스트에 추가 -> 매서드로 변경 필요
       // BlockList.add(new UserProfile("hwstar"));

        cancel_black = findViewById(R.id.cancel_black);
        cancel_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //검색창에서 텍스트가 바뀔때마다 해당 텍스트에 해당하는 요소를 보여주는 매서드
    public void searchFilter(String searchText) {
        filteredList.clear();

        for(int i = 0; i < BlackList.size(); i++){
            if(BlackList.get(i).getUser_NM().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(BlackList.get(i));
            }
        }
        BlackAdapter.filterBlackList(filteredList);
    }

    private void getBlackList(UserKey userKey) {
        Call<List<BlackListData>> call = apiService.getBlackList(userKey);
        call.enqueue(new Callback<List<BlackListData>>() {
            @Override
            public void onResponse(Call<List<BlackListData>> call, Response<List<BlackListData>> response) {
                if (response.isSuccessful()) {
//                    Log.d(TAG, "ssss: a"+response.body().get(0).getUser_NM());
                    BlackList = (ArrayList<BlackListData>) response.body();
                    if(BlackList.size()>0) {


                        BlackAdapter = new BlockUserAdapter(BlackList, ReviewList, WittList, BlackActivity.this);//어뎁터에 차단 목록 생성
                        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(BlackAdapter);
                        BlackAdapter.notifyDataSetChanged(); //변경점을 어뎁터에 알림
                    }
                } else {
                    Log.e("getBlackList", "API 요청 실패. 응답 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<BlackListData>> call, Throwable t) {
                Log.e("getBlackList", "API 요청실패, 에러메세지: " + t.getMessage());
            }
        });

    }

    private void SaveBlackList(BlackListData blackListData) {
//        sqLiteUtil = SQLiteUtil.getInstance();
        sqLiteUtil.setInitView(this, "BLACK_LIST_TB");

        // 중복 PK 확인
        boolean CheckStored = false;
        List<BlackListData> blackList = sqLiteUtil.SelectBlackUser();
        for (BlackListData storedData : blackList) {
            int storedPK = storedData.getBL_PK();
            if (storedPK == blackListData.getBL_PK()) {
                CheckStored = true;
                break;
            }
        }

        if (CheckStored) {
            Log.d("SaveBlackList 메서드", "중복된 PK -> 저장 X");
        } else {
            sqLiteUtil.setInitView(this, "BLACK_LIST_TB");
            sqLiteUtil.insertBL(blackListData);
            Log.d("SaveBlackList 메서드", "저장 완료");
        }
    }


}