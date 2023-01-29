package com.example.healthappttt.adapter;

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

    public RoutineAdapter(Routine routine) {
        this.exercises = new ArrayList<>(routine.getExercises());
        this.exerciseCategories = routine.getExerciseCategories();
        this.exerciseCnt = routine.getExercieseCount();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {

        public MainViewHolder(View view) {
            super(view);

        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }



}
