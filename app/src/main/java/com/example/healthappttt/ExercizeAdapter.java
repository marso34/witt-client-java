package com.example.healthappttt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Arrays;

public class ExercizeAdapter extends RecyclerView.Adapter<ExercizeAdapter.MainViewHolder>  {
    private String[] Name;
    private int[] SetNum;
    private int[] Num;
    private int[] Weight;
    private int[] setCnt;

    public ExercizeAdapter(String[] Name, int[] SetNum, int[] Num, int[] Weight, int[] setCnt) { // 일단 테스트
        this.Name = Name;
        this.SetNum = SetNum;
        this.Num = Num;
        this.Weight = Weight;
        this.setCnt = setCnt;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder{
        public TextView NameView;
        public TextView SetView;
        public TextView DetailView;

        public MainViewHolder(View view) {
            super(view);

            this.NameView = view.findViewById(R.id.exerciseName);
            this.SetView = view.findViewById(R.id.setCountNum);
            this.DetailView = view.findViewById(R.id.exerciseDetail);
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exercize, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countUp(v, mainViewHolder.getAdapterPosition());
            }
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
//        @SuppressLint("DefaultLocale") String result = String.format("Set %d", this.cnt[position]);
        @SuppressLint("DefaultLocale") String result2 = String.format("%dKg X %d개", this.Weight[position], this.Num[position]);
        holder.NameView.setText(this.Name[position]);
//        holder.SetView.setText(result);
        holder.DetailView.setText(result2);
    }

    @Override
    public int getItemCount() {
        return Name.length;
    }

    private void countUp(View v, final int position) {

    }
}