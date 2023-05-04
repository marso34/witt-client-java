package com.example.healthappttt.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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

import org.checkerframework.checker.units.qual.C;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.MainViewHolder> {
    private ArrayList<Exercise> exercises;
    private boolean isRoutine; // false면 루틴 생성할 때, true면 확인할 때
    private boolean[][] checked;

    private OnSelectExercise onSelectExercise;

    public ExerciseListAdapter(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
        this.isRoutine = false;
        this.checked = new boolean[exercises.size()][2];
    }

    public ExerciseListAdapter(ArrayList<Exercise> exercises, boolean isRoutine) {
        this.exercises = exercises;
        this.isRoutine = isRoutine;
        this.checked = new boolean[exercises.size()][2];
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public CardView ExerciseCard;

        public LinearLayout ExerciseLayout, ListCatLayout;
        public TextView ListCatTxt;
        public TextView CatView, NameView, DetailView;
        public LinearLayout CheckBoxLayout;
        public ImageView CheckedImg;

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
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exercise_list, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        view.setOnClickListener(v -> {
            int position = mainViewHolder.getAbsoluteAdapterPosition();

            if (!isRoutine && !checked[position][1]) { // 루틴 생성용
                checked[position][0] = ! checked[position][0];

                if (checked[position][0]) {
                    mainViewHolder.CheckedImg.setVisibility(View.VISIBLE);
                    onSelectExercise.onSelectExercise(this.exercises.get(position), true);
                } else {
                    mainViewHolder.CheckedImg.setVisibility(View.GONE);
                    onSelectExercise.onSelectExercise(this.exercises.get(position), false);
                }

            } else { // 루틴 확인용

            }
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        String name = this.exercises.get(position).getTitle();
        String cat = this.exercises.get(position).getState();

        if (name.equals(cat)) {
            checked[position][1] = true;
            holder.ListCatLayout.setVisibility(View.VISIBLE); // 부위 이름만 표시
            holder.ExerciseLayout.setVisibility(View.GONE); // 다른 정보 다 끄기
            holder.ListCatTxt.setText(name);
        } else {
            holder.ListCatLayout.setVisibility(View.GONE);
            holder.ExerciseLayout.setVisibility(View.VISIBLE); // 다른 정보 다 켜기

            holder.CatView.setText(cat); // 운동
            holder.CatView.setTextColor(Color.parseColor(this.exercises.get(position).getTextColor())); // 부위 텍스트 색
            holder.CatView.setBackgroundColor(Color.parseColor(this.exercises.get(position).getColor())); // 부위 바탕 색

            holder.NameView.setText(name);
        }

        if (isRoutine) { // 루틴 확인할 때 -> exercises
            holder.DetailView.setVisibility(View.VISIBLE); // 운동 디테일(세트 수 등) 표시
            holder.CheckBoxLayout.setVisibility(View.GONE); // 선택칸 표시X
            holder.DetailView.setText(setDetailViewTxt(position));
        } else { // 루틴 생성할 때 -> data
            holder.DetailView.setVisibility(View.GONE); // 운동 디테일(세트 수 등) 표시X
            holder.CheckBoxLayout.setVisibility(View.VISIBLE); // 선택칸 표시

            if (checked[position][0])
                holder.CheckedImg.setVisibility(View.VISIBLE);
            else
                holder.CheckedImg.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    private String setDetailViewTxt(int position) {
        int SetOrTime = exercises.get(position).getCount(); // 무산소 세트 수, 유산소 시간
        int Volume = exercises.get(position).getVolume(); // 무산소 무게, 유산소 강도(속도)
        int CntOrDis = exercises.get(position).getNum(); // 무산소 횟수, 유산소 거리

        String result = "";

        if (Volume != 0)
            result += Volume + "kg";
        if (CntOrDis != 0)
            result += " · " + CntOrDis + "회";
        if (SetOrTime != 0)
            result += " · " + SetOrTime + "세트";

        return result;
    }

    public void setOnSelectExerciseListener(OnSelectExercise onSelectExerciseListener) {
        this.onSelectExercise = onSelectExerciseListener;
    } // 액티비티에서 콜백 메서드를 set

    public interface OnSelectExercise {
        void onSelectExercise(Exercise exercise, boolean add);
    } // 운동 클릭했을 때, 엑티비티에 값 전달을 위한 인터페이스
}
