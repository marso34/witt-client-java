package com.example.healthappttt.Record;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Exercise.ExerciseData;
import com.example.healthappttt.Data.Exercise.RecordData;
import com.example.healthappttt.Data.Exercise.RoutineData;
import com.example.healthappttt.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class ExerciseResultAdapter extends RecyclerView.Adapter<ExerciseResultAdapter.MainViewHolder> {
    private ArrayList<ExerciseData> routineEx;
    private ArrayList<ExerciseData> recordEx;

    public ExerciseResultAdapter(ArrayList<ExerciseData> routineEx, ArrayList<ExerciseData> recordEx) {
        this.routineEx = routineEx;
        this.recordEx = recordEx;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public MaterialCardView ExerciseLayout;
        public CardView CatView;
        public TextView NameView;
        public ImageView CheckedImg;

        public boolean isChecked;

        public MainViewHolder(@NonNull View view) {
            super(view);

            this.ExerciseLayout = view.findViewById(R.id.exerciseLayout);
            this.CatView = view.findViewById(R.id.exerciseCat);
            this.NameView = view.findViewById(R.id.exerciseName);
            this.CheckedImg = view.findViewById(R.id.checked);

            this.isChecked = false;
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exercise_list, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        ExerciseData routine = routineEx.get(position);
        ExerciseData record  = recordEx.get(position);

        holder.NameView.setText(record.getExerciseName());
        holder.CatView.setCardBackgroundColor(Color.parseColor(record.getTextColor()));

        if (routine.getSetOrTime() == record.getSetOrTime()) {
            holder.CheckedImg.setImageResource(R.drawable.baseline_check_circle_24_g);
            holder.CheckedImg.setVisibility(View.VISIBLE);
            holder.ExerciseLayout.setStrokeWidth(1);
        } else {
            holder.CheckedImg.setVisibility(View.GONE);
            holder.ExerciseLayout.setStrokeWidth(0);
        }
    }

    @Override
    public int getItemCount() { return recordEx.size(); }
}
