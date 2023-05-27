package com.example.healthappttt.adapter;//package com.example.healthappttt.adapter;

import android.content.Context;
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
    private AlarmInfo alarmInfo;
    private Context mContext;

    static class MainViewHolder extends RecyclerView.ViewHolder {
        private Context mContext;
        public TextView CAT ;
        public TextView Contents ;
        public ImageView CAT_IMG;
        public TextView time_;
        public RecyclerView recyclerView;


        MainViewHolder(@NonNull View itemView) {
            super(itemView);
            CAT =  itemView.findViewById(R.id.CAT);
            Contents = itemView.findViewById(R.id.contents);
            CAT_IMG = itemView.findViewById(R.id.CAT_IMG);
            time_ = itemView.findViewById(R.id.beforetime);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }

    public AlarmAdapter(Context mContext,ArrayList<AlarmInfo> myDataset) {
        this.mContext = mContext;
        this.mDataset = myDataset;
    }


    @Override
    public int getItemViewType(int position){
        return position;
    }

    @NonNull
    @Override
    public AlarmAdapter.MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cardView = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_alram, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(cardView);

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainViewHolder holder, int position) {
         alarmInfo = mDataset.get(position);

//
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(mContext, ProfileActivity.class);
                //intent.putExtra("UserInfo",userInfo);
//                intent.putExtra("post",finalProfilefile);
                //mContext.startActivity(intent);
            }
        });

        if(alarmInfo.getCat() == 0){
            holder.CAT.setText("위트");
            holder.CAT_IMG.setImageResource(R.drawable.logo_test);
            //SQL라이트로 로컬에서 내 이름 긁어 와서000에 넣기.
            //앱 실행시 서버에서 알람테이블을 긁어오면서 상대방의 정보도 가져오기.
            //SQL라이트로 로컬에서 나에게 위트보낸 상대방의 이름 긁어와서 넣기
            holder.Contents.setText("000님의 새로운 운동파트너 도작! 111님이 위트를 보냈어요");
            //sql라이트로 시간값 긁어오기.
            holder.time_.setText("n월 m일");
        }
        else if(alarmInfo.getCat() == 1){
//            holder.CAT.setText("위트");
//            holder.CAT_IMG.setImageResource(R.drawable.logo_test);
//            //SQL라이트로 로컬에서 내 이름 긁어 와서000에 넣기.
//            //앱 실행시 서버에서 알람테이블을 긁어오면서 상대방의 정보도 가져오기.
//            //SQL라이트로 로컬에서 나에게 위트보낸 상대방의 이름 긁어와서 넣기
//            holder.Contents.setText("000님의 새로운 운동파트너 도작! 111님이 위트를 보냈어요");
        }
        else if(alarmInfo.getCat() == 2){
            //채워넣기
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}