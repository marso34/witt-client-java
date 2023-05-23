package com.example.healthappttt.adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.R;
import com.example.healthappttt.Data.Routine;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.MainViewHolder>  {
    private static ArrayList<Exercise> exercises;

    private OnExerciseClick onExerciseClick;

    public ExerciseAdapter(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public CardView ExerciseCard;
        public LinearLayout CountLayout; // 각 운동정보, 유산소 카운트, 세트카운트, 완료 레이아웃
        public TextView CatView, NameView;
        public TextView CountView, CountUnitView, CardioTxtView;
        public ProgressBar progressBar;
        public ImageView endView;

//        public int progressMaxValue;

        public MainViewHolder(View view) {
            super(view);

            this.ExerciseCard  = view.findViewById(R.id.exerciseCard);
            this.CountLayout   = view.findViewById(R.id.countLayout);
            this.CatView       = view.findViewById(R.id.exerciseCat);
            this.NameView      = view.findViewById(R.id.exerciseName);
            this.CountView     = view.findViewById(R.id.count);
            this.CountUnitView = view.findViewById(R.id.countUnit);
            this.CardioTxtView = view.findViewById(R.id.cardioTxt);
            this.progressBar   = view.findViewById(R.id.progressBar);
            this.endView       = view.findViewById(R.id.checked);

//            this.progressMaxValue = exercises.get(getAbsoluteAdapterPosition()).getCount();
//            exercises.get(getAbsoluteAdapterPosition()).setCount(0);
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exercise, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mainViewHolder.getAbsoluteAdapterPosition();

                onExerciseClick.onExerciseClick(position, mainViewHolder.CardioTxtView, mainViewHolder.progressBar);

                if (mainViewHolder.progressBar.getProgress() == mainViewHolder.progressBar.getMax()) {
                    mainViewHolder.endView.setVisibility(View.VISIBLE);
                    mainViewHolder.CountLayout.setVisibility(View.GONE);
                }
            }
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        String name = this.exercises.get(position).getTitle();
        String cat = this.exercises.get(position).getState();
        int progressCurrentValue = holder.progressBar.getProgress();
        int progressMaxValue = this.exercises.get(position).getCount();

        holder.CatView.setTextColor(Color.parseColor(this.exercises.get(position).getTextColor())); // 부위 텍스트 색
        holder.CatView.setBackgroundColor(Color.parseColor(this.exercises.get(position).getColor())); // 부위 바탕 색
        holder.CatView.setText(cat);
        holder.NameView.setText(name);


        if (this.exercises.get(position).getCat() == 0x40) {// 유산소일 경우
            holder.CountView.setText(Integer.toString(progressMaxValue));
            holder.CountUnitView.setText("분");
            holder.CardioTxtView.setVisibility(View.VISIBLE);
            holder.CardioTxtView.setText(holder.CardioTxtView.getText());

            progressMaxValue = progressMaxValue * 60 * 100;
        } else {
            holder.CountView.setText(Integer.toString(this.exercises.get(position).getNum()));
            holder.CountUnitView.setText("회");
            holder.CardioTxtView.setVisibility(View.GONE);
        }

        holder.progressBar.setMax(progressMaxValue);
        holder.progressBar.setProgress(progressCurrentValue);

        if (progressCurrentValue == progressMaxValue) { // 운동을 완료했을 경우
            holder.endView.setVisibility(View.VISIBLE);
            holder.CountLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    } // exercises.size()+1 -> 광고 자리를 위한 +1

    public void setOnExerciseClickListener(OnExerciseClick onExerciseClickListener) {
        this.onExerciseClick = onExerciseClickListener;
    } // 액티비티에서 콜백 메서드를 set

    public interface OnExerciseClick {
        void onExerciseClick(int position, TextView CardioTxtView, ProgressBar progressBar);
    } // 운동 클릭했을 때, 엑티비티에 값 전달을 위한 인터페이스
}