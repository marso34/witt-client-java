package com.example.healthappttt.adapter;//package com.example.healthappttt.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.healthappttt.Activity.ProfileActivity;
import com.example.healthappttt.Data.User;
import com.example.healthappttt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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

    static class MainViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        public TextView Name ;
        public TextView LocaName ;
        public ImageView photoImageVIew;
        public TextView PreferredTime;
        public RecyclerView recyclerView;

        public AreaAdapter Adapter;
        public ArrayList<String> ExerciseNames;
        MainViewHolder(@NonNull View itemView) {
            super(itemView);
            Name =  itemView.findViewById(R.id.UNE);
             LocaName = itemView.findViewById(R.id.GT);
           photoImageVIew = itemView.findViewById(R.id.PRI);
            PreferredTime = itemView.findViewById(R.id.GoodTime);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            ExerciseNames = new ArrayList<String>();
            Adapter = new AreaAdapter(mContext, ExerciseNames);
        }
    }

    public UserAdapter(Context mContext, ArrayList<User> myDataset) {
        this.mDataset = myDataset;
        this.mContext = mContext;
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
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(holder.Adapter);
        User userInfo = mDataset.get(position);
        getCurrentWeek();
            db.collection("routines").document(userInfo.getKey_() +"_" + dayOfWeek).get().
            addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        holder.ExerciseNames.clear();
                        DocumentSnapshot document = task.getResult();
                        holder.PreferredTime.setText(document.get("startTime").toString() +" ~ "+document.get("endTime").toString());
                        Integer exerciseCat = Integer.parseInt(document.get("exerciseCategories").toString());
                            Log.d(TAG, "Document exists!");
                            if ((exerciseCat & 0x1) == 0x1) {
                                String a = "가슴";
                                    holder.ExerciseNames.add(a);
                            }
                            if ((exerciseCat & 0x2) ==    0x2) {
                                String a = "등";
                                holder.ExerciseNames.add(a);
                            }
                            if ((exerciseCat & 0x4) == 0x4) {
                                String a = "어깨";
                                holder.ExerciseNames.add(a);
                            }
                            if ((exerciseCat & 0x8) == 0x8) {
                                String a = "하체";
                                holder.ExerciseNames.add(a);
                            }
                            if ((exerciseCat & 0x10) == 0x10) {
                                String a = "팔";
                                holder.ExerciseNames.add(a);
                            }
                            if ((exerciseCat & 0x20) == 0x20) {
                                String a = "복근";
                                holder.ExerciseNames.add(a);
                            }
                            if ((exerciseCat & 0x40) == 0x40) {
                                String a = "유산소";
                                holder.ExerciseNames.add(a);
                            }

                    }
                    holder.Adapter.notifyDataSetChanged();
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
                    Glide.with(mContext).load(finalProfilefile).into(holder.photoImageVIew);
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
        holder.Name.setText(userInfo.getUserName().toString());
        holder.LocaName.setText(userInfo.getDistance().toString() + "Km");
       ;
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