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
    private ArrayList<Exercise> exercises;
    private String exerciseCategories;
    private int exerciseCnt;

    public RoutineAdapter() {
    }

    public RoutineAdapter(Routine routine) {
        this.exercises = routine.getExercises();
        this.exerciseCategories = routine.getExerciseCategories();
        this.exerciseCnt = routine.getExercieseCount();

    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout AerobicLayout, countLayout;

        public MainViewHolder(View view) {
            super(view);

            this.AerobicLayout = (LinearLayout) view.findViewById(R.id.cardioLayout);
            this.countLayout = (LinearLayout) view.findViewById(R.id.countLayout);

        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exercise, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);


        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.AerobicLayout.setVisibility(View.GONE);
        holder.countLayout.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() { return exerciseCnt; }



}
