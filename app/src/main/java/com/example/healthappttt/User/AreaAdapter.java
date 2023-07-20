package com.example.healthappttt.User;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.R;

import java.util.ArrayList;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.MainViewHolder>  {

    ArrayList<String>MyExercise;

    private Context mContext;
    private TextView CountTxt;


    public AreaAdapter(Context context, ArrayList<String> MyExercise_) { // 일단 테스트
        this.mContext = context;
        this.MyExercise = new ArrayList<>();
        this.MyExercise = MyExercise_;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {


        public MainViewHolder(View view) {
            super(view);

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
      CountTxt = (TextView) holder.itemView.findViewById(R.id.exerciseCat);

      CountTxt.setBackgroundColor(getColor(MyExercise.get(position)));
      CountTxt.setTextColor(getTextColor(MyExercise.get(position)));
      CountTxt.setText(MyExercise.get(position));
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
        return MyExercise.size();
    } // exercises.size()+1 -> 광고 자리를 위한 +1


}