package com.example.healthappttt.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthappttt.Data.CompareUser;
import com.example.healthappttt.R;

import java.util.ArrayList;
import java.util.Collections;

public class alarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        // 내위트 테이블에있는 목록 다 확인, connectFlag 가 0인 친구만 오래된 시간순으로 오름차순으로 띄울것
        // 알람 어뎁터에 수락,거절 버튼이있는데 수락을 누르면 해당 유저의 connectFlag = 1; 채팅프레그먼티에서 띄우기
        //알람 어뎁터 만들기.ㅣ
    }

    private void postsUpdate(final boolean clear) { // 스크롤되면 할 작업
        updating = true;
        UserList.clear();
        //Date date = userList
        // .size() == 0 || clear ? new Date() : userList.get(userList.size() - 1).getCreatedAt();
        getUserData();
        //UserSolt(UserList);
        //UserSolt(CurrentUser);
        //퀵정렬 편집해서 만드는건 가능한데 일단 보류 난이도가 높음.
        updating = false;
    }

    public void UserSolt(UserInfo currentUser){// 유저 거리순 정렬.
        ArrayList<Integer> distans = new ArrayList<Integer>();
        for(int i=0;i<UserList.size();++i){
            if(UserList.get(i).getUserKey() == currentUser.getUserKey()) {
                distans.add(0);
                UserList.get(i).setDistance(0);
            }
            else {
                int a = 0;
                Double dis = DistanceByDegreeAndroid(currentUser.getLatitude(),currentUser.getLongitude(),UserList.get(i).getLatitude(),UserList.get(i).getLongitude());
                if(dis > 0.0) {
                    a = (int)Math.round(dis/1000);
                }
                distans.add(a);
                UserList.get(i).setDistance(a);
            }
        }
        for(int i=0;i<UserList.size();++i){
            CompareuserList.add(new CompareUser(UserList.get(i),distans.get(i)));
        }
        Collections.sort(CompareuserList);
        for(int i = 0; i < CompareuserList.size();++i)
            Log.d("list" , CompareuserList.get(i).getDistance().toString());
        UserList.clear();
        for(int i=0;i<CompareuserList.size();++i){
            UserList.add(CompareuserList.get(i).getUser());
        }
        CompareuserList.clear();
        adapter.notifyDataSetChanged();
    }

}