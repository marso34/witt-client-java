package com.gwnu.witt.Routine;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gwnu.witt.Data.Exercise.ExerciseData;
import com.gwnu.witt.R;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.MainViewHolder> {
    private static ArrayList<ExerciseData> exercises;

    ExerciseAdapter(ArrayList<ExerciseData> exercises) { this.exercises = exercises; }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public CardView CatView;
        public TextView NameView, DetailView;

        public MainViewHolder(@NonNull View view) {
            super(view);

            this.CatView = view.findViewById(R.id.exerciseCat);
            this.NameView = view.findViewById(R.id.exerciseName);
            this.DetailView = view.findViewById(R.id.exerciseDetail);
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
//        holder.CatView.setText(this.exercises.get(position).getStrCat());
//        holder.CatView.setTextColor(Color.parseColor(this.exercises.get(position).getTextColor())); // 부위 텍스트 색
        holder.CatView.setCardBackgroundColor(Color.parseColor(this.exercises.get(position).getTextColor())); // 부위 바탕 색
        holder.NameView.setText(this.exercises.get(position).getExerciseName());
        holder.DetailView.setText(setDetailViewTxt(position));
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    private String setDetailViewTxt(int position) {
        int SetOrTime = exercises.get(position).getSetOrTime(); // 무산소 세트 수, 유산소 시간
        int Volume = exercises.get(position).getVolume(); // 무산소 무게, 유산소 강도(속도)
        int CntOrDis = exercises.get(position).getCntOrDis(); // 무산소 횟수, 유산소 거리
        int cat = exercises.get(position).getCat();

        String result = "";

        if (cat == 0x40) { // 유산소, 아래 내용은 나중에 수정
//            if (Volume != 0)
//                result += Volume + "km/s";
//            if (CntOrDis != 0)
//                result += " · " + CntOrDis + "km";
//            if (SetOrTime != 0)
//                result += SetOrTime + "분";
            if (SetOrTime/60 > 0)
                result += (SetOrTime/60) + "시간 ";
            if (SetOrTime%60 > 0)
                result += (SetOrTime%60) + "분";

        } else {
            if (Volume != 0)
                result += Volume + "kg";
            if (CntOrDis != 0)
                result += " · " + CntOrDis + "회";
            if (SetOrTime != 0)
                result += " · " + SetOrTime + "세트";
        }

        return result;
    }
}
