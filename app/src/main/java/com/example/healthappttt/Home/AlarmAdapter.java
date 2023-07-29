package com.example.healthappttt.Home;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.AlarmInfo;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.R;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.MainViewHolder> {
    private ArrayList<AlarmInfo> mDataset;
    private Context mContext;

    static class MainViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView contents;
        public ImageView catImg;
        public TextView time;

        MainViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.CAT);
            contents = itemView.findViewById(R.id.contents);
            catImg = itemView.findViewById(R.id.CAT_IMG);
            time = itemView.findViewById(R.id.beforetime);
        }
    }

    public AlarmAdapter(Context mContext, ArrayList<AlarmInfo> myDataset) {
        this.mContext = mContext;
        this.mDataset = myDataset;
    }
    @NonNull
    @Override
    public AlarmAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_alram, parent, false);
        AlarmAdapter.MainViewHolder viewHolder = new AlarmAdapter.MainViewHolder(view);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {
        AlarmInfo alarmInfo = mDataset.get(position);
        Log.d(TAG, "onBindViewHolder: ");
        SQLiteUtil sqLiteUtil = SQLiteUtil.getInstance();


        // Set data based on the alarm category
        switch (alarmInfo.getCat()) {
            case 1:
                holder.title.setText("위트를 받았어요");
                holder.catImg.setImageResource(R.drawable.logo_test);
                // Set other data for category 1
                holder.contents.setText(alarmInfo.getOUSER_NM()+"님이 위트를 보냈어요");
                holder.time.setText("n월 m일");
                break;
            case 2:
                holder.title.setText("후기를 받았어요");
                holder.catImg.setImageResource(R.drawable.ic_baseline_email_24);
                // Set other data for category 1
                holder.contents.setText(alarmInfo.getOUSER_NM()+"님이 후기를 보냈어요");
                holder.time.setText("n월 m일");
                break;
            case 3:
                // Set data for category 3
                break;
            case 4:
                // Set data for category 4
                break;
            default:
                // Handle unknown category or any other case
                break;
        }
        Log.d(TAG, "onBindViewHolder: "+alarmInfo.getRead_TS());
        if(alarmInfo.getRead_TS().equals("READ")){
            holder.itemView.findViewById(R.id.out).setBackgroundResource(R.drawable.round_button_white);
        }
        else{
            holder.itemView.findViewById(R.id.out).setBackgroundResource(R.drawable.round_line);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String extractDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if(dateString == "-1"){
            return "";
        }
        else {
            // 문자열을 LocalDateTime 객체로 변환
            LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
            // 현재 날짜 가져오기
            LocalDate currentDate = LocalDate.now();
            if (dateTime.toLocalDate().isEqual(currentDate)) {
                // 날짜가 오늘이면 시간과 분 추출
                int hour = dateTime.getHour();
                int minute = dateTime.getMinute();
                return String.format("%02d:%02d", hour, minute);
            } else {
                // 날짜가 오늘이 아니면 월과 일 추출
                int month = dateTime.getMonthValue();
                int day = dateTime.getDayOfMonth();
                return String.format("%02d월 %02d일", month, day);
            }
        }
    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
