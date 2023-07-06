package com.example.healthappttt.Routine;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Exercise.ExerciseData;
import com.example.healthappttt.R;

import java.util.ArrayList;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.MainViewHolder> {
    private ArrayList<ExerciseData> exercises;
    private ArrayList<String> selectExerciseIndex;
    private String cat;

    public ExerciseListAdapter(ArrayList<ExerciseData> exercises, ArrayList<String> selectExerciseIndex) {
        this.exercises = exercises;
        this.selectExerciseIndex = selectExerciseIndex;

        if (this.exercises.size() > 0 ) {
            cat = this.exercises.get(0).getStrCat();
        }
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ExerciseLayout;
        public TextView CatView, NameView;
        public LinearLayout CheckBoxLayout;
        public ImageView CheckedImg;

        public boolean isChecked;

        public MainViewHolder(View view) {
            super(view);

            this.ExerciseLayout = view.findViewById(R.id.exerciseLayout);

            this.CatView = view.findViewById(R.id.exerciseCat);
            this.NameView = view.findViewById(R.id.exerciseName);

            this.CheckBoxLayout = view.findViewById(R.id.checkbox);
            this.CheckedImg = view.findViewById(R.id.checked);

            this.isChecked = false;
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exercise_list, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        mainViewHolder.ExerciseLayout.setOnClickListener(v -> {
            int position = mainViewHolder.getAbsoluteAdapterPosition();

            mainViewHolder.isChecked = !mainViewHolder.isChecked;

            if (mainViewHolder.isChecked) {
                mainViewHolder.CheckedImg.setVisibility(View.VISIBLE);
                selectExerciseIndex.add(cat + " " + this.exercises.get(position).getExerciseName());
            } else {
                mainViewHolder.CheckedImg.setVisibility(View.GONE);
                selectExerciseIndex.remove(cat + " " + this.exercises.get(position).getExerciseName());
            }
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        String name = this.exercises.get(position).getExerciseName();

        holder.CatView.setText(cat); // 운동
        holder.CatView.setTextColor(Color.parseColor(this.exercises.get(position).getTextColor())); // 부위 텍스트 색
        holder.CatView.setBackgroundColor(Color.parseColor(this.exercises.get(position).getColor())); // 부위 바탕 색
        holder.NameView.setText(name);

        for (String e : selectExerciseIndex) {
            if (e.equals(cat + " " + name)) {
                holder.CheckedImg.setVisibility(View.VISIBLE);
                holder.isChecked = true;
                break;
            }
        }

        if (!holder.isChecked)
            holder.CheckedImg.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }
}
