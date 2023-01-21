package com.example.healthappttt.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.User;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.UserAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private FirebaseFirestore firebaseFirestore;
    private UserAdapter userAdapter;
    private ArrayList<User> userList;
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


//        firebaseFirestore = FirebaseFirestore.getInstance();
//        userList = new ArrayList<>();
//        userAdapter = new UserAdapter(getActivity(), userList);
//
//        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
//
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(userAdapter);
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//                int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
//
//                if(newState == 1 && firstVisibleItemPosition == 0){
//                    topScrolled = true;
//                }
//                if(newState == 0 && topScrolled){
//                    //postsUpdate(true);
//                    topScrolled = false;
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
//                super.onScrolled(recyclerView, dx, dy);
//
//                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//                int visibleItemCount = layoutManager.getChildCount();
//                int totalItemCount = layoutManager.getItemCount();
//                int firstVisibleItemPosition = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();
//                int lastVisibleItemPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
//
//                if(totalItemCount - 3 <= lastVisibleItemPosition && !updating){
//                    //postsUpdate(false);
//                }
//
//                if(0 < firstVisibleItemPosition){
//                    topScrolled = false;
//                }
//            }
//        });
//
//        //postsUpdate(false);

        return view;

    }

//    private void postsUpdate(final boolean clear) {
//        updating = true;
//        //Date date = userList.size() == 0 || clear ? new Date() : userList.get(userList.size() - 1).getCreatedAt();
//        CollectionReference collectionReference = firebaseFirestore.collection("users");
//        collectionReference.get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            if(clear){
//                                userList.clear();
//                            }
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
////                               ***** userList.add(new User()); DB에서 각 값들긇어와서 넣어주기.
//                            }
//                            userAdapter.notifyDataSetChanged();
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                        updating = false;
//                    }
//                });
//    }

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
}