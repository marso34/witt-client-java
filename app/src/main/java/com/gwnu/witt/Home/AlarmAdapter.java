package com.gwnu.witt.Home;

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

import com.gwnu.witt.Data.AlarmInfo;
import com.example.healthappttt.R;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {
        AlarmInfo alarmInfo = mDataset.get(position);
        Log.d(TAG, "onBindViewHolder:alalal "+ alarmInfo.getOUSER_NM() + alarmInfo.getCat());

        // Set data based on the alarm category
        switch (alarmInfo.getCat()) {
            case 1:
                holder.title.setText("위트를 받았어요");
                holder.catImg.setImageResource(R.drawable.logo_test);
                // Set other data for category 1
                holder.contents.setText(alarmInfo.getOUSER_NM()+"님이 위트를 보냈어요");
                holder.time.setText(extractDateTime(alarmInfo.getTS()));
                break;
            case 2:
                holder.title.setText("후기를 받았어요");
                holder.catImg.setImageResource(R.drawable.ic_baseline_email_24);
                // Set other data for category 1
                holder.contents.setText(alarmInfo.getOUSER_NM()+"님이 후기를 보냈어요");
                holder.time.setText(extractDateTime(alarmInfo.getTS()));
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
        if (dateString.equals("-1")) { // 문자열 비교를 equals 메서드를 사용하여 변경
            return "";
        } else {
            // 문자열을 LocalDateTime 객체로 변환
            LocalDateTime dateTime = LocalDateTime.parse(dateString, formatter);
            // 현재 날짜와 시간 가져오기
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDate currentDate = currentDateTime.toLocalDate();
            LocalTime currentTime = currentDateTime.toLocalTime();

            if (dateTime.toLocalDate().isEqual(currentDate)) {
                // 날짜가 오늘이면 시간과 분 추출
                int hour = dateTime.getHour();
                int minute = dateTime.getMinute();
                if (hour == currentTime.getHour()) {
                    // 현재 시간과 같은 시간대라면 분만 계산하여 표시
                    int diffMinute = currentTime.getMinute() - minute;
                    return diffMinute + "분 전";
                } else {
                    // 현재 시간과 다른 시간대라면 시간과 분 계산하여 표시
                    int diffHour = currentTime.getHour() - hour;
                    int diffMinute = currentTime.getMinute() - minute;
                    return diffHour + "시간전 ";
                }
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
