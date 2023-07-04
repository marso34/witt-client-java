package com.example.healthappttt.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Activity.EditRoutineActivity;
import com.example.healthappttt.Data.ExerciseData;
import com.example.healthappttt.Data.ExerciseComparator;
import com.example.healthappttt.Data.RoutineData;
import com.example.healthappttt.R;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collections;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.MainViewHolder> {
    private Context context;
    private ArrayList<RoutineData> routines;

    private boolean isRecoding;

    private OnClickRoutine onClickRoutine;

    public RoutineAdapter(Context context, ArrayList<RoutineData> routines) {
        this.routines = routines;
        this.context = context;
        this.isRecoding = false;
    }

    public RoutineAdapter(Context context, ArrayList<RoutineData> routines, boolean isRecoding) {
        this.routines = routines;
        this.context = context;
        this.isRecoding = isRecoding;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout RoutineLayout, ClickLayout, NullLayout;
        public TextView startTimeView, endTimeView;
        public TextView EditBtn;
        private RecyclerView recyclerView;
        private ExerciseAdapter adapter;

        public MainViewHolder(View view) {
            super(view);

            this.RoutineLayout = (LinearLayout) view.findViewById(R.id.routineLayout);
            this.ClickLayout = (LinearLayout) view.findViewById(R.id.clickLayout);
            this.NullLayout = (LinearLayout) view.findViewById(R.id.nullLayout);

            this.startTimeView = (TextView) view.findViewById(R.id.startTime);
            this.endTimeView = (TextView) view.findViewById(R.id.endTime);
            this.EditBtn = (TextView) view.findViewById(R.id.editBtn);

            this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_routine, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        mainViewHolder.ClickLayout.setOnClickListener(v -> {
            if (isRecoding) {
                int position = mainViewHolder.getAbsoluteAdapterPosition();

                onClickRoutine.onClickRoutine(routines.get(position));
            }
        });

        mainViewHolder.EditBtn.setOnClickListener(v -> {
            int position = mainViewHolder.getAbsoluteAdapterPosition();

            onClickRoutine.onClickRoutine(routines.get(position));
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        if (routines.size() > 0) {
            holder.RoutineLayout.setVisibility(View.VISIBLE);
            holder.ClickLayout.setVisibility(View.GONE);
            holder.NullLayout.setVisibility(View.GONE);
            holder.startTimeView.setText(TimeToString(routines.get(position).getStartTime()));
            holder.endTimeView.setText(TimeToString(routines.get(position).getEndTime()));

            setRecyclerView(holder.recyclerView, holder.adapter, position);

            if (isRecoding) {
                holder.EditBtn.setVisibility(View.GONE);
                holder.ClickLayout.setVisibility(View.VISIBLE);
            }
        } else {
            holder.RoutineLayout.setVisibility(View.GONE);
            holder.ClickLayout.setVisibility(View.GONE);
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
