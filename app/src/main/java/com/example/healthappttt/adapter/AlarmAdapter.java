//package com.example.healthappttt.adapter;//package com.example.healthappttt.adapter;
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.healthappttt.Data.AlarmInfo;
//import com.example.healthappttt.Data.UserInfo;
//import com.example.healthappttt.R;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.storage.FirebaseStorage;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//
//public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.MainViewHolder> {
//    private ArrayList<AlarmInfo> mDataset;
//    private AlarmInfo alarmInfo;
//    private Context mContext;
//
//    static class MainViewHolder extends RecyclerView.ViewHolder {
//        private Context mContext;
//        public TextView CAT ;
//        public TextView Contents ;
//        public ImageView CAT_IMG;
//        public TextView time_;
//        public RecyclerView recyclerView;
//
//
//        MainViewHolder(@NonNull View itemView) {
//            super(itemView);
//            CAT =  itemView.findViewById(R.id.CAT);
//            Contents = itemView.findViewById(R.id.contents);
//            CAT_IMG = itemView.findViewById(R.id.CAT_IMG);
//            time_ = itemView.findViewById(R.id.beforetime);
//            recyclerView = itemView.findViewById(R.id.recyclerView);
//        }
//    }
//
//    public AlarmAdapter(Context mContext,ArrayList<AlarmInfo> myDataset) {
//        this.mContext = mContext;
//        this.mDataset = myDataset;
//    }
//
//
//    @Override
//    public int getItemViewType(int position){
//        return position;
//    }
//
//    @NonNull
//    @Override
//    public AlarmAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View cardView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_alram, parent, false);
//        final MainViewHolder mainViewHolder = new MainViewHolder(cardView);
//
//        return mainViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {
//         alarmInfo = mDataset.get(position);
//
////
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Intent intent = new Intent(mContext, ProfileActivity.class);
//                //intent.putExtra("UserInfo",userInfo);
////                intent.putExtra("post",finalProfilefile);
//                //mContext.startActivity(intent);
//            }
//        });
//        if(alarmInfo.getCat() == 0){
//
//        }
//        else if(alarmInfo.getCat() == 1){
//            //채워넣기.
//        }
//        else if(alarmInfo.getCat() == 2){
//            //채워넣기
//        }
//
//        //  Log.d(TAG, "onBindViewHolder: "+ userInfo.getUserName().toString());
//        holder.Name.setText(alarmInfo.getName().toString());
//        holder.LocaName.setText(userInfo.getDistance().toString() + "Km");
//        holder.PreferredTime.setText(userInfo.getStartTime()+" ~ "+userInfo.getEndTime());
//        holder.ExerciseNames.clear();
//        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        holder.recyclerView.setAdapter(holder.Adapter);
//        holder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
//        holder.recyclerView.setAdapter(holder.Adapter);
//        Integer exerciseCat = userInfo.getRoutineCategory();
//        Log.d("rrr", "Document exists!");
//        if ((exerciseCat & 0x1) == 0x1) {
//            String a = "가슴";
//            holder.ExerciseNames.add(a);
//        }
//        if ((exerciseCat & 0x2) ==    0x2) {
//            String a = "등";
//            holder.ExerciseNames.add(a);
//        }
//        if ((exerciseCat & 0x4) == 0x4) {
//            String a = "어깨";
//            holder.ExerciseNames.add(a);
//        }
//        if ((exerciseCat & 0x8) == 0x8) {
//            String a = "하체";
//            holder.ExerciseNames.add(a);
//        }
//        if ((exerciseCat & 0x10) == 0x10) {
//            String a = "팔";
//            holder.ExerciseNames.add(a);
//        }
//        if ((exerciseCat & 0x20) == 0x20) {
//            String a = "복근";
//            holder.ExerciseNames.add(a);
//        }
//        if ((exerciseCat & 0x40) == 0x40) {
//            String a = "유산소";
//            holder.ExerciseNames.add(a);
//        }
//        holder.Adapter.notifyDataSetChanged();
//
//    }
//
//    public void getCurrentWeek() {
//        Date currentDate = new Date();
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(currentDate);
//
//        switch (calendar.get(Calendar.DAY_OF_WEEK) - 1) {
//            case 0:
//                dayOfWeek = "sun"; break; // 일
//            case 1:
//                dayOfWeek = "mon";break; // 월
//            case 2:
//                dayOfWeek = "tue";break; // 화
//            case 3:
//                dayOfWeek = "wed"; break; // 수
//            case 4:
//                dayOfWeek = "thu";break; // 목
//            case 5:
//                dayOfWeek = "fri";break; // 금
//            case 6:
//                dayOfWeek = "sat";break; // 토
//        }
//    }
//    @Override
//    public int getItemCount() {
//        return mDataset.size();
//    }
//}