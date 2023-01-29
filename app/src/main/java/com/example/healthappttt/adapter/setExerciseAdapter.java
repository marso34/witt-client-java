package com.example.healthappttt.adapter;

import android.graphics.Color;
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

public class setExerciseAdapter extends RecyclerView.Adapter<setExerciseAdapter.MainViewHolder> {
    private ArrayList<Exercise> exercises;
    private String exerciseCategories;
    private int exerciseCnt;

    public setExerciseAdapter(Routine routine) { // 일단 테스트
        this.exercises = routine.getExercises();
        this.exerciseCategories = routine.getExerciseCategories();
        this.exerciseCnt = routine.getExercieseCount();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout AerobicLayout, countLayout;
        public TextView CatView;
        public TextView NameView;
        public TextView DetailView;

        public String DetailViewTxt;

        public MainViewHolder(View view) {
            super(view);

            this.AerobicLayout = (LinearLayout) view.findViewById(R.id.cardioLayout);
            this.countLayout = (LinearLayout) view.findViewById(R.id.countLayout);
            this.CatView = (TextView) view.findViewById(R.id.exerciseCat);
            this.NameView = (TextView) view.findViewById(R.id.exerciseName);
            this.DetailView = (TextView) view.findViewById(R.id.exerciseDetail);
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
        setTxt(holder);

        holder.AerobicLayout.setVisibility(View.GONE);
        holder.countLayout.setVisibility(View.GONE);

        holder.CatView.setText(this.exercises.get(position).getState()); // 운동 부위
        holder.CatView.setBackgroundColor(Color.parseColor(this.exercises.get(position).getColor()));
        holder.NameView.setText(this.exercises.get(position).getTitle()); // 운동 이름
        holder.DetailView.setText(holder.DetailViewTxt);
    }

    @Override
    public int getItemCount() {
        return exerciseCnt;
    }

    private void setTxt(@NonNull MainViewHolder holder) { // 초기값 설정
        int position = holder.getAdapterPosition();
        int count = this.exercises.get(position).getCount();
        int volume = this.exercises.get(position).getVolume();

        if (holder.DetailViewTxt == null) {
            if (this.exercises.get(position).getState() == "유산소") {
                holder.DetailViewTxt = "속도 " + volume +  "· " + count + "분";
            } else {
                holder.DetailViewTxt = volume + " kg · " + count + " 세트";
            }
        }
    }
}
