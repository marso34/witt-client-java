package com.example.healthappttt.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.ExerciseName;
import com.example.healthappttt.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.MainViewHolder> {
    private ArrayList<Exercise> exercises;
    private boolean isRoutine; // false면 루틴 생성할 때, true면 확인할 때

    private OnSelectExercise onSelectExercise;

    public ExerciseListAdapter(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
        this.isRoutine = false;
    }

    public ExerciseListAdapter(ArrayList<Exercise> exercises, boolean isRoutine) {
        this.exercises = exercises;
        this.isRoutine = isRoutine;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public CardView ExerciseCard;

        public LinearLayout ExerciseLayout, ListCatLayout;
        public TextView ListCatTxt;

        public TextView CatView, NameView, DetailView;
        public LinearLayout CheckBoxLayout;
        public ImageView CheckedImg;

        public boolean checked;

        public MainViewHolder(View view) {
            super(view);

            this.ExerciseCard = view.findViewById(R.id.exerciseCard);
            this.ExerciseLayout = view.findViewById(R.id.exerciseLayout);

            this.ListCatLayout = view.findViewById(R.id.listCatLayout);
            this.ListCatTxt = view.findViewById(R.id.listCat);

            this.CatView = view.findViewById(R.id.exerciseCat);
            this.NameView = view.findViewById(R.id.exerciseName);
            this.DetailView = view.findViewById(R.id.exerciseDetail);
            this.CheckBoxLayout = view.findViewById(R.id.checkbox);
            this.CheckedImg = view.findViewById(R.id.checked);

            this.checked = false;
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exercise_list, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        view.setOnClickListener(v -> {
            int position = mainViewHolder.getAdapterPosition();

            if (isRoutine) { // 루틴 확인용

            } else { // 루틴 생성용
                mainViewHolder.checked = !mainViewHolder.checked;

                if (mainViewHolder.checked) {
                    mainViewHolder.CheckedImg.setVisibility(View.VISIBLE);
                    onSelectExercise.onSelectExercise(this.exercises.get(position), true);
                } else {
                    mainViewHolder.CheckedImg.setVisibility(View.GONE);
                    onSelectExercise.onSelectExercise(this.exercises.get(position), false);
                }

            }
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        String name = this.exercises.get(position).getTitle();
        String cat = this.exercises.get(position).getState();

        if (name.equals(cat)) { // 부위 구분
            holder.ListCatLayout.setVisibility(View.VISIBLE); // 부위 이름만 표시
            holder.ExerciseLayout.setVisibility(View.GONE); // 다른 정보 다 끄기
            holder.ListCatTxt.setText(name);
        } else {
            holder.ListCatLayout.setVisibility(View.GONE);
            holder.ExerciseLayout.setVisibility(View.VISIBLE); // 다른 정보 다 켜기

            holder.CatView.setText(cat); // 운동 부위
            holder.CatView.setTextColor(Color.parseColor(this.exercises.get(position).getTextColor())); // 부위 텍스트 색
            holder.CatView.setBackgroundColor(Color.parseColor(this.exercises.get(position).getColor())); // 부위 바탕 색
            holder.NameView.setText(name);
        }

        if (isRoutine) { // 루틴 확인할 때 -> exercises
            holder.DetailView.setVisibility(View.VISIBLE); // 운동 디테일(세트 수 등) 표시
            holder.CheckBoxLayout.setVisibility(View.GONE); // 선택칸 표시X
//            holder.DetailView.setText("this.exercises.get(position).무게, 세트 등등 "); // 무게 및 세트 수
        } else { // 루틴 생성할 때 -> data
            holder.DetailView.setVisibility(View.GONE); // 운동 디테일(세트 수 등) 표시X
            holder.CheckBoxLayout.setVisibility(View.VISIBLE); // 선택칸 표시
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
        void onSelectExercise(Exercise exercise, boolean add);
    } // 운동 클릭했을 때, 엑티비티에 값 전달을 위한 인터페이스
}
