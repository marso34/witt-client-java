package com.example.healthappttt.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.R;

import java.util.ArrayList;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.MainViewHolder> {

    private ArrayList<Routine> routines;


    private int exerciseCategories;
    private int exerciseCnt;

    public RoutineAdapter() {
    }

    public RoutineAdapter(ArrayList<Routine> routines) {
        this.routines = routines;


    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public TextView am_pm1, am_pm2, time1, time2;
        public TextView Edit;

        public MainViewHolder(View view) {
            super(view);

            this.am_pm1 = (TextView) view.findViewById(R.id.am_pm1);
            this.am_pm2 = (TextView) view.findViewById(R.id.am_pm2);
            this.time1 = (TextView) view.findViewById(R.id.time1);
            this.time2 = (TextView) view.findViewById(R.id.time2);
            this.Edit = (TextView) view.findViewById(R.id.Edit);
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_routine, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
//        holder.AerobicLayout.setVisibility(View.GONE);
//        holder.countLayout.setVisibility(View.GONE);
//        holder.am_pm1.setVisibility();
//
//        holder.eName.setText(exercises.get(position).getTitle());
    }

    @Override
    public int getItemCount() { return exerciseCnt; }
}
