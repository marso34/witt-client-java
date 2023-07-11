package com.example.healthappttt.Routine;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Exercise.ExerciseData;
import com.example.healthappttt.Data.Exercise.ExerciseComparator;
import com.example.healthappttt.Data.Exercise.RoutineData;
import com.example.healthappttt.R;

import java.util.ArrayList;
import java.util.Collections;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.MainViewHolder> {
    private Context context;
    private ArrayList<RoutineData> routines;

    private int attribute; // == 0 내 루틴, attribute > 0 운동 기록할 때 선택용, attribute < 0 남의 루틴

    private OnClickRoutine onClickRoutine;

    /**
     * @attribute attribute(운동 기록) > 0  or attribute == 0(내 루틴) or attribute < 0(다른 사람 루틴)
     */
    public RoutineAdapter(Context context, ArrayList<RoutineData> routines, int attribute) {
        this.routines = routines;
        this.context = context;
        this.attribute = attribute;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout RoutineLayout, ClickLayout, NullLayout;
        public TextView startTimeView, endTimeView;
        public ImageView EditBtn;
        public RecyclerView recyclerView;
        public ExerciseAdapter adapter;

        public MainViewHolder(View view) {
            super(view);

            this.RoutineLayout = view.findViewById(R.id.routineLayout);
            this.ClickLayout = view.findViewById(R.id.clickLayout);
            this.NullLayout = view.findViewById(R.id.nullLayout);

            this.startTimeView = view.findViewById(R.id.startTime);
            this.endTimeView = view.findViewById(R.id.endTime);
            this.EditBtn = view.findViewById(R.id.editBtn);

            this.recyclerView = view.findViewById(R.id.recyclerView);
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_routine, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        mainViewHolder.ClickLayout.setOnClickListener(v -> {
            if (attribute > 0) {
                int position = mainViewHolder.getAbsoluteAdapterPosition();

                onClickRoutine.onClickRoutine(routines.get(position));
            }
        });

        mainViewHolder.EditBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View dialogView = LayoutInflater.from(parent.getContext()).inflate(R.layout.routine_edit_popup, null);

            builder.setView(dialogView);
            AlertDialog alertDialog  = builder.create();
            alertDialog.getWindow().setGravity(Gravity.BOTTOM);   //다이얼로그 하단
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();



//            int position = mainViewHolder.getAbsoluteAdapterPosition();
//
//            onClickRoutine.onClickRoutine(routines.get(position));
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        if (routines.size() > 0) {
            holder.RoutineLayout.setVisibility(View.VISIBLE);
            holder.NullLayout.setVisibility(View.GONE);
            holder.ClickLayout.setVisibility(View.GONE);

            holder.startTimeView.setText(TimeToString(routines.get(position).getStartTime()));
            holder.endTimeView.setText(TimeToString(routines.get(position).getEndTime()));

            if (attribute == 0) { // 내 루틴

            } else if (attribute > 0) { // 운동 기록
                holder.ClickLayout.setVisibility(View.VISIBLE);
                holder.EditBtn.setVisibility(View.GONE);
            } else { // 다른 사람 루틴
                holder.EditBtn.setVisibility(View.GONE);
            }

            setRecyclerView(holder.recyclerView, holder.adapter, position);

        } else {
            holder.RoutineLayout.setVisibility(View.GONE);
            holder.NullLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (routines.size() == 0)   return 1;

        return routines.size();
    }

    public void removeItem(int routineID) {
        for (RoutineData r : routines) {
            if (r.getID() == routineID) {
                int position = routines.indexOf(r);
                routines.remove(position);
                notifyItemRemoved(position);

                break;
            }
        }
    }

    private String TimeToString(String Time) {
        String[] TimeSplit = Time.split(":");
        String am_pm = "";

        int hour = Integer.parseInt(TimeSplit[0]);

        if (hour < 12) {
            am_pm = "오전";
            if (hour == 0) hour = 12;
        } else {
            am_pm = "오후";
            if (hour >= 13) hour-= 12;
        }

        return am_pm + " " + hour + ":" + TimeSplit[1];
    }

    private void setRecyclerView(RecyclerView recyclerView, ExerciseAdapter adapter, int positon) {
        ArrayList<ExerciseData> e = (ArrayList<ExerciseData>) routines.get(positon).getExercises();
        Collections.sort(e, new ExerciseComparator());

        adapter = new ExerciseAdapter(e); // 나중에 routine
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

    public void setOnClickRoutineListener(OnClickRoutine onClickRoutineListener) {
        this.onClickRoutine = onClickRoutineListener;
    } // 액티비티에서 콜백 메서드를 set

    public interface OnClickRoutine {
        void onClickRoutine(RoutineData r);
    } // 운동 클릭했을 때, 엑티비티에 값 전달을 위한 인터페이스
}
