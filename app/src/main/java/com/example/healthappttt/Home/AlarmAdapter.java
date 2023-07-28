package com.example.healthappttt.Home;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.AlarmInfo;
import com.example.healthappttt.R;

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
        View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_alram, parent, false);
        return new MainViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {
        AlarmInfo alarmInfo = mDataset.get(position);
        Log.d(TAG, "onBindViewHolder: ");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 링크에 따라 프레그먼트 or 액티비티 이동
            }
        });

        // Set data based on the alarm category
        switch (alarmInfo.getCat()) {
            case 1:
                holder.title.setText("위트");
                holder.catImg.setImageResource(R.drawable.logo_test);
                // Set other data for category 1
                holder.contents.setText("000님의 새로운 운동파트너 도작! 111님이 위트를 보냈어요");
                holder.time.setText("n월 m일");
                break;
            case 2:
                // Set data for category 2
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
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
