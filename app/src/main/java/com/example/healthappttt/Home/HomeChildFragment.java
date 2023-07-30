package com.example.healthappttt.Home;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.healthappttt.Data.Exercise.RoutineData;
import com.example.healthappttt.Data.PreferenceHelper;
import com.example.healthappttt.Data.User.CompareUser;
import com.example.healthappttt.Data.User.NearUsersData;
import com.example.healthappttt.Data.RetrofitClient;
import com.example.healthappttt.Data.UserInfo;
import com.example.healthappttt.MainActivity;
import com.example.healthappttt.Routine.RoutineChildFragment;
import com.example.healthappttt.R;
import com.example.healthappttt.User.UserAdapter;
import com.example.healthappttt.databinding.FragmentHomeChildBinding;
import com.example.healthappttt.interface_.ServiceApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoutineChildFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeChildFragment extends Fragment {
    FragmentHomeChildBinding binding;

    private static final int REQUEST_CODE = 100;

    private UserAdapter adapter;


    private boolean topScrolled;
    private ArrayList<CompareUser> CompareuserList;
    private List<UserInfo> UserList;
    private boolean updating = false;
    private UserInfo CurrentUser;
    private String UserUid;
    private PreferenceHelper preferenceHelper;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DAY_OF_WEEK = "dayOfWeek";
    private static final String ARG_ROUTINE = "routine";

    // TODO: Rename and change types of parameters

    private int dayOfWeek;
    private RoutineData routine;

    public HomeChildFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param dayOfWeek Parameter 1.
     * @param routine Parameter 2.
     * @return A new instance of fragment RoutineChildFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeChildFragment newInstance(int dayOfWeek, RoutineData routine) {
        HomeChildFragment fragment = new HomeChildFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DAY_OF_WEEK, dayOfWeek);
        args.putSerializable(ARG_ROUTINE, routine);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dayOfWeek = getArguments().getInt(ARG_DAY_OF_WEEK);
            routine = (RoutineData) getArguments().getSerializable(ARG_ROUTINE);
        }

        preferenceHelper = new PreferenceHelper("UserTB", getContext());
        UserList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeChildBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView.LayoutManager layoutManager = binding.recyclerView.getLayoutManager();
                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
                        int lastVisibleItemPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();

                        if(totalItemCount - 3 <= lastVisibleItemPosition && !updating){
                            postsUpdate(true);
                        }
//
                        if(0 < firstVisibleItemPosition){
                            topScrolled = false;
                        }
                        binding.swipelayout.setRefreshing(false);
                    }
                }, 500);
            }
        });

        binding.moveRoutine.setOnClickListener(v -> {
            ((MainActivity)requireActivity()).goToRoutine(dayOfWeek);
        });

        if (routine != null) {
            binding.emptylayout.setVisibility(View.GONE);
            binding.swipelayout.setVisibility(View.VISIBLE);
            setRecyclerView();

            {
                RecyclerView.LayoutManager layoutManager = binding.recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
                int lastVisibleItemPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
                if(totalItemCount < 1 && !updating){
                    postsUpdate(false);
                }
            }
        } else {
            binding.emptylayout.setVisibility(View.VISIBLE);
            binding.swipelayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    private void setRecyclerView() {
        adapter = new UserAdapter(getContext(), (ArrayList<UserInfo>) UserList, dayOfWeek); // 나중에 routine
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }



    private void getUserData() {
        ServiceApi apiInterface = RetrofitClient.getClient().create(ServiceApi.class);
        NearUsersData NR = new NearUsersData(dayOfWeek, preferenceHelper.getPK());//현재 내 유저 정보 보내서 내껀 안나오게함.
        Call<List<UserInfo>> call = apiInterface.GetNearUsers(NR);
        call.enqueue(new Callback<List<UserInfo>>() {
            @Override
            public void onResponse(Call<List<UserInfo>> call, Response<List<UserInfo>> response) {
                if (response.isSuccessful()) {
                    Log.d("Response", "Successful");

                    Log.d("pk", response.body() + "");


                    UserList.clear();
                    for (UserInfo value : response.body()) {
                        UserList.add(value);

                        Log.d("이름", value.getName());
                        Log.d("타임", value.getTime() + "");
                        Log.d("키", String.valueOf(value.getUserKey()));
                        Log.d("헬스장", value.getGymName());
                        Log.d("날짜", String.valueOf(value.getDayOfWeek()));
                        Log.d("부위",String.valueOf(value.getRoutineCategory()));
                    }
                 adapter.notifyDataSetChanged();
                } else {
                    Log.d("Response", "Unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<List<UserInfo>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
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
        //
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
}

