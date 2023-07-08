package com.example.healthappttt.User;//package com.example.healthappttt.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.UserInfo;
import com.example.healthappttt.Profile.MyProfileActivity;
import com.example.healthappttt.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MainViewHolder> {
    private ArrayList<UserInfo> mDataset;
    private FirebaseStorage storage;
    private UserInfo userInfo;
    private Context mContext;
    private UserInfo thisUser;
    private String dayOfWeek;
    FirebaseFirestore db;

    static class MainViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        public TextView Name ;
        public TextView LocaName ;
        public ImageView photoImageVIew;
        public TextView PreferredTime;
        public RecyclerView recyclerView;
        public LinearLayout UserLayout;
        public AreaAdapter Adapter;

        public ArrayList<String> ExerciseNames;

        MainViewHolder(@NonNull View itemView) {
            super(itemView);
            Name =  itemView.findViewById(R.id.UNE);
             LocaName = itemView.findViewById(R.id.GT);
           photoImageVIew = itemView.findViewById(R.id.PRI);
            PreferredTime = itemView.findViewById(R.id.GoodTime);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            ExerciseNames = new ArrayList<>();
            Adapter = new AreaAdapter(mContext,ExerciseNames);

           }
    }

    public UserAdapter(Context mContext, ArrayList<UserInfo> myDataset) {
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

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {

        UserInfo userInfo = mDataset.get(position);
        Log.d("유저 이름!", userInfo.getName());

        getCurrentWeek();
//
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MyProfileActivity.class);
                intent.putExtra("UserKey",userInfo.getUserKey());
//                intent.putExtra("post",finalProfilefile);
                mContext.startActivity(intent);
            }
        });

      //  Log.d(TAG, "onBindViewHolder: "+ userInfo.getUserName().toString());
        holder.Name.setText(userInfo.getName().toString());
        holder.LocaName.setText(userInfo.getDistance().toString() + "Km");
        holder.PreferredTime.setText(userInfo.getStartTime()+" ~ "+userInfo.getEndTime());
        holder.ExerciseNames.clear();
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        holder.recyclerView.setAdapter(holder.Adapter);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(holder.Adapter);
        Integer exerciseCat = userInfo.getRoutineCategory();
                            Log.d("rrr", "Document exists!");
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
                    holder.Adapter.notifyDataSetChanged();

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