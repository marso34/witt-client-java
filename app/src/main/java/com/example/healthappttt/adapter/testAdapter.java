package com.example.healthappttt.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.R;

import java.util.ArrayList;

public class testAdapter extends RecyclerView.Adapter<testAdapter.MainViewHolder> {
    private ArrayList<Exercise> exercises;

    public testAdapter(Routine routine) {
        this.exercises = routine.getExercises();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public TextView ExerciseCat, ExerciseName, ExerciseDetail;
        public String DetailViewTxt;

        public MainViewHolder(View view) {
            super(view);

            this.ExerciseCat = view.findViewById(R.id.exerciseCat);
            this.ExerciseName = view.findViewById(R.id.exerciseName);
            this.ExerciseDetail = view.findViewById(R.id.exerciseDetail);

            this.DetailViewTxt = null;
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_test, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        setTxt(holder);

        holder.ExerciseCat.setText(this.exercises.get(position).getState()); // 운동 부위
        holder.ExerciseCat.setBackgroundColor(Color.parseColor(this.exercises.get(position).getColor()));
        holder.ExerciseName.setText(this.exercises.get(position).getTitle());
        holder.ExerciseDetail.setText(holder.DetailViewTxt); // 운동 이름
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    private void setTxt(@NonNull MainViewHolder holder) { // 초기값 설정
        int position = holder.getAdapterPosition();

        if (holder.DetailViewTxt == null) {
            int count = this.exercises.get(position).getCount();
            int volume = this.exercises.get(position).getVolume();

            if (this.exercises.get(position).getState().equals("유산소")) {
                holder.DetailViewTxt = "속도 " + volume +  "· " + count + "분";
            } else {
                holder.DetailViewTxt = volume + " kg · " + count + " 세트";
            }
        }
    }

}
