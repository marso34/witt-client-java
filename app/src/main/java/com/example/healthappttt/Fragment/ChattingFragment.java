package com.example.healthappttt.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Activity.ChatActivity;
import com.example.healthappttt.Data.User;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.UserListAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//package com.example.healthappttt.Fragment;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.healthappttt.Data.UserInfo;
//import com.example.healthappttt.R;
//import com.example.healthappttt.adapter.UserListAdapter;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.ArrayList;
//
//
public class ChattingFragment extends Fragment {
    private RecyclerView userlistRecyclerView;
    private UserListAdapter userListAdapter;
    private List<User> userList;

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
        userListAdapter = new UserListAdapter(userList);
        userlistRecyclerView.setAdapter(userListAdapter);

        // 어댑터의 아이템 클릭 리스너를 설정합니다.
        userListAdapter.setOnItemClickListener(new UserListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // 클릭된 유저의 정보를 얻어와서 ChatActivity로 전달합니다.
                User clickedUser = userList.get(position);
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("username", clickedUser.getUser_NM());
                startActivity(intent);
            }
        });



        return view;
    }

    // 서버에서 유저 목록을 가져오는 메소드입니다.
    private void getUsersFromServer() {
        // 서버로부터 유저 목록을 가져와서 List<User>로 반환합니다.
        ApiInterface apiService = ApiClient.getRetrofitInstance().create(ApiInterface.class);
        Call<List<User>> call = apiService.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    List<User> users = response.body();
                    userList.clear();
                    for (User user : users) {
                        User newUser = new User(user.getUser_NM());
                        userList.add(newUser);
                        Log.d("chat", newUser.getUser_NM());
                    }
                    userListAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Failed to retrieve user list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to retrieve user list", Toast.LENGTH_SHORT).show();
            }
        });
    }
}