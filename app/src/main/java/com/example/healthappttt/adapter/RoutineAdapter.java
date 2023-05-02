package com.example.healthappttt.adapter;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.R;

import java.util.ArrayList;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.MainViewHolder> {
    private Context context;
    private ArrayList<Routine> routines;


    public RoutineAdapter(ArrayList<Routine> routines, Context context) {
        this.routines = routines;
        this.context = context;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout RoutineLayout, NullLayout;
        public TextView startTimeView, endTimeView;
        public TextView Edit;
        private RecyclerView recyclerView;
        private ExerciseListAdapter adapter;

        public MainViewHolder(View view) {
            super(view);

            this.RoutineLayout = (LinearLayout) view.findViewById(R.id.routineLayout);
            this.NullLayout = (LinearLayout) view.findViewById(R.id.nullLayout);

            this.startTimeView = (TextView) view.findViewById(R.id.startTime);
            this.endTimeView = (TextView) view.findViewById(R.id.endTime);
            this.Edit = (TextView) view.findViewById(R.id.Edit);

            this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_routine, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        if (routines.size() > 0) {
            holder.RoutineLayout.setVisibility(View.VISIBLE);
            holder.NullLayout.setVisibility(View.GONE);

//            routines에서 시간 정보 가져와서 오전 오후 시간 정보 분리하는 메서드 만들기
            holder.startTimeView.setText(TimeToString(routines.get(position).getStartTime()));
            holder.endTimeView.setText(TimeToString(routines.get(position).getEndTime()));
            setRecyclerView(holder.recyclerView, holder.adapter, routines.get(position));
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

    private void setRecyclerView(RecyclerView recyclerView, ExerciseListAdapter adapter, Routine routine) {
        ArrayList<Exercise> exercises = new ArrayList<>();
//        exercises.add(new Exercise("팔굽혀펴기", 0x1, 3,3, 3));
//        exercises.add(new Exercise("턱걸이", 0x2, 3,3, 3));
//        exercises.add(new Exercise("스쿼트", 0x4, 3,3, 3));
//        exercises.add(new Exercise("플랭크", 0x10, 3,3, 3));


        adapter = new ExerciseListAdapter(exercises, true); // 나중에 routine
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }
}
