package com.gwnu.witt.Home;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.healthappttt.R;
import com.gwnu.witt.Data.AlarmInfo;
import com.gwnu.witt.Data.PreferenceHelper;
import com.gwnu.witt.Data.RetrofitClient;
import com.gwnu.witt.Data.SQLiteUtil;
import com.gwnu.witt.Data.pkData;
import com.gwnu.witt.interface_.ServiceApi;

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
        View view = findViewById(R.id.backBtn);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setRecyclerView() {
        adapter = new AlarmAdapter(this, alarmList); // 확인: adapter에 alarmList의 참조를 설정하는 코드가 포함되어 있는지 확인
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    private void postsUpdate(final boolean clear) {
        try {
            getAlarmListFromServer();
        } finally {
        }
    }

    private void getAlarmListFromServer() {
        try {
            sqLiteUtil.setInitView(this, "NOTIFY_TB");
            int lastKey = sqLiteUtil.SelectLastAlarm(String.valueOf(preferenceHelper.getPK()));
            Log.d(TAG, "getAlarmListFromServer: " + lastKey);
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
                            ArrayList<AlarmInfo> alist = alarmListResponse;
                            for (AlarmInfo al : alist) {
                                sqLiteUtil.setInitView(getBaseContext(), "NOTIFY_TB");
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
        }
        finally {
            sqLiteUtil.setInitView(getBaseContext(), "NOTIFY_TB");
            ArrayList<AlarmInfo> al = sqLiteUtil.selectAlarms(String.valueOf(preferenceHelper.getPK()));
            alarmList.clear();
            alarmList.addAll(al) ;
            adapter.notifyDataSetChanged(); // 확인: adapter에 변경된 alarmList를 반영하는 코드가 포함되어 있는지 확인
            updating = false;
        }
        // You may implement getAlarmData() method to retrieve data from your local database
        // Remember to handle the SQLite operations asynchronously.
    }

}
