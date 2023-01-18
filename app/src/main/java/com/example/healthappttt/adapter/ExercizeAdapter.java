package com.example.healthappttt.adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.healthappttt.Data.Exercize;
import com.example.healthappttt.R;
import com.example.healthappttt.Data.Routine;

import java.util.ArrayList;

public class ExercizeAdapter extends RecyclerView.Adapter<ExercizeAdapter.MainViewHolder>  {
    private Activity activity;
    private ArrayList<Exercize> exercizes;

    private String title;
    private String exercizeCategories;
    private int exercizeCnt;

    private OnExercizeClick onExercizeClick;

    public ExercizeAdapter(Activity activity, Routine routine) { // 일단 테스트
        this.activity = activity;
        this.title = routine.getTitle();
        this.exercizeCategories = routine.getExercizeCategories();
        this.exercizes = new ArrayList<>(routine.getExercizes());
        this.exercizeCnt = routine.getExerciezeCount();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout ExercizeLayout;
        public LinearLayout EndLayout;

        public TextView NameView;
        public TextView DetailView;
        public TextView CountView;
        public ProgressBar SetBar;

        public String DetailViewTxt;
        public String CountTxt;

        public MainViewHolder(View view) {
            super(view);

            this.ExercizeLayout = (RelativeLayout) view.findViewById(R.id.exerciseLayout);
            this.EndLayout = (LinearLayout) view.findViewById(R.id.end);

            this.NameView = (TextView) view.findViewById(R.id.exerciseName);
            this.DetailView = (TextView) view.findViewById(R.id.exerciseDetail);
            this.CountView = (TextView) view.findViewById(R.id.setCount);
            this.SetBar = (ProgressBar) view.findViewById(R.id.setBar);
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exercize, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(cardView);

        cardView.findViewById(R.id.exerciseLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mainViewHolder.getAdapterPosition();
//                int setPosition = mainViewHolder.exercizeSet.size();
//
//                if (setPosition < exercizes.get(position).getExercizeSetCount()) {
//                    mainViewHolder.exercizeSet.add(exercizes.get(position).getExercizeSet().get(setPosition));
//                    recordExercizes.get(position).setExercizeSet(mainViewHolder.exercizeSet);
//
//                    if (setPosition == 0) // 처음 눌렀을 때는 1세트가 완료됐을 때라 첫 세트의 운동 시간을 알 수 없음. 1세트만 운동했을 때도 문제. 나중에 고민할 것
//                        recordExercizes.get(position).setStartTime(Long.toString(System.currentTimeMillis()));
//
//                    recordExercizes.get(position).setEndTime(Long.toString(System.currentTimeMillis()));
                    onExercizeClick.onExercizeClick(position, mainViewHolder.CountView);
//                }
//
//                setString(mainViewHolder);
//
//                mainViewHolder.SetBar.setProgress(mainViewHolder.exercizeSet.size());   // 현재 세트 수 (프로그레스바)
//                mainViewHolder.CountView.setText(mainViewHolder.SetViewTxt);          // 현재 세트 -> Set n 에 해당
//                mainViewHolder.DetailView.setText(mainViewHolder.DetailViewTxt);    // 무게 및 세트당 횟수
            }
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        if (exercizeCnt > position) {
            setTxt(holder); // DetailViewTxt, CountTxt 초기값 설정
//
            holder.NameView.setText(this.exercizes.get(position).getTitle()); // 운동 이름
            holder.DetailView.setText(holder.DetailViewTxt);    // 무게 및 세트수
            holder.CountView.setText(holder.CountTxt);          // 1/5 or 남은 시간

            if (this.exercizes.get(position).getState() == "유산소") {
                holder.SetBar.setVisibility(View.VISIBLE);
                holder.SetBar.setMax(this.exercizes.get(position).getExercizeSetCount()); // 전체 세트 수 (프로그레스바)
//                holder.SetBar.setProgress(holder.exercizeSet.size());   // 현재 세트 수 (프로그레스바)
            } else {
                holder.SetBar.setVisibility(View.GONE);
            }
        }
        else { // -> exercizeCnt+1
            holder.ExercizeLayout.setVisibility(View.GONE); // 만약 광고 넣으면 광고 자리
        }                                                   // 안 넣으면 아예 삭제

        onExercizeClick.onExercizeClick(position, holder.CountView);
    }

    @Override
    public int getItemCount() {
        return (exercizeCnt);
    } // exercizeCnt+1 -> 광고 자리를 위한 +1

    private void setTxt(@NonNull MainViewHolder holder) { // 초기값 설정
        int position = holder.getAdapterPosition();
        String str = this.exercizes.get(position).getCount();

        if (holder.DetailViewTxt == null) {
            if (this.exercizes.get(position).getState() == "근력")
                holder.DetailViewTxt = str.replace(":", "Kg ") + "세트";
            else
                holder.DetailViewTxt = str; // 나중에 몇 분 몇 초 식으로 변경할 수도 있음
            this.exercizes.get(position).getCount();
        }

        if (holder.CountTxt == null) {
            if (this.exercizes.get(position).getState() == "근력")
                holder.CountTxt = "0 / " + str.substring(str.lastIndexOf(":")+1); // (0 / 총 세트수)
            else
                holder.CountTxt = str; // 남은 시간 or 현재 시간
        }

        // 좀 더 알아보기 쉽고 간단한 방법 고려할 것

//        if (setPosition < this.exercizes.get(position).getExercizeSetCount())
//        {
//            String weight = this.exercizes.get(position).getExercizeSet().get(setPosition).getWeight();
//            String count =  this.exercizes.get(position).getExercizeSet().get(setPosition).getCount();
//
//            holder.SetViewTxt = String.format("Set %d", setPosition + 1);
//            holder.DetailViewTxt = weight + "Kg X " + count + "개";
//        } else {
//            holder.EndLayout.setVisibility(View.VISIBLE);
//            holder.SetViewTxt = "완료";
//            holder.DetailViewTxt = "";
//        }
    }

    public void setOnExercizeClickListener(OnExercizeClick onExercizeClickListener) { // 액티비티에서 콜백 메서드를 set
        this.onExercizeClick = onExercizeClickListener;
    }

    public interface OnExercizeClick { // 운동 클릭했을 때, 엑티비티에 값 전달을 위한 인터페이스
        void onExercizeClick(int position, TextView CountView);
    }
}