package com.example.healthappttt.Fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthappttt.Data.User;
import com.example.healthappttt.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

        //CollectionReference -> 파이어스토어의 컬랙션 참조하는 객체
        DocumentReference productRef = db.collection("users").document("FeXJyKuFvHM2crCAZRG3IgwKvG02");
        //get()을 통해서 해당 컬랙션의 정보를 가져옴



        //단일 문서의 내용 검색
        productRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if( document.exists()) { // 데이터가 존재할 경우
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

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