//package com.example.healthappttt.Activity;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//
//import com.example.healthappttt.Data.CompareUser;
//import com.example.healthappttt.Data.UserInfo;
//import com.example.healthappttt.R;
//import com.example.healthappttt.adapter.UserAdapter;
//
//import java.util.ArrayList;
//import java.util.Collections;
//
//public class alarmActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_alarm);
//        setRecyclerView();
//        SwipeRefreshLayout mSwipeRefreshLayout = view.findViewById(R.id.swipe_layout);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//                        int visibleItemCount = layoutManager.getChildCount();
//                        int totalItemCount = layoutManager.getItemCount();
//                        int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
//                        int lastVisibleItemPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
//
//                        if(totalItemCount - 3 <= lastVisibleItemPosition && !updating){
//                            postsUpdate(true);
//                        }
////
//                        if(0 < firstVisibleItemPosition){
//                            topScrolled = false;
//                        }
//                        mSwipeRefreshLayout.setRefreshing(false);
//                    }
//                }, 500);
//            }
//        });
//        {
//            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//            int visibleItemCount = layoutManager.getChildCount();
//            int totalItemCount = layoutManager.getItemCount();
//            int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
//            int lastVisibleItemPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
//            if(totalItemCount < 1 && !updating){
//                postsUpdate(false);
//            }
//        }
//        return view;
//    }
//
//    private void postsUpdate(final boolean clear) { // 스크롤되면 할 작업
//        updating = true;
//        UserList.clear();
//        //Date date = userList
//        // .size() == 0 || clear ? new Date() : userList.get(userList.size() - 1).getCreatedAt();
//        getUserData();
//        //UserSolt(UserList);
//        //UserSolt(CurrentUser);
//        //퀵정렬 편집해서 만드는건 가능한데 일단 보류 난이도가 높음.
//        updating = false;
//    }
//
//    public void UserSolt(UserInfo currentUser){// 유저 거리순 정렬.
//        ArrayList<Integer> distans = new ArrayList<Integer>();
//        for(int i=0;i<UserList.size();++i){
//            if(UserList.get(i).getUserKey() == currentUser.getUserKey()) {
//                distans.add(0);
//                UserList.get(i).setDistance(0);
//            }
//            else {
//                int a = 0;
//                Double dis = DistanceByDegreeAndroid(currentUser.getLatitude(),currentUser.getLongitude(),UserList.get(i).getLatitude(),UserList.get(i).getLongitude());
//                if(dis > 0.0) {
//                    a = (int)Math.round(dis/1000);
//                }
//                distans.add(a);
//                UserList.get(i).setDistance(a);
//            }
//        }
//        for(int i=0;i<UserList.size();++i){
//            CompareuserList.add(new CompareUser(UserList.get(i),distans.get(i)));
//        }
//        Collections.sort(CompareuserList);
//        for(int i = 0; i < CompareuserList.size();++i)
//            Log.d("list" , CompareuserList.get(i).getDistance().toString());
//        UserList.clear();
//        for(int i=0;i<CompareuserList.size();++i){
//            UserList.add(CompareuserList.get(i).getUser());
//        }
//        CompareuserList.clear();
//        adapter.notifyDataSetChanged();
//    }
//    private void setRecyclerView() {
//        adapter = new UserAdapter(getContext(), (ArrayList<UserInfo>) UserList); // 나중에 routine
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);
//
//    }
//}