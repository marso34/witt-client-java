package com.gwnu.witt.WorkOut;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gwnu.witt.Data.Exercise.ExerciseData;
import com.example.healthappttt.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class ExerciseRecordAdapter extends RecyclerView.Adapter<ExerciseRecordAdapter.MainViewHolder>  {
    Context context;

    private static final String Signature = "#05C78C";
    private static final String Box = "#E3E8EF";

    private static ArrayList<ExerciseData> exercises;
    private OnExerciseClick onExerciseClick;

    public ExerciseRecordAdapter(Context context, ArrayList<ExerciseData> exercises) {
        this.context = context;
        this.exercises = exercises;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public MaterialCardView ExerciseCard, NameCard;
        public CardView CatView;
        public TextView NameTxt, DetailTxt, CardioTxt;
        public ProgressBar progressBar;
        public ImageView CardioIcon, EndView;

        public MainViewHolder(View view) {
            super(view);

            this.ExerciseCard  = view.findViewById(R.id.exerciseCard);
            this.NameCard      = view.findViewById(R.id.nameCard);

            this.CatView       = view.findViewById(R.id.exerciseCat);
            this.NameTxt       = view.findViewById(R.id.exerciseName);
            this.DetailTxt     = view.findViewById(R.id.exerciseDetail);
            this.CardioTxt     = view.findViewById(R.id.cardioTxt);

            this.progressBar   = view.findViewById(R.id.progressBar);
            this.CardioIcon    = view.findViewById(R.id.cardioIcon);
            this.EndView       = view.findViewById(R.id.checked);
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exercise_record, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        mainViewHolder.ExerciseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mainViewHolder.getAbsoluteAdapterPosition();

                if (exercises.get(position).getCat() == 0x40) {
                    mainViewHolder.CardioIcon.setColorFilter(Color.parseColor(Signature));
                    mainViewHolder.CardioTxt.setTextColor(Color.parseColor(Signature));
                }

                onExerciseClick.onExerciseClick(position, mainViewHolder.CardioTxt, mainViewHolder.progressBar);

                if (mainViewHolder.progressBar.getProgress() == mainViewHolder.progressBar.getMax()) {
                    mainViewHolder.ExerciseCard.setStrokeWidth(2);
                    mainViewHolder.NameCard.setStrokeWidth(2);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        mainViewHolder.ExerciseCard.setOutlineSpotShadowColor(Color.parseColor(Signature));
                    }
                    mainViewHolder.EndView.setVisibility(View.VISIBLE);
                }
            }
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        String name = this.exercises.get(position).getExerciseName(); // 운동 이름
        String detail = "";
        int SetOrTime = this.exercises.get(position).getSetOrTime();  // 무산소 세트 수, 유산소 시간
        int Volume = this.exercises.get(position).getVolume();        // 무산소 무게, 유산소 강도(속도)
        int CntOrDis = this.exercises.get(position).getCntOrDis();    // 무산소 횟수, 유산소 거리
        int progressCurrentValue = holder.progressBar.getProgress();
        int progressMaxValue = SetOrTime;

        holder.CatView.setCardBackgroundColor(Color.parseColor(this.exercises.get(position).getTextColor())); // 부위 바탕 색
        holder.NameTxt.setText(name);

        if (this.exercises.get(position).getCat() == 0x40) {// 유산소일 경우
            holder.CardioIcon.setVisibility(View.VISIBLE);
            holder.CardioTxt.setVisibility(View.VISIBLE);
            holder.CardioTxt.setText(holder.CardioTxt.getText());

            progressMaxValue = progressMaxValue * 60 * 100;

            if (SetOrTime/60 > 0)
                detail += (SetOrTime/60) + "시간 ";
            if (SetOrTime%60 > 0)
                detail += (SetOrTime%60) + "분";
        } else {
            holder.CardioIcon.setVisibility(View.GONE);
            holder.CardioTxt.setVisibility(View.GONE);

            if (Volume != 0)
                detail += Volume + "kg";
            if (CntOrDis != 0)
                detail += " · " + CntOrDis + "회";
            if (SetOrTime != 0)
                detail += " · " + SetOrTime + "세트";

            SeperatedProgressbar bgProgress = new SeperatedProgressbar(context, Color.parseColor(Signature), Color.parseColor(Box), progressMaxValue);
            holder.progressBar.setProgressDrawable(bgProgress);
        }

        holder.DetailTxt.setText(detail);
        holder.progressBar.setMax(progressMaxValue);
        holder.progressBar.setProgress(progressCurrentValue);

        if (progressCurrentValue == progressMaxValue) { // 운동을 완료했을 경우
            holder.EndView.setVisibility(View.VISIBLE);
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