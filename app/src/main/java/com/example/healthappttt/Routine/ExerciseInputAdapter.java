package com.example.healthappttt.Routine;

import android.content.Context;
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
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Exercise.ExerciseData;
import com.example.healthappttt.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;

public class ExerciseInputAdapter extends RecyclerView.Adapter<ExerciseInputAdapter.MainViewHolder> {
    private Context context;
    private ArrayList<ExerciseData> exercises;
    private ArrayList<Integer> deletePk;
    private boolean isEdit; // 수정인지 생성인지, true면 수정

    public ExerciseInputAdapter(Context context, ArrayList<ExerciseData> exercises) { // 드래그 앤 드롭 추가할 것 // ui 전체적으로 수정 필요
        this.context = context;
        this.exercises = exercises;
        this.isEdit = false;
    }

    public ExerciseInputAdapter(Context context, ArrayList<ExerciseData> exercises, ArrayList<Integer> deletePk, boolean isEdit) {
        this.context = context;
        this.exercises = exercises;
        this.deletePk = deletePk;
        this.isEdit = isEdit;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout DefaultLayout, CardioLayout, VolumeLayout;
        private CardView CatView;
        public ImageView RemoveBtn;
        public TextView NameView, Volume, Count, Set, CardioTime;

        public MainViewHolder(View view) {
            super(view);

            this.DefaultLayout = view.findViewById(R.id.defaultLayout);
            this.CardioLayout = view.findViewById(R.id.cardioLayout);
            this.VolumeLayout = view.findViewById(R.id.volumeLayout);

            this.CatView = view.findViewById(R.id.exerciseCat);
            this.RemoveBtn = view.findViewById(R.id.remove);

            this.NameView   = view.findViewById(R.id.exerciseName);
            this.Volume     = view.findViewById(R.id.volume);
            this.Count      = view.findViewById(R.id.count);
            this.Set        = view.findViewById(R.id.set);
            this.CardioTime = view.findViewById(R.id.cardioTime);
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exercise_input, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        mainViewHolder.RemoveBtn.setOnClickListener(v -> {
            int position = mainViewHolder.getAbsoluteAdapterPosition();
//                exercises.get(position).setID(-1 * exercises.get(position).getID());
            // 인터페이스 구현해서 버튼 누르면 position 전달해서 프래그먼트 or 액티비티에서 removeItem 호출하고 adapter.notifyDataSetChanged() 실행
            removeItem(position);
        });

        mainViewHolder.DefaultLayout.setOnClickListener(v -> {
            // 나중에 맨몸 운동 처리도 할 것
            int position = mainViewHolder.getAbsoluteAdapterPosition();

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);

            View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.input_exercise_detail, null);
            NumberPicker CountPicker = view1.findViewById(R.id.countPicker);
            NumberPicker SetPicker = view1.findViewById(R.id.setPicker);
            NumberPicker VolumePicker = view1.findViewById(R.id.volumePicker);
            TextView SelectBtn = view1.findViewById(R.id.selectBtn);

            bottomSheetDialog.setContentView(view1);

            CountPicker.setMaxValue(50); //최대값
            CountPicker.setMinValue(0); //최소값
            CountPicker.setValue(this.exercises.get(position).getCntOrDis());// 초기값

            SetPicker.setMaxValue(50); //최대값
            SetPicker.setMinValue(0); //최소값
            SetPicker.setValue(this.exercises.get(position).getSetOrTime());// 초기값
//
            VolumePicker.setMaxValue(500); //최대값
            VolumePicker.setMinValue(0); //최소값
            VolumePicker.setValue(this.exercises.get(position).getVolume());// 초기값

            SelectBtn.setOnClickListener(v1 -> {
                mainViewHolder.Count.setText(CountPicker.getValue() + ""); // 스트링으로 바꾸기 편하게
                mainViewHolder.Set.setText(SetPicker.getValue() + "");
                mainViewHolder.Volume.setText(VolumePicker.getValue() + ""); // 스트링으로 바꾸기 편하게

                this.exercises.get(position).setCntOrDis(CountPicker.getValue());
                this.exercises.get(position).setSetOrTime(SetPicker.getValue());
                this.exercises.get(position).setVolume(VolumePicker.getValue());

                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.show();
        });

        mainViewHolder.CardioLayout.setOnClickListener(v -> {
            int position = mainViewHolder.getAbsoluteAdapterPosition();

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);

            View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.input_exercise_detail_cardio, null);
            NumberPicker HourPicker = view1.findViewById(R.id.hourPicker);
            NumberPicker MinutePicker = view1.findViewById(R.id.minutePicker);
            TextView SelectBtn = view1.findViewById(R.id.selectBtn);

            bottomSheetDialog.setContentView(view1);

            HourPicker.setMaxValue(24); //최대값
            HourPicker.setMinValue(0); //최소값
            HourPicker.setValue(this.exercises.get(position).getSetOrTime()/60);// 초기값

            MinutePicker.setMaxValue(60); //최대값
            MinutePicker.setMinValue(0); //최소값
            MinutePicker.setValue(this.exercises.get(position).getSetOrTime()%60);// 초기값

            SelectBtn.setOnClickListener(v1 -> {
                String result = "";

                if (HourPicker.getValue() != 0)
                    result += HourPicker.getValue() + "시간 ";
                if (MinutePicker.getValue() != 0)
                    result += MinutePicker.getValue() + "분";
                if (HourPicker.getValue()  == 0 && MinutePicker.getValue() == 0)
                    result = "0";

                this.exercises.get(position).setSetOrTime(HourPicker.getValue() * 60 + MinutePicker.getValue());

                mainViewHolder.CardioTime.setText(result); // 스트링으로 바꾸기 편하게
                bottomSheetDialog.dismiss();
            });

            bottomSheetDialog.show();

        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.CatView.setCardBackgroundColor(Color.parseColor(this.exercises.get(position).getTextColor())); // 부위 바탕 색
        holder.NameView.setText(this.exercises.get(position).getExerciseName()); // 운동 이름
//
        holder.Volume.setText(Integer.toString(this.exercises.get(position).getVolume()));
        holder.Count.setText(Integer.toString(this.exercises.get(position).getCntOrDis()));
        holder.Set.setText(Integer.toString(this.exercises.get(position).getSetOrTime()));
        holder.CardioTime.setText(Integer.toString(this.exercises.get(position).getSetOrTime()));
//
        if (this.exercises.get(position).getCat() == 0x40) { // 유산소
            holder.CardioLayout.setVisibility(View.VISIBLE);
            holder.DefaultLayout.setVisibility(View.GONE);
        } else {
            holder.CardioLayout.setVisibility(View.GONE);
            holder.DefaultLayout.setVisibility(View.VISIBLE);

//            if (this.exercises.get(position).get??? == ???) { // 무게 없는 맨몸 운동의 경우
//                holder.VolumeLayout.setVisibility(View.GONE);
//            }
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
