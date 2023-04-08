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

    private OnExerciseClick onExerciseClick;

    public ExerciseAdapter(Routine routine) { // 일단 테스트
        this.exercises = routine.getExercises();
    }

    public ExerciseAdapter(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public CardView ExerciseCard;
        public LinearLayout ExerciseLayout; // 운동 기록에 광고 안 들어가면 삭제
        public LinearLayout EndLayout; // 운동 완료 표시
        public LinearLayout CardioLayout; // 유산소 운동일 때만 표시

        public TextView CatView;
        public TextView NameView;
        public TextView DetailView;
        public TextView CountView;
        public TextView CardioMView;
        public TextView CardioTxtView;
        public ProgressBar CardioBar;

        public String DetailViewTxt;
        public String CountTxt;

        public MainViewHolder(View view) {
            super(view);

            this.ExerciseCard = (CardView) view.findViewById(R.id.exerciseCard);
            this.ExerciseLayout = (LinearLayout) view.findViewById(R.id.exerciseLayout);
            this.EndLayout = (LinearLayout) view.findViewById(R.id.end);
            this.CardioLayout = (LinearLayout) view.findViewById(R.id.cardioLayout);

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
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        if (exercises.size() > position) { // 운동 기록에서 광고 없으면 없어도 됨
            setTxt(holder); // DetailViewTxt, CountTxt 초기값 설정

            holder.CatView.setText(this.exercises.get(position).getState()); // 운동 부위
            holder.CatView.setBackgroundColor(Color.parseColor(this.exercises.get(position).getColor()));
            holder.NameView.setText(this.exercises.get(position).getTitle()); // 운동 이름
            holder.DetailView.setText(holder.DetailViewTxt);    // 무게 및 세트수
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
        }
        else { // -> exercises.size()+1
            holder.ExerciseLayout.setVisibility(View.GONE); // 만약 광고 넣으면 광고 자리
        }
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