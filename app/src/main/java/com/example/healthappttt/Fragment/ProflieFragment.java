package com.example.healthappttt.Fragment;

import static android.content.ContentValues.TAG;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideContext;
import com.example.healthappttt.Data.User;
import com.example.healthappttt.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProflieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProflieFragment extends Fragment {
    // 파이어스토어에 접근하기 위한 객체 생성
    private ArrayList<User> userList;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProflieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProflieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProflieFragment newInstance(String param1, String param2) {
        ProflieFragment fragment = new ProflieFragment();
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
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_proflie, container, false);
        // 아이디 연결
        FrameLayout ProfileFrame = view.findViewById(R.id.ProfileFrame);

        TextView mytempreture2 = ProfileFrame.findViewById(R.id.MyTempreture2);
        TextView username = ProfileFrame.findViewById(R.id.UserName);
        TextView locationname = ProfileFrame.findViewById(R.id.MyLocation);
        TextView squat = ProfileFrame.findViewById(R.id.squat);
        TextView bench = ProfileFrame.findViewById(R.id.benchpress);
        TextView deadlift = ProfileFrame.findViewById(R.id.deadlift);
        ImageView userImg = ProfileFrame.findViewById(R.id.UserImg);

        //CollectionReference -> 파이어스토어의 컬랙션 참조하는 객체
        DocumentReference productRef = db.collection("users").document("vE8rAti9AgYMKxGdZwQqBZX1qFv1");
        //get()을 통해서 해당 컬랙션의 정보를 가져옴

        //단일 문서의 내용 검색
        productRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if( document.exists()) { // 데이터가 존재할 경우
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        mytempreture2.setText(document.getData().get("userTemperature").toString());
                        username.setText(document.getData().get("userName").toString());
                        bench.setText(document.getData().get("bench").toString());
                        deadlift.setText(document.getData().get("deadlift").toString());
                        squat.setText(document.getData().get("squat").toString());
                        locationname.setText(document.getData().get("locationName").toString());

                        Glide.with(getView()).load(document.getData().get("profileImg")).override(100,100).into(userImg);


//                        userImg.setImageURI((Uri) document.getData().get("profileImg"));
//                      document.getData().get("key").toString();
//                      document.getData().get("GoodTime").toString();

                    }else {
                        Log.d(TAG, "No such document!");
                    }
                }else {
                    Log.d(TAG, "get failed with",task.getException());
                }
            }
        });


        return view;
    }
}