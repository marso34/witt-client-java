package com.example.healthappttt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.ExerciseName;
import com.example.healthappttt.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.MainViewHolder> {
    private boolean t;
    private ArrayList<ExerciseName> data;
    private ArrayList<Exercise> exercises;

    public ExerciseListAdapter(ArrayList<ExerciseName> data) {
        this.data = data;
        this.t = false;
    }

    public ExerciseListAdapter(ArrayList<Exercise> exercises, boolean t) {
        this.exercises = exercises;
        this.t = t;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {

        public MainViewHolder(View view) {
            super(view);

        }
    }


    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exercise_list, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
