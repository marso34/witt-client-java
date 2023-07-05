package com.example.healthappttt.Chat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.Chat.UserChat;
import com.example.healthappttt.Data.User.UserKey;
import com.example.healthappttt.R;
import com.example.healthappttt.User.UserListAdapter;
import com.example.healthappttt.interface_.ServiceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChattingFragment extends Fragment {
    private RecyclerView userlistRecyclerView;
    private UserListAdapter userListAdapter;
    private List<UserChat> userList;
    private PreferenceHelper prefhelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 레이아웃을 inflate합니다.
        View view = inflater.inflate(R.layout.fragment_chatting, container, false);
        userList = new ArrayList<>();

        getUsersFromServer();
        // 리사이클러뷰를 초기화합니다.
        userlistRecyclerView = view.findViewById(R.id.userlistRecyclerView);
        userlistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 유저 목록을 가져와서 userList에 저장합니다.

        // 어댑터를 초기화하고 userList를 설정합니다.
        userListAdapter = new UserListAdapter(getContext(),userList);
        userlistRecyclerView.setAdapter(userListAdapter);

        // 어댑터의 아이템 클릭 리스너를 설정합니다.
       



        return view;
    }

    // 서버에서 유저 목록을 가져오는 메소드입니다.
    private void getUsersFromServer() {
        prefhelper = new PreferenceHelper(getContext());
        // 서버로부터 유저 목록을 가져와서 List<UserChat>로 반환합니다.
        ServiceApi apiService = RetrofitClient.getClient().create(ServiceApi.class);
        Log.d(TAG, "getUsersFromServer: "+String.valueOf(prefhelper.getPK()));
        Call<List<UserChat>> call = apiService.getUsers(new UserKey(prefhelper.getPK())); // 유저키 얻어와서 넣기
        call.enqueue(new Callback<List<UserChat>>() {
            @Override
            public void onResponse(Call<List<UserChat>> call, Response<List<UserChat>> response) {
                if (response.isSuccessful()) {
                    List<UserChat> users = response.body();
                    userList.clear();
                    for (UserChat user : users) {
                        userList.add(user);
                        Log.d("chatUSERLIST", user.getUserNM());
                    }
                    userListAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Failed to retrieve user list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserChat>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to retrieve user list", Toast.LENGTH_SHORT).show();
            }
        });

    }
}