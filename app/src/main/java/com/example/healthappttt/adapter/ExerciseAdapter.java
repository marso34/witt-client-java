package com.example.healthappttt.adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.R;
import com.example.healthappttt.Data.Routine;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.MainViewHolder>  {
    private ArrayList<Exercise> exercises;
    private boolean isCount;

    private OnExerciseClick onExerciseClick;

    public ExerciseAdapter(Routine routine) { // 일단 테스트
//        this.exercises = routine.getExercises();
        this.isCount = false;
    }

    public ExerciseAdapter(Routine routine, boolean isCount) { // 일단 테스트
//        this.exercises = routine.getExercises();
        this.isCount = isCount;
    }

    public ExerciseAdapter(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
        this.isCount = false;
    }

    public ExerciseAdapter(ArrayList<Exercise> exercises, boolean isCount) {
        this.exercises = exercises;
        this.isCount = isCount;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public CardView ExerciseCard;
        public LinearLayout ExerciseLayout, CardioLayout, CountLayout, EndLayout; // 각 운동정보, 유산소 카운트, 세트카운트, 완료 레이아웃
        public TextView CatView, NameView, DetailView;
        public TextView CountView, CardioMView, CardioTxtView;
        public ProgressBar CardioBar;

        public String DetailViewTxt, CountTxt;

        public MainViewHolder(View view) {
            super(view);

            this.ExerciseCard = (CardView) view.findViewById(R.id.exerciseCard);
            this.ExerciseLayout = (LinearLayout) view.findViewById(R.id.exerciseLayout);
            this.CardioLayout = (LinearLayout) view.findViewById(R.id.cardioLayout);
            this.CountLayout = (LinearLayout) view.findViewById(R.id.countLayout);
//            this.EndLayout = (LinearLayout) view.findViewById(R.id.end);

            this.CatView = (TextView) view.findViewById(R.id.exerciseCat);
            this.NameView = (TextView) view.findViewById(R.id.exerciseName);
            this.DetailView = (TextView) view.findViewById(R.id.exerciseDetail);
            this.CountView = (TextView) view.findViewById(R.id.setCount);
            this.CardioMView = (TextView) view.findViewById(R.id.cardioM);
            this.CardioTxtView = (TextView) view.findViewById(R.id.cardioTxt);
            this.CardioBar = (ProgressBar) view.findViewById(R.id.cardioBar);
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
                if (isCount) {
                    int position = mainViewHolder.getAdapterPosition();

                    onExerciseClick.onExerciseClick(position, mainViewHolder.CountView, mainViewHolder.CardioTxtView, mainViewHolder.CardioBar);

                    if (exercises.get(position).getState().equals("유산소")) {
                        if (mainViewHolder.CardioBar.getProgress() == mainViewHolder.CardioBar.getMax())
                            mainViewHolder.EndLayout.setVisibility(View.VISIBLE);
                    } else {
                        String str = (String) mainViewHolder.CountView.getText();
                        String str1 = str.substring(0, str.lastIndexOf("/"));
                        String str2 = str.substring(str.lastIndexOf("/")+1);

                        if (str1.equals(str2))
                            mainViewHolder.EndLayout.setVisibility(View.VISIBLE);
                    } // 운동 set 달성 시 완료 표시
                }
            }
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        setTxt(holder); // DetailViewTxt, CountTxt 초기값 설정

        holder.CatView.setText(this.exercises.get(position).getState()); // 운동 부위
        holder.CatView.setBackgroundColor(Color.parseColor(this.exercises.get(position).getColor())); // 부위 배경색
        holder.NameView.setText(this.exercises.get(position).getTitle()); // 운동 이름
        holder.DetailView.setText(holder.DetailViewTxt);    // 무게 및 세트수

        if (isCount) {
            holder.CountLayout.setVisibility(View.VISIBLE);
            holder.CountView.setText(holder.CountTxt);          // 1/5 or 남은 시간

            if (this.exercises.get(position).getState().equals("유산소")) {
                holder.CardioLayout.setVisibility(View.VISIBLE);
                holder.CardioMView.setVisibility(View.VISIBLE);
                holder.CardioBar.setMax(this.exercises.get(position).getCount() * 60 * 10); // 시간 (프로그레스 바) 일단 초 0.01초
                holder.CardioBar.setProgress(holder.CardioBar.getProgress());
            } else {
                holder.CardioLayout.setVisibility(View.GONE);
                holder.CardioMView.setVisibility(View.GONE);
            }

        } else {
            holder.CountLayout.setVisibility(View.GONE);
        }

//        if (exercises.size() > position) { // 운동 기록에서 광고 없으면 없어도 됨
//
//        }
//        else { // -> exercises.size()+1
//            holder.ExerciseLayout.setVisibility(View.GONE); // 만약 광고 넣으면 광고 자리
//        }
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    } // exercises.size()+1 -> 광고 자리를 위한 +1

    private void setTxt(@NonNull MainViewHolder holder) { // 초기값 설정
        int position = holder.getAdapterPosition();
        int count = this.exercises.get(position).getCount();
        int volume = this.exercises.get(position).getVolume();

        if (holder.DetailViewTxt == null) {
            if (this.exercises.get(position).getState().equals("유산소")) {
                holder.DetailViewTxt = "속도 " + volume;
            } else {
                holder.DetailViewTxt = volume + " kg · " + count + " 세트";
            }
        }

        if (holder.CountTxt == null) {
            if (this.exercises.get(position).getState().equals("유산소"))
                holder.CountTxt = Integer.toString(count);
            else
                holder.CountTxt = "0/" + count;
        }
    }

    public void setOnExerciseClickListener(OnExerciseClick onExerciseClickListener) {
        this.onExerciseClick = onExerciseClickListener;
    } // 액티비티에서 콜백 메서드를 set

    public interface OnExerciseClick {
        void onExerciseClick(int position, TextView CountView, TextView CardioTxtView, ProgressBar CardioBar);
    } // 운동 클릭했을 때, 엑티비티에 값 전달을 위한 인터페이스
}