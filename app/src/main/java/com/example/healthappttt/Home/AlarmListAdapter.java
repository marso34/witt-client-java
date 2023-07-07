package com.example.healthappttt.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.AlarmInfo;
import com.example.healthappttt.R;

import java.util.List;

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.AlarmListViewHolder> {

    private List<AlarmInfo> alarmList;
    private OnItemClickListener listener;

    public AlarmListAdapter(List<AlarmInfo> alarmList) {
        this.alarmList = alarmList;
    }

    @NonNull
    @Override
    public AlarmListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_alram, parent, false);
        return new AlarmListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmListViewHolder holder, int position) {
        AlarmInfo alarm = alarmList.get(position);
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class AlarmListViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;

        public AlarmListViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.UNE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}

