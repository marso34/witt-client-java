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
import com.example.healthappttt.Data.Rutin;
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

    public ExercizeAdapter(Activity activity, Rutin rutin) { // 일단 테스트
        this.activity = activity;
        this.title = rutin.getTitle();
        this.exercizeArea = rutin.getExerciseArea();
        this.exercizes = new ArrayList<>(rutin.getExercizes());
        this.exercizeCnt = rutin.getExerciezeCount();

        this.recordExercizes = new ArrayList<>();

        for (int i = 0; i < exercizeCnt; i++) {
            recordExercizes.add(new Exercize(exercizes.get(i).getTitle(), exercizes.get(i).getState(), new ArrayList<>()));
        }
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ExercizeLayout;
        public LinearLayout NotesLayout;

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

                    if (setPosition == 0)
                        recordExercizes.get(position).setStartTime(Long.toString(System.currentTimeMillis()));
                    else if (setPosition == exercizes.get(position).getExercizeSetCount()-1)
                        recordExercizes.get(position).setEndTime(Long.toString(System.currentTimeMillis()));
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
            }

            @Override
            public void afterTextChanged(Editable arg0) {
//                mainViewHolder.NotesLayout.setBackgroundResource(R.drawable.border_layout);
                notes = mainViewHolder.Notes.getText().toString();
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
        else { // 여기는 나중에 광고
            holder.NotesLayout.setVisibility(View.GONE);
            holder.ExercizeLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (exercizeCnt+2); // 나중에 +2로
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
            holder.SetViewTxt = "완료";
            holder.DetailViewTxt = "";
        }
    }

    public Rutin getRecord() {
        Rutin record = new Rutin(title, exercizeArea, recordExercizes);
        record.setNotes(notes);

        return record;
    }
}