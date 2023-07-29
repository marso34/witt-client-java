package com.example.healthappttt.Home;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.healthappttt.Data.AlarmInfo;
import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.Data.pkData;
import com.example.healthappttt.R;
import com.example.healthappttt.interface_.ServiceApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class alarmActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private boolean topScrolled;
    private boolean updating = false;
    private AlarmAdapter adapter;
    private SQLiteUtil sqLiteUtil;
    private ArrayList<AlarmInfo> alarmList;
    private PreferenceHelper preferenceHelper;
    private ServiceApi apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        recyclerView = findViewById(R.id.recyclerView); // Assuming you have a RecyclerView with id 'recyclerView' in your layout

        preferenceHelper = new PreferenceHelper("UserTB", this);
        alarmList = new ArrayList<>();
        SwipeRefreshLayout mSwipeRefreshLayout = findViewById(R.id.swipe_layout);
        sqLiteUtil = SQLiteUtil.getInstance();
        setRecyclerView();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        int firstVisibleItemPosition = 0;
                        int lastVisibleItemPosition = 0;
                        if (layoutManager instanceof LinearLayoutManager) {
                            firstVisibleItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                        }

                        if (totalItemCount - 3 <= lastVisibleItemPosition && !updating) {
                            postsUpdate(true);
                        }

                        if (0 < firstVisibleItemPosition) {
                            topScrolled = false;
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });

        // Load the initial data
        postsUpdate(false);
    }

    private void setRecyclerView() {
        adapter = new AlarmAdapter(this, alarmList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void postsUpdate(final boolean clear) {
        try {
            getAlarmListFromServer();
        }
        finally {
            if (clear)
                alarmList.clear();
            // Perform any background task or network request to fetch new alarm data here
            // For now, let's just add some dummy data to demonstrate
            // Replace this with your actual data retrieval logic
            // Dummy data
            sqLiteUtil.setInitView(this, "NOTIFY_TB");
            alarmList.addAll(sqLiteUtil.SelectAlarms(String.valueOf(preferenceHelper.getPK())));
            for(AlarmInfo a : alarmList){
                Log.d(TAG, "postsUpdate: "+a.getNotifyKey()+a.getCat());
            }
            adapter.notifyDataSetChanged();
            updating = false;
        }
    }

    private void getAlarmListFromServer() {
        sqLiteUtil.setInitView(this, "NOTIFY_TB");
        int lastKey = sqLiteUtil.SelectLastAlarm(String.valueOf(preferenceHelper.getPK()));
        Log.d(TAG, "getAlarmListFromServer: "+lastKey);
        apiService = RetrofitClient.getClient().create(ServiceApi.class);
        Call<ArrayList<AlarmInfo>> call = apiService.getAlarmList(new pkData(preferenceHelper.getPK(), lastKey));
        // 서버에서 알람 리스트 데이터를 받아오는 요청을 보냅니다.
        // userKey는 사용자의 식별자로 대체해야 합니다.
        call.enqueue(new Callback<ArrayList<AlarmInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<AlarmInfo>> call, Response<ArrayList<AlarmInfo>> response) {
                if (response.isSuccessful()) {
                    ArrayList<AlarmInfo> alarmListResponse = response.body(); // 수정: response.body()를 따로 변수에 저장
                    if (alarmListResponse != null) { // 수정: null 체크 추가
                        alarmList = alarmListResponse;
                        for (AlarmInfo al : alarmList) {
                            sqLiteUtil.setInitView(getApplication(), "NOTIFY_TB");
                            Log.d(TAG, "onResponse: alarm" + al.getTS().toString()); // 수정: 각 알람 정보를 로그로 확인
                            sqLiteUtil.insert(al);
                        }

                    } else {
                        Log.e(TAG, "onResponse: Alarm list is null"); // 수정: Alarm list가 null일 때 로그
                    }
                } else {
                    Log.e(TAG, "onResponse: Failed to get alarm list"); // 수정: 서버 응답 실패 로그
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AlarmInfo>> call, Throwable t) {
                Log.e(TAG, "onFailure: Error during API call", t); // 수정: 서버 요청 실패 로그
            }

        });
        // You may implement getAlarmData() method to retrieve data from your local database
        // Remember to handle the SQLite operations asynchronously.
    }

}
