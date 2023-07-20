package com.example.healthappttt.Routine;

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

import com.example.healthappttt.Data.Exercise.ExerciseData;
import com.example.healthappttt.R;

import java.util.ArrayList;

public class ExerciseInputAdapter extends RecyclerView.Adapter<ExerciseInputAdapter.MainViewHolder> {
    private ArrayList<ExerciseData> exercises;
    private ArrayList<Integer> deletePk;
    private boolean isEdit;

    public ExerciseInputAdapter(ArrayList<ExerciseData> exercises) { // 드래그 앤 드롭 추가할 것 // ui 전체적으로 수정 필요
        this.exercises = exercises;
        this.isEdit = false;
    }

    public ExerciseInputAdapter(ArrayList<ExerciseData> exercises, ArrayList<Integer> deletePk, boolean isEdit) {
        this.exercises = exercises;
        this.deletePk = deletePk;
        this.isEdit = isEdit;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ExerciseLyout;
        public TextView CatView, NameView;
        public EditText EditVolume, EditCount, EditSet;
        public ImageView RemoveBtn;

        public MainViewHolder(View view) {
            super(view);

            this.ExerciseLyout = view.findViewById(R.id.exerciseCard);

            this.CatView = (TextView) view.findViewById(R.id.exerciseCat);
            this.NameView = (TextView) view.findViewById(R.id.exerciseName);

            this.EditVolume = (EditText) view.findViewById(R.id.inputVoluem);
            this.EditCount = (EditText) view.findViewById(R.id.inputCount);
            this.EditSet = (EditText) view.findViewById(R.id.inputSet);

            this.RemoveBtn = (ImageView) view.findViewById(R.id.removeBtn);
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exercise_input, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        mainViewHolder.RemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = mainViewHolder.getAbsoluteAdapterPosition();
//                exercises.get(position).setID(-1 * exercises.get(position).getID());
                // 인터페이스 구현해서 버튼 누르면 position 전달해서 프래그먼트 or 액티비티에서 removeItem 호출하고 adapter.notifyDataSetChanged() 실행
                removeItem(position);
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

                if (str != null)
                    exercises.get(mainViewHolder.getAbsoluteAdapterPosition()).setVolume(Integer.parseInt(str));
                else
                    exercises.get(mainViewHolder.getAbsoluteAdapterPosition()).setVolume(0);
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

                if (str != null)
                    exercises.get(mainViewHolder.getAbsoluteAdapterPosition()).setCntOrDis(Integer.parseInt(str));
                else
                    exercises.get(mainViewHolder.getAbsoluteAdapterPosition()).setCntOrDis(0);
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

                if (str != null)
                    exercises.get(mainViewHolder.getAbsoluteAdapterPosition()).setSetOrTime(Integer.parseInt(str));
                else
                    exercises.get(mainViewHolder.getAbsoluteAdapterPosition()).setSetOrTime(0);
            }
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.CatView.setText(this.exercises.get(position).getStrCat()); // 운동 부위
        holder.CatView.setTextColor(Color.parseColor(this.exercises.get(position).getTextColor())); // 부위 텍스트 색
        holder.CatView.setBackgroundColor(Color.parseColor(this.exercises.get(position).getColor())); // 부위 바탕 색
        holder.NameView.setText(this.exercises.get(position).getExerciseName()); // 운동 이름

        if (isEdit) { // 운동 수정의 경우, 초기값 할당
            holder.EditVolume.setText(Integer.toString(this.exercises.get(position).getVolume()));
            holder.EditCount.setText(Integer.toString(this.exercises.get(position).getCntOrDis()));
            holder.EditSet.setText(Integer.toString(this.exercises.get(position).getSetOrTime()));
            holder.RemoveBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() { return exercises.size(); }

    public void removeItem(int position) {
        if (exercises.get(position).getID() != 0)
            deletePk.add(exercises.get(position).getID());
        exercises.remove(position);
        notifyItemRemoved(position);
    }
}
