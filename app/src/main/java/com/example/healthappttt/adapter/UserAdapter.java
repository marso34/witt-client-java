package com.example.healthappttt.adapter;//package com.example.healthappttt.adapter;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.healthappttt.Activity.ProfileActivity;
import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.Data.User;
import com.example.healthappttt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MainViewHolder> {
    private ArrayList<User> mDataset;
    private FirebaseStorage storage;
    private User userInfo;
    private Context mContext;
    private User thisUser;
    private String dayOfWeek;
    FirebaseFirestore db;
    private AreaAdapter Adapter;
    private ArrayList<String> ExerciseNames;
    static class MainViewHolder extends RecyclerView.ViewHolder {
        View cardView;
        MainViewHolder(View v) {
            super((View) v);
            cardView = v;
        }
    }

    public UserAdapter(Activity activity, ArrayList<User> myDataset) {
        this.mDataset = myDataset;
        this.mContext = activity;
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    @NonNull
    @Override
    public UserAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cardView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //클릭됐을시 행동 여기에 적으면 돼네.....시벌...
            }
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {
        storage = FirebaseStorage.getInstance();
        db= FirebaseFirestore.getInstance();
        View cardView = holder.cardView;
        TextView Name =  cardView.findViewById(R.id.UNE);
        TextView LocaName = cardView.findViewById(R.id.GT);
        ImageView photoImageVIew = cardView.findViewById(R.id.PRI);
        TextView PreferredTime = cardView.findViewById(R.id.GoodTime);
        final RecyclerView ExerciseArea = cardView.findViewById(R.id.recyclerView);
        ExerciseNames = new ArrayList<>();
        Adapter = new AreaAdapter(mContext, ExerciseNames);
        ExerciseArea.setHasFixedSize(true);
        ExerciseArea.setLayoutManager(new LinearLayoutManager(mContext));
        ExerciseArea.setAdapter(Adapter);
        ExerciseArea.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        User userInfo = mDataset.get(position);
        getCurrentWeek();

            db.collection("routines").document(userInfo.getKey_() +"_" + dayOfWeek).get().
            addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        Integer exerciseCat = Integer.parseInt(document.get("exerciseCategories").toString());
                        if (document.exists()) {
                            Log.d(TAG, "Document exists!");
                            if ((exerciseCat & 0x1) == 0x1) {
                                String a = "가슴";
                                    ExerciseNames.add(a);
                            }
                            if ((exerciseCat & 0x2) == 0x2) {
                                String a = "등";
                                ExerciseNames.add(a);
                            }
                            if ((exerciseCat & 0x4) == 0x4) {
                                String a = "어깨";
                                ExerciseNames.add(a);
                            }
                            if ((exerciseCat & 0x8) == 0x8) {
                                String a = "하체";
                                ExerciseNames.add(a);
                            }
                            if ((exerciseCat & 0x10) == 0x10) {
                                String a = "팔";
                                ExerciseNames.add(a);
                            }
                            if ((exerciseCat & 0x20) == 0x20) {
                                String a = "복근";
                                ExerciseNames.add(a);
                            }
                            if ((exerciseCat & 0x40) == 0x40) {
                                String a = "유산소";
                                ExerciseNames.add(a);
                            }

                        } else {
                            Log.d(TAG, "Document does not exist!");
                        }
                    } else {
                        Log.d(TAG, "Failed with: ", task.getException());
                    }
                    Adapter.notifyDataSetChanged();
                }
            });

        String fileName = userInfo.getKey_();

        File profilefile = null;

        try {
            profilefile = File.createTempFile("images","jpeg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        StorageReference sref  = storage.getReference().child("article/photo").child(fileName);
        File finalProfilefile = profilefile;
        sref.getFile(profilefile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Local temp file has been created
                    Glide.with(mContext).load(finalProfilefile).into(photoImageVIew);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra("User",userInfo);
                intent.putExtra("post",finalProfilefile);
                mContext.startActivity(intent);
            }
        });

      //  Log.d(TAG, "onBindViewHolder: "+ userInfo.getUserName().toString());
        Name.setText(userInfo.getUserName().toString());
        LocaName.setText(userInfo.getDistance().toString());
        PreferredTime.setText("11~13");
    }
    public void getCurrentWeek() {
        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        switch (calendar.get(Calendar.DAY_OF_WEEK) - 1) {
            case 0:
                dayOfWeek = "sun"; break; // 일
            case 1:
                dayOfWeek = "mon";break; // 월
            case 2:
                dayOfWeek = "tue";break; // 화
            case 3:
                dayOfWeek = "wed"; break; // 수
            case 4:
                dayOfWeek = "thu";break; // 목
            case 5:
                dayOfWeek = "fri";break; // 금
            case 6:
                dayOfWeek = "sat";break; // 토
        }
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}