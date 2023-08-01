package com.gwnu.witt.Chat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
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

import com.gwnu.witt.R;
import com.gwnu.witt.Data.Chat.MSG;
import com.gwnu.witt.Data.Chat.SocketSingleton;
import com.gwnu.witt.Data.Chat.UserChat;
import com.gwnu.witt.Data.PreferenceHelper;
import com.gwnu.witt.Data.RetrofitClient;
import com.gwnu.witt.Data.SQLiteUtil;
import com.gwnu.witt.Data.pkData;
import com.gwnu.witt.MainActivity;
import com.gwnu.witt.User.UserListAdapter;
import com.gwnu.witt.interface_.ServiceApi;

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
    private View rootView;
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
            SocketSingleton.getInstance(getContext());
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            SocketSingleton.getInstance(getContext());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // 레이아웃을 inflate합니다.
        chatflag = false;
        rootView = inflater.inflate(R.layout.fragment_chatting, container, false);
        userList = new ArrayList<>();
        sqLiteUtil = SQLiteUtil.getInstance();
        sqLiteUtil.setEmptyDB();
        prefhelper = new PreferenceHelper(name_TB,getContext());
        socketSingleton = SocketSingleton.getInstance(getContext());
        socketSingleton.setChatFragment(this);
        // 리사이클러뷰를 초기화합니다.
        userlistRecyclerView = rootView.findViewById(R.id.recyclerView);
        userlistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 유저 목록을 가져와서 userList에 저장합니다.

        // 어댑터를 초기화하고 userList를 설정합니다.
        userListAdapter = new UserListAdapter(getContext(),userList);
        userlistRecyclerView.setAdapter(userListAdapter);
        // 어댑터의 아이템 클릭 리스너를 설정합니다.
        getUsersFromServer(getContext());

        View mh = rootView.findViewById(R.id.moveHome);
        mh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)requireActivity()).goToHome();
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUsersFromServer(getContext() );
        // Fragment가 화면에 보여질 때 동작할 코드 작성
        // 예: 데이터를 업데이트하거나 화면 갱신
        // 예: 필요한 작업 수행 등
    }
    public void getLastMSG(String chatRoomId, String userKey, Context c) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(c != null){
                    MSG m = sqLiteUtil.selectLastMsg(c,chatRoomId, userKey, 2);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                for (UserChat uc : userList) {
                                    if (uc.getChatRoomId() == Integer.parseInt(chatRoomId)) {
                                        uc.setLastChat(m.getMessage());
                                        uc.setLastChatTime(m.timestampString());
                                        userListAdapter.notifyDataSetChanged();
                                        break;
                                    }
                                }
                            }
                            finally {
                                chatflag = false;
                            }

                        }

                    });
                }
            }
            // 메인 스레드로 UI 갱신 작업 보냄

        }).start();
    }
    // 서버에서 유저 목록을 가져오는 메소드입니다.
    public void getUsersFromServer(Context C) {// 이건 챗프레그먼트 켜질때문 부르도록 다른곳에있는 이거 전부 getLastMSG로 바꾸기
        if(chatflag == false && C != null) {
            try {
                ServiceApi apiService = RetrofitClient.getClient().create(ServiceApi.class);
                Log.d(TAG, "getUsersFromServer: " + String.valueOf(prefhelper.getPK()));
                Call<List<UserChat>> call = apiService.getUsers(new pkData(prefhelper.getPK())); // 유저키 얻어와서 넣기
                call.enqueue(new Callback<List<UserChat>>() {
                    @Override
                    public void onResponse(Call<List<UserChat>> call, Response<List<UserChat>> response) {
                        if (response.isSuccessful()) {
                            List<UserChat> users = response.body();
                            for (UserChat u : users) Log.d(TAG, "onResponse: uuu" + u.getUserNM());
                            if (C != null) {
                                sqLiteUtil.deleteChatRooms(C);
                                sqLiteUtil.insert(C, prefhelper.getPK(), users);
                            }
                            if (rootView != null) {
                                if (!users.isEmpty()) {
                                    rootView.findViewById(R.id.emptylayout).setVisibility(View.GONE);
                                    rootView.findViewById(R.id.userListLayout).setVisibility(View.VISIBLE);
                                } else {
                                    rootView.findViewById(R.id.emptylayout).setVisibility(View.VISIBLE);
                                    rootView.findViewById(R.id.userListLayout).setVisibility(View.GONE);
                                }
                            }

                        } else {
                            Toast.makeText(getContext(), "Failed to retrieve user list", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<UserChat>> call, Throwable t) {
                        Toast.makeText(getContext(), "Failed to retrieve user list", Toast.LENGTH_SHORT).show();
                    }
                });
            }finally {
                try {
                    List<UserChat> users = sqLiteUtil.selectChatRoom(C,String.valueOf(prefhelper.getPK()));
                    userList.clear();
                    userList.addAll(users);
                    userListAdapter.notifyDataSetChanged();
                } finally {
                    chatflag = false;
                }
            }
        }




    }
}

