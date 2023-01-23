package com.example.healthappttt.adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.healthappttt.Data.Exercize;
import com.example.healthappttt.R;
import com.example.healthappttt.Data.Routine;

import java.util.ArrayList;

public class ExercizeAdapter extends RecyclerView.Adapter<ExercizeAdapter.MainViewHolder>  {
    private ArrayList<Exercize> exercizes;

    private String title;
    private int exercizeCnt;

    private OnExercizeClick onExercizeClick;

    public ExercizeAdapter(Routine routine) { // 일단 테스트
        this.title = routine.getTitle();
        this.exercizes = new ArrayList<>(routine.getExercizes());
        this.exercizeCnt = routine.getExerciezeCount();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public CardView ExercizeCard;
        public LinearLayout ExercizeLayout; // 운동 기록에 광고 안 들어가면 삭제
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

            this.ExercizeCard = (CardView) view.findViewById(R.id.exerciseCard);
            this.ExercizeLayout = (LinearLayout) view.findViewById(R.id.exerciseLayout);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exercize, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        view.findViewById(R.id.exerciseLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mainViewHolder.getAdapterPosition();

                onExercizeClick.onExercizeClick(position, mainViewHolder.CountView, mainViewHolder.AerobicTxtView, mainViewHolder.AerobicBar);

                if (exercizes.get(position).getState().equals("유산소")) {
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
        if (exercizeCnt > position) { // 운동 기록에서 광고 없으면 없어도 됨
            setTxt(holder); // DetailViewTxt, CountTxt 초기값 설정

            holder.CatView.setText(this.exercizes.get(position).getState()); // 운동 부위
            holder.CatView.setBackgroundColor(Color.parseColor(this.exercizes.get(position).getColor()));
            holder.NameView.setText(this.exercizes.get(position).getTitle()); // 운동 이름
            holder.DetailView.setText(holder.DetailViewTxt);    // 무게 및 세트수
            holder.CountView.setText(holder.CountTxt);          // 1/5 or 남은 시간

            if (this.exercizes.get(position).getState().equals("유산소")) {
                holder.AerobicLayout.setVisibility(View.VISIBLE);
                holder.AerobicMView.setVisibility(View.VISIBLE);
                holder.AerobicBar.setMax(this.exercizes.get(position).getCount() * 60); // 시간 (프로그레스 바)
            } else {
                holder.AerobicLayout.setVisibility(View.GONE);
                holder.AerobicMView.setVisibility(View.GONE);
            }
        }
        else { // -> exercizeCnt+1
            holder.ExercizeLayout.setVisibility(View.GONE); // 만약 광고 넣으면 광고 자리
        }
    }

    @Override
    public int getItemCount() {
        return (exercizeCnt);
    } // exercizeCnt+1 -> 광고 자리를 위한 +1

    private void setTxt(@NonNull MainViewHolder holder) { // 초기값 설정
        int position = holder.getAdapterPosition();
        int count = this.exercizes.get(position).getCount();
        int volume = this.exercizes.get(position).getVolume();

        if (holder.DetailViewTxt == null) {
            if (this.exercizes.get(position).getState().equals("유산소")) {
                holder.DetailViewTxt = "속도 " + volume;
            } else {
                holder.DetailViewTxt = volume + " Kg · " + count + " 세트";
            }
        }

        if (holder.CountTxt == null) {
            if (this.exercizes.get(position).getState() == "유산소")
                holder.CountTxt = Integer.toString(count);
            else
                holder.CountTxt = "0/" + count;
        }
    }

    public void setOnExercizeClickListener(OnExercizeClick onExercizeClickListener) {
        this.onExercizeClick = onExercizeClickListener;
    } // 액티비티에서 콜백 메서드를 set

    public interface OnExercizeClick {
        void onExercizeClick(int position, TextView CountView, TextView AerobicTxtView, ProgressBar AerobicBar);
    } // 운동 클릭했을 때, 엑티비티에 값 전달을 위한 인터페이스
}