package com.example.healthappttt.Fragment;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.healthappttt.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;






import android.content.Context;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.CompareUser;
import com.example.healthappttt.Data.CompareUser;
import com.example.healthappttt.Data.User;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.UserAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static boolean pivotList[] = null;
    private static final String TAG = "HomeFragment";
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private UserAdapter userAdapter;
    private ArrayList<User> userList;
    private ArrayList<CompareUser> CompareuserList;
    private boolean updating;
    private boolean topScrolled;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //fragment_main에 인플레이션을 함

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();// 파이어베이스의 auth기능의 접근 권한을 갖는변수
        userList = new ArrayList<User>();
        userAdapter = new UserAdapter(getActivity(), userList);
        CompareuserList = new ArrayList<>();
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(userAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();

                if(newState == 1 && firstVisibleItemPosition == 0){
                    topScrolled = true;
                }

                if(newState == 0 && topScrolled){
                    //postsUpdate(true);
                    topScrolled = false;
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);

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
            }
        });

        postsUpdate(false);

        return view;

    }

    private void postsUpdate(final boolean clear) {
        updating = true;
        //Date date = userList.size() == 0 || clear ? new Date() : userList.get(userList.size() - 1).getCreatedAt();
        CollectionReference collectionReference = firebaseFirestore.collection("users");
        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        User currentUser = null;
                        if (task.isSuccessful()) {
                            if(clear){
                                userList.clear();
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                               // Log.d(TAG, document.getId() + " &&&&+&=> " + document.getData().get("userName").toString());
                               User a= new User(
                                       Double.parseDouble(document.getData().get("userTemperature").toString()),
                                        document.getData().get("key").toString(),
                                        Double.parseDouble(document.getData().get("lat").toString()),
                                        Double.parseDouble(document.getData().get("lon").toString()),
                                        document.getData().get("GoodTime").toString(),
                                        document.getData().get("userName").toString(),
                                        document.getData().get("profileImg").toString(),
                                        document.getData().get("bench").toString(),
                                        document.getData().get("deadlift").toString(),
                                        document.getData().get("squat").toString(),
                                        document.getData().get("locationName").toString()
                                        );
                                userList.add(a);
                                if(a.getKey() != mAuth.getCurrentUser().getUid()) currentUser = a;
                            }
                            //퀵정렬 편집해서 만드는건 가능한데 일단 보류 난이도가 높음.
                            //
                            ArrayList<Double> distans = new ArrayList<Double>();
                            for(int i=0;i<userList.size();++i){
                                if(userList.get(i).getKey() == currentUser.getKey()) {
                                    distans.add(0.0);
                                    userList.get(i).setDistance(0.0);
                                }
                                else {
                                    Double dis = DistanceByDegreeAndroid(currentUser.getLat(),currentUser.getLon(),userList.get(i).getLat(),userList.get(i).getLon());
                                    distans.add(dis);
                                    userList.get(i).setDistance(dis);
                                }
                            }
                            for(int i=0;i<userList.size();++i){
                                CompareuserList.add(new CompareUser(userList.get(i),distans.get(i)));
                            }
                            Collections.sort(CompareuserList);
                            for(int i = 0; i < CompareuserList.size();++i)
                                Log.d("list" , CompareuserList.get(i).getDistance().toString());
                            userList.clear();
                            for(int i=0;i<CompareuserList.size();++i){
                                userList.add(CompareuserList.get(i).getUser());
                            }
                            CompareuserList.clear();
                            userAdapter.notifyDataSetChanged();
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        updating = false;
                    }
                });
    }

    public static String getCurrentWeek() {
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        int dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK);
        String day = null;
        switch (dayOfWeekNumber) {
            case 1 :
                day = "일요일";
                break;
            case 2 :
                day = "월요일";
                break;
            case 3 :
                day = "화요일";
                break;
            case 4 :
                day = "수요일";
                break;
            case 5 :
                day = "목요일";
                break;
            case 6 :
                day = "금요일";
                break;
            case 7 :
                day = "토요일";
                break;
        }
        return day;
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