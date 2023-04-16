package com.example.healthappttt.adapter;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ExerciseInputAdapter extends RecyclerView.Adapter<ExerciseInputAdapter.MainViewHolder> {
    private ArrayList<Exercise> exercises;
    private InputExerciseDetail inputExerciseDetail;


    public ExerciseInputAdapter(ArrayList<Exercise> exercises) {
        this.exercises = exercises;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public TextView CatView, NameView;
        public EditText EditVolume, EditCount, EditSet;

        public MainViewHolder(View view) {
            super(view);

            this.CatView = (TextView) view.findViewById(R.id.exerciseCat);
            this.NameView = (TextView) view.findViewById(R.id.exerciseName);

            this.EditVolume = (EditText) view.findViewById(R.id.inputWeight);
            this.EditCount = (EditText) view.findViewById(R.id.inputCount);
            this.EditSet = (EditText) view.findViewById(R.id.inputSet);
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exercise_input, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        mainViewHolder.EditVolume.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = mainViewHolder.EditVolume.getText().toString();

                if (str != null) {
                    exercises.get(mainViewHolder.getAbsoluteAdapterPosition()).setVolume(Integer.parseInt(str));
                    inputExerciseDetail.inputExerciseDetail(exercises);
                }
            }
        });

        mainViewHolder.EditCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = mainViewHolder.EditCount.getText().toString();

                if (str != null) {
                    exercises.get(mainViewHolder.getAbsoluteAdapterPosition()).setNum(Integer.parseInt(str));
                    inputExerciseDetail.inputExerciseDetail(exercises);
                }
            }
        });

        mainViewHolder.EditSet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = mainViewHolder.EditSet.getText().toString();

                if (str != null) {
                    exercises.get(mainViewHolder.getAbsoluteAdapterPosition()).setCount(Integer.parseInt(str));
                    inputExerciseDetail.inputExerciseDetail(exercises);
                }
            }
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.CatView.setText(this.exercises.get(position).getState()); // 운동 부위
        holder.CatView.setTextColor(Color.parseColor(this.exercises.get(position).getTextColor())); // 부위 텍스트 색
        holder.CatView.setBackgroundColor(Color.parseColor(this.exercises.get(position).getColor())); // 부위 바탕 색
        holder.NameView.setText(this.exercises.get(position).getTitle()); // 운동 이름
    }

    @Override
    public int getItemCount() { return exercises.size(); }

    public void removeItem(int position) { exercises.remove(position); }


    public void setinputExerciseListener(InputExerciseDetail inputExerciseDetail) {
        this.inputExerciseDetail = inputExerciseDetail;
    } // 액티비티에서 콜백 메서드를 set

    public interface InputExerciseDetail {
        void inputExerciseDetail(ArrayList<Exercise> exercises);
    } // 운동 클릭했을 때, 엑티비티에 값 전달을 위한 인터페이스
}
