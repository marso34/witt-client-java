package com.example.healthappttt.Chat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Chat.MSG;
import com.example.healthappttt.Data.Chat.SocketSingleton;
import com.example.healthappttt.Data.Chat.UserChat;
import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.SQLiteUtil;
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
    private String name_TB = "UserTB";
    private SQLiteUtil sqLiteUtil;
    public boolean chatflag = false;
    private SocketSingleton socketSingleton;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ChattingFragment() {
    }

    public static ChattingFragment newInstance(String param1, String param2) {
        ChattingFragment fragment = new ChattingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 레이아웃을 inflate합니다.
        View view = inflater.inflate(R.layout.fragment_chatting, container, false);
        userList = new ArrayList<>();
        sqLiteUtil = SQLiteUtil.getInstance();
        sqLiteUtil.setEmptyDB();
        prefhelper = new PreferenceHelper(name_TB,getContext());
        socketSingleton = SocketSingleton.getInstance(getContext());
        socketSingleton.setChatFragment(this);
        // 리사이클러뷰를 초기화합니다.
        userlistRecyclerView = view.findViewById(R.id.recyclerView2);
        userlistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 유저 목록을 가져와서 userList에 저장합니다.

        // 어댑터를 초기화하고 userList를 설정합니다.
        userListAdapter = new UserListAdapter(getContext(),userList);
        userlistRecyclerView.setAdapter(userListAdapter);
        getUsersFromServer();
        // 어댑터의 아이템 클릭 리스너를 설정합니다.
        return view;
    }

    public void getLastMSG(String chatRoomId, String userKey) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sqLiteUtil.setInitView(getContext(), "CHAT_MSG_TB");
                MSG m = sqLiteUtil.selectLastMsg(chatRoomId, userKey, 2);

                // 메인 스레드로 UI 갱신 작업 보냄
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (UserChat uc : userList) {
                            if (uc.getChatRoomId().equals(chatRoomId)) {
                                uc.setLastChat(m.getMessage());
                                uc.setLastChatTime(m.timestampString());
                                break;
                            }
                        }
                        userListAdapter.notifyDataSetChanged();
                        chatflag = false;
                    }
                });
            }
        }).start();
    }
    // 서버에서 유저 목록을 가져오는 메소드입니다.
    public void getUsersFromServer() {// 이건 챗프레그먼트 켜질때문 부르도록 다른곳에있는 이거 전부 getLastMSG로 바꾸기
        // 서버로부터 유저 목록을 가져와서 List<UserChat>로 반환합니다.
        ServiceApi apiService = RetrofitClient.getClient().create(ServiceApi.class);
        Log.d(TAG, "getUsersFromServer: "+String.valueOf(prefhelper.getPK()));
        Call<List<UserChat>> call = apiService.getUsers(new UserKey(prefhelper.getPK())); // 유저키 얻어와서 넣기
        call.enqueue(new Callback<List<UserChat>>() {
            @Override
            public void onResponse(Call<List<UserChat>> call, Response<List<UserChat>> response) {
                if (response.isSuccessful()) {

                    List<UserChat> users = response.body();
                    sqLiteUtil.setInitView(getContext(),"CHAT_ROOM_TB");
                    sqLiteUtil.deleteChatRoom();
                    sqLiteUtil.setInitView(getContext(),"CHAT_ROOM_TB");
                    sqLiteUtil.insert(prefhelper.getPK(),users);

                    // userList에 데이터가 추가된 후에 실행되어야 하는 로직을 여기에 작성합니다.
                    userList.clear();
                    userList.addAll(users);
                    userListAdapter.notifyDataSetChanged();
                    chatflag = false;

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

