package com.example.healthappttt.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.R;

import java.util.ArrayList;

public class setExerciseAdapter extends RecyclerView.Adapter<setExerciseAdapter.MainViewHolder> {
    private ArrayList<Exercise> exercises;

    private OnExerciseClick onExerciseClick;

    public setExerciseAdapter(Routine routine) { // 일단 테스트
        this.exercises = routine.getExercises();
    }

    public setExerciseAdapter(ArrayList<Exercise> exercises) { // 일단 테스트
        this.exercises = exercises;
//        this.exerciseCategories = routine.getExerciseCategories();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public TextView CatView, NameView, DetailView;
        public ImageView DelImageVIew;

        public String DetailViewTxt;

        public MainViewHolder(View view) {
            super(view);

            this.CatView = (TextView) view.findViewById(R.id.exerciseCat);
            this.NameView = (TextView) view.findViewById(R.id.exerciseName);
            this.DetailView = (TextView) view.findViewById(R.id.exerciseDetail);
            this.DelImageVIew = (ImageView) view.findViewById(R.id.delBtn);
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_set_exercise, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        mainViewHolder.DelImageVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = mainViewHolder.getAdapterPosition();

                onExerciseClick.onExerciseClick(position);
            }
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        setTxt(holder);

        holder.CatView.setText(this.exercises.get(position).getState()); // 운동 부위
        holder.CatView.setBackgroundColor(Color.parseColor(this.exercises.get(position).getColor()));
        holder.NameView.setText(this.exercises.get(position).getTitle()); // 운동 이름
        holder.DetailView.setText(holder.DetailViewTxt);
    }

    @Override
    public int getItemCount() { return exercises.size(); }

    public void removeItem(int position) { exercises.remove(position); }

    private void setTxt(@NonNull MainViewHolder holder) { // 초기값 설정
        int position = holder.getAdapterPosition();
        int count = this.exercises.get(position).getCount();
        int volume = this.exercises.get(position).getVolume();

        if (holder.DetailViewTxt == null) {
            if (this.exercises.get(position).getState() == "유산소") {
                holder.DetailViewTxt = "속도 " + volume +  "· " + count + "분";
            } else {
                holder.DetailViewTxt = volume + " kg · " + count + " 세트";
            }
        }
    }

    public void setOnExerciseClickListener(OnExerciseClick onExerciseClickListener) {
        this.onExerciseClick = onExerciseClickListener;
    } // 액티비티에서 콜백 메서드를 set

    public interface OnExerciseClick {
        void onExerciseClick(int position);
    } // 운동 클릭했을 때, 엑티비티에 값 전달을 위한 인터페이스
}
