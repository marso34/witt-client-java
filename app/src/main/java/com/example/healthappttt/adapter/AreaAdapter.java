package com.example.healthappttt.adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.User;
import com.example.healthappttt.R;
import com.example.healthappttt.Data.Routine;

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
      CountTxt = (TextView) holder.itemView.findViewById(R.id.area);
      CountTxt.setText(MyExercise.get(position));
    }

    @Override
    public int getItemCount() {
        return MyExercise.size();
    } // exercises.size()+1 -> 광고 자리를 위한 +1


}