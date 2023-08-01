package com.gwnu.witt.Routine;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gwnu.witt.Data.Exercise.ExerciseData;
import com.example.healthappttt.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.MainViewHolder> {
    private ArrayList<ExerciseData> exercises;
    private ArrayList<String> selectExerciseIndex;
    private String cat;

    private static final String Signature = "#05C78C";
    private static final String Body = "#4A5567";

    public ExerciseListAdapter(ArrayList<ExerciseData> exercises, ArrayList<String> selectExerciseIndex) {
        this.exercises = exercises;
        this.selectExerciseIndex = selectExerciseIndex;

        if (this.exercises.size() > 0 ) {
            cat = this.exercises.get(0).getStrCat();
        }
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public MaterialCardView ExerciseLayout;
        public CardView CatView;
        public TextView NameView;
        public ImageView CheckedImg;

        public boolean isChecked;

        public MainViewHolder(View view) {
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

        mainViewHolder.ExerciseLayout.setOnClickListener(v -> {
            int position = mainViewHolder.getAbsoluteAdapterPosition();

            mainViewHolder.isChecked = !mainViewHolder.isChecked;

            if (mainViewHolder.isChecked) {
                selectExerciseIndex.add(cat + " " + this.exercises.get(position).getExerciseName());

                mainViewHolder.CheckedImg.setVisibility(View.VISIBLE);
                mainViewHolder.ExerciseLayout.setStrokeWidth(2);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    mainViewHolder.ExerciseLayout.setOutlineSpotShadowColor(Color.parseColor(Signature));
            } else {
                selectExerciseIndex.remove(cat + " " + this.exercises.get(position).getExerciseName());

                mainViewHolder.CheckedImg.setVisibility(View.GONE);
                mainViewHolder.ExerciseLayout.setStrokeWidth(0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    mainViewHolder.ExerciseLayout.setOutlineSpotShadowColor(Color.parseColor(Body));
            }
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        String name = this.exercises.get(position).getExerciseName();

        holder.CatView.setCardBackgroundColor(Color.parseColor(this.exercises.get(position).getTextColor())); // 부위 바탕 색
        holder.NameView.setText(name);

        for (String e : selectExerciseIndex) {
            if (e.equals(cat + " " + name)) {
                holder.isChecked = true;
                holder.CheckedImg.setVisibility(View.VISIBLE);
                holder.ExerciseLayout.setStrokeWidth(2);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    holder.ExerciseLayout.setOutlineSpotShadowColor(Color.parseColor(Signature));

                break;
            }
        }

        if (!holder.isChecked)
            holder.CheckedImg.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }
}
