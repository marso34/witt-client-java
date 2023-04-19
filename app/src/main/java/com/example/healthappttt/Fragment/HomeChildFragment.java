package com.example.healthappttt.Fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.healthappttt.Data.CompareUser;
import com.example.healthappttt.Data.User;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoutineChildFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeChildFragment extends Fragment {
    private static final int REQUEST_CODE = 100;

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private CardView addRoutineBtn;

    private boolean topScrolled;
    private ArrayList<CompareUser> CompareuserList;
    private ArrayList<User> UserList;
    private int day_of_week;
    private boolean updating;
    private User CurrentUser;


    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;// 파이어베이스 유저관련 접속하기위한 변수
    private FirebaseFirestore db;
    private String UserUid;
    private String dayOfWeek = "";



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeChildFragment() {}

    public HomeChildFragment(int day_of_week) {
        this.day_of_week = day_of_week;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RoutineChildFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeChildFragment newInstance(String param1, String param2) {
        HomeChildFragment fragment = new HomeChildFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
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
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_home_child, container, false);
        recyclerView = view.findViewById(R.id.recyclerView2);
        addRoutineBtn = view.findViewById(R.id.addRoutine);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        UserUid = mAuth.getCurrentUser().getUid();

        UserList = new ArrayList<>();

        switch (day_of_week) {
            case 0: dayOfWeek = "sun"; break;
            case 1: dayOfWeek = "mon"; break;
            case 2: dayOfWeek = "tue"; break;
            case 3: dayOfWeek = "wed"; break;
            case 4: dayOfWeek = "thu"; break;
            case 5: dayOfWeek = "fri"; break;
            case 6: dayOfWeek = "sat"; break;
        }

        setRecyclerView();
        SwipeRefreshLayout mSwipeRefreshLayout = view.findViewById(R.id.swipe_layout);
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
                        int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
                        int lastVisibleItemPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();

                        if(totalItemCount - 3 <= lastVisibleItemPosition && !updating){
                            postsUpdate(true);
                        }

                        if(0 < firstVisibleItemPosition){
                            topScrolled = false;
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });
        postsUpdate(false);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && data != null) {
//            Routine r = data.getSerializableExtra("result");
            String result = (String) data.getSerializableExtra("result");
            //유저 추가부분//UserList.add(new User());
            adapter.notifyDataSetChanged();

            Log.d("Test", result);
        }
    } // startActivityForResult로 실행한 액티비티의 반환값을 전달받는 메서드

    private void setRecyclerView() {
        adapter = new UserAdapter(getContext(),UserList); // 나중에 routine
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

//        if (adapter != null) {
//            adapter.setOnExerciseClickListener(new setExerciseAdapter.OnExerciseClick() {
//                @Override
//                public void onExerciseClick(int postion) {
//                    deleteExercise(postion);
//                    adapter.removeItem(postion);
//                    adapter.notifyDataSetChanged();
//
////                    saveRoutine(routine.getExercises().get(postion));
//                }
//            });
//        }
    }

    private void GetUserData(){// 디비에서 유저들 정보 얻어오기
        ApiInterface apiInterface = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<String> call = apiInterface.GetNearUsers();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.e(TAG, "성공 : " + response.body());
                    parseJson(response.body());
                    //parseJson(response.body());// 유저 리스트에 파싱
                } else {
                    try {
                        Log.e(TAG, "실패 : " + response.errorBody().string());
                    }   catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "에러 : " + t.getMessage());
            }
        });
    }

    private void postsUpdate(final boolean clear) { // 스크롤되면 할 작업
        updating = true;
        //Date date = userList.size() == 0 || clear ? new Date() : userList.get(userList.size() - 1).getCreatedAt();
        GetUserData();

        //UserSolt(CurrentUser);
        //퀵정렬 편집해서 만드는건 가능한데 일단 보류 난이도가 높음.
        //
        updating = false;
        }



        public void UserSolt(User currentUser){// 유저 거리순 정렬.
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

    public double DistanceByDegreeAndroid(double _latitude1, double _longitude1, double _latitude2, double _longitude2){
        Location startPos = new Location("PointA");
        Location endPos = new Location("PointB");

        startPos.setLatitude(_latitude1);
        startPos.setLongitude(_longitude1);
        endPos.setLatitude(_latitude2);
        endPos.setLongitude(_longitude2);

        double distance = startPos.distanceTo(endPos);

        return distance;
    }
    private void parseJson(String json) { // 유저들 정보 js파일 userlist로 매핑 시키기.
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(json);
            Log.d("qwerasdf", jsonObject.toString());
            JSONArray UserArray = jsonObject.getJSONArray("UsersInfo");
            for(int i=0; i < UserArray.length(); ++i){
                JSONObject OBJ = UserArray.getJSONObject(i);
                User U = new User(
                        OBJ.getInt("USER_PK"),
                        OBJ.getString("User_NM"),
                        OBJ.getString("User_IMG"),
                        OBJ.getString("GYM_NM"),
                        OBJ.getDouble("User_Lat"),
                        OBJ.getDouble("User_Lon"),
                        OBJ.getString("Start_Time"),
                        OBJ.getString("End_Time"),
                        OBJ.getInt("CAT")
                );
                UserList.add(U);
                Log.d("유저들 이름!!!!",UserList.get(i).getName());
            }
//            if(a.getKey_().equals(mAuth.getCurrentUser().getUid())) currentUser = a;
//            else userList.add(a);

        }   catch (JSONException e) {
            e.printStackTrace();
        }


//        for (QueryDocumentSnapshot document : task.getResult()) {

            // Log.d(TAG, document.getId() + " &&&&+&=> " + document.getData().get("userName").toString());
//                User a= new User(
//
//                );

//                if(a.getKey_().equals(mAuth.getCurrentUser().getUid())) currentUser = a;
//                else UserList.add(a);
//            }

    }

}

