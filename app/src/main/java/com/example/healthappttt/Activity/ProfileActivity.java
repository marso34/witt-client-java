package com.example.healthappttt.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.Message;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.Data.User;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.setExerciseAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.appindexing.builders.StickerBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.Temperature;

import java.util.ArrayList;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {
    private Button wittBtn;
    private ImageView ProImg;
    private String ThisProfileUid;
    private TextView ThisProfileName;
    private TextView LocationName;
    private TextView ThisProfileTemperature;
    private TextView Squat;
    private TextView Bench;
    private TextView Dead;

    private Routine routine;
    private ArrayList<Exercise> exercises;
    private setExerciseAdapter adapter;
    private String dayOfWeek;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private DatabaseReference mDbRef;
    String time = "";
    Intent intent;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ProImg = findViewById(R.id.UserImg);
        mAuth = FirebaseAuth.getInstance();// 파이어베이스의 auth기능의 접근 권한을 갖는변수
        mDbRef = FirebaseDatabase.getInstance().getReference(); //Firebase에 데이터를 추가하거나 조회하기 위한 코드, 정의

        ThisProfileName = findViewById(R.id.UserName);
        LocationName = findViewById(R.id.MyLocation);
        ThisProfileTemperature = findViewById(R.id.MyTempreture);
        Squat = findViewById(R.id.squat);
        Bench = findViewById(R.id.benchpress);
        Dead = findViewById(R.id.deadlift);
        wittBtn = findViewById(R.id.WittBtn);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        intent = getIntent();
        firebaseFirestore = FirebaseFirestore.getInstance();
        User U = (User) getIntent().getSerializableExtra("User");//포스트인포 객체 만들어서 할당.;
        File f =  (File) getIntent().getSerializableExtra("post");
        ThisProfileUid = U.getKey_();
        ThisProfileName.setText(U.getUserName());
        LocationName.setText(U.getLocationName());
        Integer A = U.getUserTemperature().intValue();
        ThisProfileTemperature.setText(A.toString());
        Squat.setText(U.getSquat());
        Bench.setText(U.getBench());
        Dead.setText(U.getDeadlift());

        exercises = new ArrayList<>();
        getCurrentWeek(); // 요일
        setExercises();
        Glide.with(this).load(f).into(ProImg);
        //준이가 짤 코드 요일 알아내서 루틴 테이블에서 운동들 가져와서 리사이클로뷰에 넣기.

        ProImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myStartActivity(enlargementActivity.class,f);
            }
        });
        wittBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //thisUser(눌린 유저)의 witt테이블에 doucument 제목으로 current유저(즉 바로나의 uid)저장
                //witttable에 필드로 현재 시간,int connectFlag 0 저장
                String message = "Witt!!";
                if(!message.isEmpty()){
                    time = getTime();
                    Log.i(ContentValues.TAG,message);
                    Message messageObject = new Message(message,mAuth.getCurrentUser().getUid(),ThisProfileUid,time);
                    String senderRoom = mAuth.getCurrentUser().getUid() + ThisProfileUid;
                    mDbRef.child("chats").child(senderRoom).child("messages").push()
                            .setValue(messageObject).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    String receiverRoom =  ThisProfileUid + mAuth.getCurrentUser().getUid();
                                    //       Message messageObject = new Message(message,receiverUid ,senderUid);
                                    mDbRef.child("chats").child(receiverRoom).child("messages").push()
                                            .setValue(messageObject);
                                }
                            });
                    myStartActivity(ChatActivity.class,U);

                }
                else
                    return;
            }
        });
    }


    private void getCurrentWeek() {
        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1: dayOfWeek = "sun"; break;
            case 2: dayOfWeek = "mon"; break;
            case 3: dayOfWeek = "tue"; break;
            case 4: dayOfWeek = "wed"; break;
            case 5: dayOfWeek = "thu"; break;
            case 6: dayOfWeek = "fri"; break;
            case 7: dayOfWeek = "sat"; break;
        }

        Log.d("오늘", dayOfWeek);
    }

    private void createRoutine() {
        firebaseFirestore.collection("routines").document(ThisProfileUid +"_" + dayOfWeek).
                get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "Document exists!");
                                routine = new Routine(
                                        document.getData().get("title").toString(),
                                        Integer.parseInt(document.getData().get("exerciseCategories").toString()),
                                        document.getData().get("startTime").toString(),
                                        document.getData().get("endTime").toString()
                                );

                                setExercises();

                            } else {
                                Log.d(TAG, "Document does not exist!");
                            }
                        } else {
                            Log.d(TAG, "Failed with: ", task.getException());
                        }
                    }
                });
    } // DB 연동 전까지 사용할 루틴 만드는 메서드 -> 추후 수정 필요

    private void setExercises() {

        firebaseFirestore.collection("routines").document(ThisProfileUid +"_" + dayOfWeek).
                collection("exercises").
                get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Exercise e = new Exercise(
                                        document.getData().get("title").toString(),
                                        document.getData().get("state").toString(),
                                        Integer.parseInt(document.getData().get("count").toString()),
                                        Integer.parseInt(document.getData().get("volume").toString())
                                );

                                exercises.add(e);
                            }

                            setRecyclerView();

                        } else {

                        }
                    }
                });
    }

    private void setRecyclerView() {
        adapter = new setExerciseAdapter(exercises); // 나중에 routine
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    private void myStartActivity(Class c,File f) {// loginactivity페이지에서 mainactivity페이지로 넘기는 코드
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("post",f);
        startActivity(intent);
    }
    private String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        String getTime = dateFormat.format(date);

        return getTime;
    }
    private void myStartActivity(Class c,User u) {// loginactivity페이지에서 mainactivity페이지로 넘기는 코드
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("userId",u.getKey_());
        intent.putExtra("username",u.getUserName());
        startActivity(intent);
    }
}