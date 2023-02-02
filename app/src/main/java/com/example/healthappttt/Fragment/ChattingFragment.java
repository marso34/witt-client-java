package com.example.healthappttt.Fragment;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthappttt.Data.Message;
import com.example.healthappttt.Data.User;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.MessageAdapter;
import com.example.healthappttt.adapter.UserListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class ChattingFragment extends Fragment {
    private RecyclerView recyclerView;

    private static final String TAG = "chat";
    private UserListAdapter userListAdapter;
    private ArrayList<User> userList;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;// 파이어베이스 유저관련 접속하기위한 변수

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_chatting, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userList = new ArrayList<User>();
        recyclerView = view.findViewById(R.id.userlistRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();

        userListAdapter = new UserListAdapter(getContext(), userList);
        recyclerView.setAdapter(userListAdapter);



        //Date date = userList.size() == 0 || clear ? new Date() : userList.get(userList.size() - 1).getCreatedAt();
        CollectionReference collectionReference = firebaseFirestore.collection("users");
        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        User currentUser = null;
                        if (task.isSuccessful()) {
                                userList.clear();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                               //Log.d(TAG, document.getId() + " &&&&+&=> " + document.getData().get("userName").toString());
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

                                if(!a.getKey().equals( mAuth.getCurrentUser().getUid()))
                                    userList.add(a);
                            }

                            userListAdapter.notifyDataSetChanged();

                        } else {
                           // Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });


        return view;
    }

}