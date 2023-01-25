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

    private String title;
    private int exerciseCnt;

    private OnExerciseClick onExerciseClick;

    public ExerciseAdapter(Routine routine) { // 일단 테스트
        this.title = routine.getTitle();
        this.exercises = new ArrayList<>(routine.getExercises());
        this.exerciseCnt = routine.getExercieseCount();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public CardView ExerciseCard;
        public LinearLayout ExerciseLayout; // 운동 기록에 광고 안 들어가면 삭제
        public LinearLayout EndLayout; // 운동 완료 표시
        public LinearLayout AerobicLayout; // 유산소 운동일 때만 표시

        public TextView CatView;
        public TextView NameView;
        public TextView DetailView;
        public TextView CountView;
        public TextView AerobicMView;
        public TextView AerobicTxtView;
        public ProgressBar AerobicBar;

        public String DetailViewTxt;
        public String CountTxt;

        public MainViewHolder(View view) {
            super(view);

            this.ExerciseCard = (CardView) view.findViewById(R.id.exerciseCard);
            this.ExerciseLayout = (LinearLayout) view.findViewById(R.id.exerciseLayout);
            this.EndLayout = (LinearLayout) view.findViewById(R.id.end);
            this.AerobicLayout = (LinearLayout) view.findViewById(R.id.AerobicLayout);

            this.CatView = (TextView) view.findViewById(R.id.exerciseCat);
            this.NameView = (TextView) view.findViewById(R.id.exerciseName);
            this.DetailView = (TextView) view.findViewById(R.id.exerciseDetail);
            this.CountView = (TextView) view.findViewById(R.id.setCount);
            this.AerobicMView = (TextView) view.findViewById(R.id.AerobicM);
            this.AerobicTxtView = (TextView) view.findViewById(R.id.AerobicTxt);
            this.AerobicBar = (ProgressBar) view.findViewById(R.id.AerobicBar);
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exercise, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        view.findViewById(R.id.exerciseLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mainViewHolder.getAdapterPosition();

                onExerciseClick.onExerciseClick(position, mainViewHolder.CountView, mainViewHolder.AerobicTxtView, mainViewHolder.AerobicBar);

                if (exercises.get(position).getState().equals("유산소")) {
                    if (mainViewHolder.AerobicBar.getProgress() == mainViewHolder.AerobicBar.getMax())
                        mainViewHolder.EndLayout.setVisibility(View.VISIBLE);
                } else {
                    String str = (String) mainViewHolder.CountView.getText();
                    String str1 = str.substring(0, str.lastIndexOf("/"));
                    String str2 = str.substring(str.lastIndexOf("/")+1);

                    if (str1.equals(str2))
                        mainViewHolder.EndLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        if (exerciseCnt > position) { // 운동 기록에서 광고 없으면 없어도 됨
            setTxt(holder); // DetailViewTxt, CountTxt 초기값 설정

            holder.CatView.setText(this.exercises.get(position).getState()); // 운동 부위
            holder.CatView.setBackgroundColor(Color.parseColor(this.exercises.get(position).getColor()));
            holder.NameView.setText(this.exercises.get(position).getTitle()); // 운동 이름
            holder.DetailView.setText(holder.DetailViewTxt);    // 무게 및 세트수
            holder.CountView.setText(holder.CountTxt);          // 1/5 or 남은 시간

            if (this.exercises.get(position).getState().equals("유산소")) {
                holder.AerobicLayout.setVisibility(View.VISIBLE);
                holder.AerobicMView.setVisibility(View.VISIBLE);
                holder.AerobicBar.setMax(this.exercises.get(position).getCount() * 60); // 시간 (프로그레스 바)
            } else {
                holder.AerobicLayout.setVisibility(View.GONE);
                holder.AerobicMView.setVisibility(View.GONE);
            }
        }
        else { // -> exercizeCnt+1
            holder.ExerciseLayout.setVisibility(View.GONE); // 만약 광고 넣으면 광고 자리
        }
    }

    @Override
    public int getItemCount() {
        return (exerciseCnt);
    } // exercizeCnt+1 -> 광고 자리를 위한 +1

    private void setTxt(@NonNull MainViewHolder holder) { // 초기값 설정
        int position = holder.getAdapterPosition();
        int count = this.exercises.get(position).getCount();
        int volume = this.exercises.get(position).getVolume();

        if (holder.DetailViewTxt == null) {
            if (this.exercises.get(position).getState().equals("유산소")) {
                holder.DetailViewTxt = "속도 " + volume;
            } else {
                holder.DetailViewTxt = volume + " Kg · " + count + " 세트";
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
        void onExerciseClick(int position, TextView CountView, TextView AerobicTxtView, ProgressBar AerobicBar);
    } // 운동 클릭했을 때, 엑티비티에 값 전달을 위한 인터페이스
}