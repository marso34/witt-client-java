package com.example.healthappttt.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.ExerciseData;
import com.example.healthappttt.R;

import java.util.ArrayList;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.MainViewHolder> {
    private ArrayList<ExerciseData> exercises;
    private ArrayList<ExerciseData> selectExercises;

    private OnSelectExercise onSelectExercise;

    public ExerciseListAdapter(ArrayList<ExerciseData> exercises) {
        this.exercises = exercises;
        selectExercises = new ArrayList<>();
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
//                selectExercises.add(new ExerciseData(this.exercises.get(position)));
//                onSelectExercise.onSelectExercise(this.exercises.get(position), true);
            } else {
                mainViewHolder.CheckedImg.setVisibility(View.GONE);
//                onSelectExercise.onSelectExercise(this.exercises.get(position), false);
            }

        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        String name = this.exercises.get(position).getExerciseName();
        String cat = this.exercises.get(position).getState();

        holder.CatView.setText(cat); // 운동
        holder.CatView.setTextColor(Color.parseColor(this.exercises.get(position).getTextColor())); // 부위 텍스트 색
        holder.CatView.setBackgroundColor(Color.parseColor(this.exercises.get(position).getColor())); // 부위 바탕 색
        holder.NameView.setText(name);

        if (holder.isChecked)
            holder.CheckedImg.setVisibility(View.VISIBLE);
        else
            holder.CheckedImg.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public void setOnSelectExerciseListener(OnSelectExercise onSelectExerciseListener) {
        this.onSelectExercise = onSelectExerciseListener;
    } // 액티비티에서 콜백 메서드를 set

    public interface OnSelectExercise {
        void onSelectExercise(ExerciseData exercise, boolean add);
    } // 운동 클릭했을 때, 엑티비티에 값 전달을 위한 인터페이스
}
