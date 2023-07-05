package com.example.healthappttt.Home;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.healthappttt.Data.AlarmInfo;
import com.example.healthappttt.Data.User.CompareUser;
import com.example.healthappttt.R;

import java.util.ArrayList;
import java.util.List;

public class alarmActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private boolean topScrolled;
    private boolean updating = false;
    private AlarmAdapter adapter;

    private ArrayList<CompareUser> CompareuserList;
    private List<AlarmInfo> alarmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        setRecyclerView();
        SwipeRefreshLayout mSwipeRefreshLayout = findViewById(R.id.swipe_layout);
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
        {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
            int lastVisibleItemPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
            if(totalItemCount < 1 && !updating){
                postsUpdate(false);
            }
        }
    }

    private void postsUpdate(final boolean clear) { // 스크롤되면 할 작업
        updating = true;
        alarmList.clear();
        //Date date = userList
        // .size() == 0 || clear ? new Date() : userList.get(userList.size() - 1).getCreatedAt();
        getAlarmData();
        //UserSolt(UserList);
        //UserSolt(CurrentUser);
        //퀵정렬 편집해서 만드는건 가능한데 일단 보류 난이도가 높음.
        updating = false;
    }

    private void getAlarmData(){

    }
    private void setRecyclerView() {
        adapter = new AlarmAdapter(this, (ArrayList<AlarmInfo>) alarmList); // 나중에 routine
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
}