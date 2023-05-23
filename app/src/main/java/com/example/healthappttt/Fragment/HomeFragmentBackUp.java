//package com.example.healthappttt.Fragment;
//
//
//import android.location.Location;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.DividerItemDecoration;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//
//import com.example.healthappttt.Data.CompareUser;
//import com.example.healthappttt.Data.UserInfo;
//import com.example.healthappttt.R;
//import com.example.healthappttt.adapter.UserAdapter;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Date;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link HomeFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class HomeFragmentBackUp extends Fragment {
//
//    private static boolean pivotList[] = null;
//    private static final String TAG = "HomeFragment";
//    private FirebaseAuth mAuth;
//    private FirebaseFirestore firebaseFirestore;
//
//    private UserAdapter userAdapter;
//    private ArrayList<UserInfo> userList;
//    private ArrayList<CompareUser> CompareuserList;
//    private boolean updating;
//    private boolean topScrolled;
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public HomeFragmentBackUp() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * th-is fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment HomeFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static HomeFragment newInstance(String param1, String param2) {
//        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        //fragment_main에 인플레이션을 함
//        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
//        firebaseFirestore = FirebaseFirestore.getInstance();
//        mAuth = FirebaseAuth.getInstance();// 파이어베이스의 auth기능의 접근 권한을 갖는변수
//        userList = new ArrayList<UserInfo>();
//        userAdapter = new UserAdapter(getActivity(), userList);
//        CompareuserList = new ArrayList<>();
//        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView2);
//        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(userAdapter);
//
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
//
//                        if(0 < firstVisibleItemPosition){
//                            topScrolled = false;
//                        }
//                        mSwipeRefreshLayout.setRefreshing(false);
//                    }
//                }, 500);
//
//            }
//        });
//        postsUpdate(false);
//
//        return view;
//
//    }
//
//    private void postsUpdate(final boolean clear) {
//        updating = true;
//        //Date date = userList.size() == 0 || clear ? new Date() : userList.get(userList.size() - 1).getCreatedAt();
//        CollectionReference collectionReference = firebaseFirestore.collection("users");
//        collectionReference.get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        UserInfo currentUser = null;
//                        if (task.isSuccessful()) {
//                            if(clear){
//                                userList.clear();
//                            }
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                // Log.d(TAG, document.getId() + " &&&&+&=> " + document.getData().get("userName").toString());
////                                UserInfo a= new UserInfo(
////                                        (int) Double.parseDouble(document.getData().get("userTemperature").toString()),
////                                        document.getData().get("key_").toString(),
////                                        Double.parseDouble(document.getData().get("lat").toString()),
////                                        Double.parseDouble(document.getData().get("lon").toString()),
////                                        document.getData().get("GoodTime").toString(),
////                                        document.getData().get("userName").toString(),
////                                        document.getData().get("profileImg").toString(),
////                                        document.getData().get("bench").toString(),
////                                        document.getData().get("deadlift").toString(),
////                                        document.getData().get("squat").toString(),
////                                        document.getData().get("locationName").toString()
////                                );
//
//                                if(a.getKey_().equals(mAuth.getCurrentUser().getUid())) currentUser = a;
//                                else userList.add(a);
//                            }
//                            //퀵정렬 편집해서 만드는건 가능한데 일단 보류 난이도가 높음.
//                            //
//                            ArrayList<Integer> distans = new ArrayList<Integer>();
//                            for(int i=0;i<userList.size();++i){
//                                if(userList.get(i).getKey_() == currentUser.getKey_()) {
//                                    distans.add(0);
//                                    userList.get(i).setDistance(0);
//                                }
//                                else {
//                                    int a = 0;
//                                    Double dis = DistanceByDegreeAndroid(currentUser.getLat(),currentUser.getLon(),userList.get(i).getLat(),userList.get(i).getLon());
//                                    if(dis > 0.0) {
//                                        a = (int)Math.round(dis/1000);
//                                    }
//
//
//                                    distans.add(a);
//
//                                    userList.get(i).setDistance(a);
//                                }
//                            }
//                            for(int i=0;i<userList.size();++i){
//                                CompareuserList.add(new CompareUser(userList.get(i),distans.get(i)));
//                            }
//                            Collections.sort(CompareuserList);
//                            for(int i = 0; i < CompareuserList.size();++i)
//                                Log.d("list" , CompareuserList.get(i).getDistance().toString());
//                            userList.clear();
//                            for(int i=0;i<CompareuserList.size();++i){
//                                userList.add(CompareuserList.get(i).getUser());
//                            }
//                            CompareuserList.clear();
//                            userAdapter.notifyDataSetChanged();
//                        } else {
//                            //Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                        updating = false;
//                    }
//                });
//    }
//
//    public static String getCurrentWeek() {
//        Date currentDate = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(currentDate);
//        int dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK);
//        String day = null;
//        switch (dayOfWeekNumber) {
//            case 1 :
//                day = "일요일";
//                break;
//            case 2 :
//                day = "월요일";
//                break;
//            case 3 :
//                day = "화요일";
//                break;
//            case 4 :
//                day = "수요일";
//                break;
//            case 5 :
//                day = "목요일";
//                break;
//            case 6 :
//                day = "금요일";
//                break;
//            case 7 :
//                day = "토요일";
//                break;
//        }
//        return day;
//    }
//
//    public double DistanceByDegreeAndroid(double _latitude1, double _longitude1, double _latitude2, double _longitude2){
//        Location startPos = new Location("PointA");
//        Location endPos = new Location("PointB");
//
//        startPos.setLatitude(_latitude1);
//        startPos.setLongitude(_longitude1);
//        endPos.setLatitude(_latitude2);
//        endPos.setLongitude(_longitude2);
//
//        double distance = startPos.distanceTo(endPos);
//
//        return distance;
//    }
//
//
//}