package com.example.healthappttt.adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.healthappttt.Data.Exercize;
import com.example.healthappttt.R;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.Data.Set;

import java.util.ArrayList;

public class ExercizeAdapter extends RecyclerView.Adapter<ExercizeAdapter.MainViewHolder>  {
    private Activity activity;
    private ArrayList<Exercize> exercizes;
    private ArrayList<Exercize> recordExercizes;

    private String title;
    private String exercizeArea;
    private String notes;
    private int exercizeCnt;

    private OnExercizeClick onExercizeClick;

    public ExercizeAdapter(Activity activity, Routine routine) { // 일단 테스트
        this.activity = activity;
        this.title = routine.getTitle();
        this.exercizeArea = routine.getExercizeCategories();
        this.exercizes = new ArrayList<>(routine.getExercizes());
        this.exercizeCnt = routine.getExerciezeCount();

        this.recordExercizes = new ArrayList<>();

        for (int i = 0; i < exercizeCnt; i++) {
            recordExercizes.add(new Exercize(exercizes.get(i).getTitle(), exercizes.get(i).getState(), new ArrayList<>()));
        }
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ExercizeLayout;
        public LinearLayout NotesLayout;
        public LinearLayout EndLayout;

        public TextView NameView;
        public TextView SetView;
        public ProgressBar SetBar;
        public TextView DetailView;

        public EditText Notes;

        public String SetViewTxt;
        public String DetailViewTxt;

        public ArrayList<Set> exercizeSet;

        public MainViewHolder(View view) {
            super(view);

            this.ExercizeLayout = (LinearLayout) view.findViewById(R.id.exerciseLayout);
            this.NotesLayout = (LinearLayout) view.findViewById(R.id.notesLayout);
            this.EndLayout = (LinearLayout) view.findViewById(R.id.end);

            this.NameView = (TextView) view.findViewById(R.id.exerciseName);
            this.SetView = (TextView) view.findViewById(R.id.setCountNum);
            this.SetBar = (ProgressBar) view.findViewById(R.id.setBar);
            this.DetailView = (TextView) view.findViewById(R.id.exerciseDetail);

            this.Notes = (EditText) view.findViewById(R.id.exerciseNote);

            this.exercizeSet = new ArrayList<>();
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
                int setPosition = mainViewHolder.exercizeSet.size();

                if (setPosition < exercizes.get(position).getExercizeSetCount()) {
                    mainViewHolder.exercizeSet.add(exercizes.get(position).getExercizeSet().get(setPosition));
                    recordExercizes.get(position).setExercizeSet(mainViewHolder.exercizeSet);

                    if (setPosition == 0) // 처음 눌렀을 때는 1세트가 완료됐을 때라 첫 세트의 운동 시간을 알 수 없음. 1세트만 운동했을 때도 문제. 나중에 고민할 것
                        recordExercizes.get(position).setStartTime(Long.toString(System.currentTimeMillis()));

                    recordExercizes.get(position).setEndTime(Long.toString(System.currentTimeMillis()));
                    onExercizeClick.onExercizeClick(recordExercizes, notes);
                }

                setString(mainViewHolder);

                mainViewHolder.SetBar.setProgress(mainViewHolder.exercizeSet.size());   // 현재 세트 수 (프로그레스바)
                mainViewHolder.SetView.setText(mainViewHolder.SetViewTxt);          // 현재 세트 -> Set n 에 해당
                mainViewHolder.DetailView.setText(mainViewHolder.DetailViewTxt);    // 무게 및 세트당 횟수
            }
        });

        mainViewHolder.Notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                mainViewHolder.NotesLayout.setBackgroundResource(R.drawable.blue_border_layout);
                notes = mainViewHolder.Notes.getText().toString();
                onExercizeClick.onExercizeClick(recordExercizes, notes);
            }

            @Override
            public void afterTextChanged(Editable arg0) {
//                mainViewHolder.NotesLayout.setBackgroundResource(R.drawable.border_layout);
            }
        } );

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        if (exercizeCnt > position) {
            holder.NotesLayout.setVisibility(View.GONE);

            setString(holder);

            holder.NameView.setText(this.exercizes.get(position).getTitle()); // 운동 이름
            holder.SetBar.setMax(this.exercizes.get(position).getExercizeSetCount()); // 전체 세트 수 (프로그레스바)

            holder.SetBar.setProgress(holder.exercizeSet.size());   // 현재 세트 수 (프로그레스바)
            holder.SetView.setText(holder.SetViewTxt);          // 현재 세트 -> Set n 에 해당
            holder.DetailView.setText(holder.DetailViewTxt);    // 무게 및 세트당 횟수
        }
        else if (exercizeCnt+1 == position){ // -> length+1
            holder.NotesLayout.setVisibility(View.VISIBLE);
            holder.ExercizeLayout.setVisibility(View.GONE);
        }
        else { // 여기는 나중에 광고. 지금은 그냥 비운 상태
            holder.NotesLayout.setVisibility(View.GONE);
            holder.ExercizeLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (exercizeCnt+2);
    }

    private void setString(@NonNull MainViewHolder holder) {
        int position = holder.getAdapterPosition();
        int setPosition = holder.exercizeSet.size();

        if (setPosition < this.exercizes.get(position).getExercizeSetCount())
        {
            String weight = this.exercizes.get(position).getExercizeSet().get(setPosition).getWeight();
            String count =  this.exercizes.get(position).getExercizeSet().get(setPosition).getCount();

            holder.SetViewTxt = String.format("Set %d", setPosition + 1);
            holder.DetailViewTxt = weight + "Kg X " + count + "개";
        } else {
            holder.EndLayout.setVisibility(View.VISIBLE);
            holder.SetViewTxt = "완료";
            holder.DetailViewTxt = "";
        }
    }

    public void setOnExercizeClickListener(OnExercizeClick onExercizeClickListener) { // 액티비티에서 콜백 메서드를 set
        this.onExercizeClick = onExercizeClickListener;
    }

    public interface OnExercizeClick { // 운동 클릭했을 때, 엑티비티에 값 전달을 위한 인터페이스
        void onExercizeClick(ArrayList<Exercize> exercizes, String notes);
    }
}