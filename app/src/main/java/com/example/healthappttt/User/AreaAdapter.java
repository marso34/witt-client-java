package com.example.healthappttt.User;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.R;

import java.util.ArrayList;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.MainViewHolder>  {
    ArrayList<String> exerciseCat;

    private int size;

    public AreaAdapter(ArrayList<String> exerciseCat) {
        this.exerciseCat = exerciseCat;
        this.size = exerciseCat.size();

        if (size > 4) size = 5;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public CardView CatView;
        public TextView CatTxt, PlusTxt;

        public MainViewHolder(View view) {
            super(view);

            this.CatView = view.findViewById(R.id.cat);
            this.CatTxt = view.findViewById(R.id.exerciseCat);
            this.PlusTxt = view.findViewById(R.id.plus);
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_area, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        if (position < 4) {
            holder.CatView.setVisibility(View.VISIBLE);
            holder.PlusTxt.setVisibility(View.GONE);

            holder.CatTxt.setText(exerciseCat.get(position));
            holder.CatTxt.setBackgroundColor(getColor(exerciseCat.get(position)));
            holder.CatTxt.setTextColor(getTextColor(exerciseCat.get(position)));
        } else {
            holder.CatView.setVisibility(View.GONE);
            holder.PlusTxt.setVisibility(View.VISIBLE);
            holder.PlusTxt.setText("+" + (exerciseCat.size() - position));
        }
    }

    public int getColor(String cat) {
        switch (cat) {
            case "가슴":  return Color.parseColor("#eee6fa");
            case "등":  return Color.parseColor("#d9f7ee");
            case "어깨":  return Color.parseColor("#fde6f3");
            case "하체":  return Color.parseColor("#ffefeb");
            case "팔": return Color.parseColor("#fef8ee");
            case "복근": return Color.parseColor("#f9e6eb");
            case "유산소": return Color.parseColor("#e6f1fd");
        }

        return 0;
    }

    public int getTextColor(String cat) {
        switch (cat) {
            case "가슴":  return Color.parseColor("#8C5ADB");
            case "등":  return Color.parseColor("#05C78C");
            case "어깨":  return Color.parseColor("#F257AF");
            case "하체":  return Color.parseColor("#FC673F");
            case "팔": return Color.parseColor("#F2BB57");
            case "복근": return Color.parseColor("#C71040");
            case "유산소": return Color.parseColor("#579EF2");
        }

        return 0;
    }

    @Override
    public int getItemCount() {
        return size;
    } // exercises.size()+1 -> 광고 자리를 위한 +1


}