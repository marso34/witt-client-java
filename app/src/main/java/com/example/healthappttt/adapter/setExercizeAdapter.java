package com.example.healthappttt.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Exercize;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.R;

import java.util.ArrayList;

public class setExercizeAdapter extends RecyclerView.Adapter<setExercizeAdapter.MainViewHolder> {
    private ArrayList<Exercize> exercizes;
    private String exercizeCategories;
    private int exercizeCnt;

    public setExercizeAdapter(Routine routine) { // 일단 테스트
        this.exercizes = new ArrayList<>(routine.getExercizes());
        this.exercizeCategories = routine.getExercizeCategories();
        this.exercizeCnt = routine.getExerciezeCount();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout AerobicLayout;
        public LinearLayout countLayout;
        public TextView CatView;
        public TextView NameView;
        public TextView DetailView;

        public String DetailViewTxt;

        public MainViewHolder(View view) {
            super(view);

            this.AerobicLayout = (LinearLayout) view.findViewById(R.id.AerobicLayout);
            this.countLayout = (LinearLayout) view.findViewById(R.id.countLayout);
            this.CatView = (TextView) view.findViewById(R.id.exerciseCat);
            this.NameView = (TextView) view.findViewById(R.id.exerciseName);
            this.DetailView = (TextView) view.findViewById(R.id.exerciseDetail);
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exercize, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        setTxt(holder);

        holder.AerobicLayout.setVisibility(View.GONE);
        holder.countLayout.setVisibility(View.GONE);

        holder.CatView.setText(this.exercizes.get(position).getState()); // 운동 부위
        holder.CatView.setBackgroundColor(Color.parseColor(this.exercizes.get(position).getColor()));
        holder.NameView.setText(this.exercizes.get(position).getTitle()); // 운동 이름
        holder.DetailView.setText(holder.DetailViewTxt);
    }


    @Override
    public int getItemCount() {
        return exercizeCnt;
    }

    private void setTxt(@NonNull MainViewHolder holder) { // 초기값 설정
        int position = holder.getAdapterPosition();
        int count = this.exercizes.get(position).getCount();
        int volume = this.exercizes.get(position).getVolume();

        if (holder.DetailViewTxt == null) {
            if (this.exercizes.get(position).getState() == "유산소") {
                holder.DetailViewTxt = "속도 " + volume +  "· " + count + "분";
            } else {
                holder.DetailViewTxt = volume + " Kg · " + count + " 세트";
            }
        }
    }
}
