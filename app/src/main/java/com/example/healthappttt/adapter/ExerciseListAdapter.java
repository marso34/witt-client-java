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

    private OnSelectExercise onSelectExercise;

    public ExerciseListAdapter(ArrayList<ExerciseData> exercises) {
        this.exercises = exercises;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ExerciseLayout, ListCatLayout;
        public TextView ListCatTxt;
        public TextView CatView, NameView;
        public LinearLayout CheckBoxLayout;
        public ImageView CheckedImg;

        public boolean isChecked;

        public MainViewHolder(View view) {
            super(view);

            this.ExerciseLayout = view.findViewById(R.id.exerciseLayout);
            this.ListCatLayout = view.findViewById(R.id.listCatLayout);
            this.ListCatTxt = view.findViewById(R.id.listCat);

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

        view.findViewById(R.id.exerciseLayout).setOnClickListener(v -> {
            int position = mainViewHolder.getAbsoluteAdapterPosition();

            mainViewHolder.isChecked = !mainViewHolder.isChecked;

            if (mainViewHolder.isChecked) {
                mainViewHolder.CheckedImg.setVisibility(View.VISIBLE);
                onSelectExercise.onSelectExercise(this.exercises.get(position), true);
            } else {
                mainViewHolder.CheckedImg.setVisibility(View.GONE);
                onSelectExercise.onSelectExercise(this.exercises.get(position), false);
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

        if (name.equals(cat)) {
            holder.ListCatLayout.setVisibility(View.VISIBLE); // 부위 이름만 표시
            holder.ExerciseLayout.setVisibility(View.GONE);   // 다른 정보 다 끄기
            holder.CheckBoxLayout.setVisibility(View.GONE);   // 선택칸 끄기
            holder.ListCatTxt.setText(name);
        } else {
            holder.ListCatLayout.setVisibility(View.GONE);
            holder.ExerciseLayout.setVisibility(View.VISIBLE); // 다른 정보 다 켜기
            holder.CheckBoxLayout.setVisibility(View.VISIBLE); // 선택칸 표시

            if (holder.isChecked)
                holder.CheckedImg.setVisibility(View.VISIBLE);
            else
                holder.CheckedImg.setVisibility(View.GONE);
        }
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
