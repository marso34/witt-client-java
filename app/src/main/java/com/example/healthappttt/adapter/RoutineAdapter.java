package com.example.healthappttt.adapter;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Activity.CreateRoutineActivity;
import com.example.healthappttt.Activity.EditRoutineActivity;
import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.ExerciseComparator;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.Data.RoutineComparator;
import com.example.healthappttt.Data.SQLiteUtil;
import com.example.healthappttt.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.MainViewHolder> {
    private Context context;
    private ArrayList<Routine> routines;
    private SQLiteUtil sqLiteUtil;


    public RoutineAdapter(Context context, ArrayList<Routine> routines) {
        this.routines = routines;
        this.context = context;

        sqLiteUtil = SQLiteUtil.getInstance();
        sqLiteUtil.setInitView(context, "EX_TB");
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout RoutineLayout, NullLayout;
        public TextView startTimeView, endTimeView;
        public TextView EditBtn;
        private RecyclerView recyclerView;
        private ExerciseListAdapter adapter;

        public MainViewHolder(View view) {
            super(view);

            this.RoutineLayout = (LinearLayout) view.findViewById(R.id.routineLayout);
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

        mainViewHolder.EditBtn.setOnClickListener(v -> {
            int position = mainViewHolder.getAbsoluteAdapterPosition();

            int id = routines.get(position).getID();

            Intent intent = new Intent(context, EditRoutineActivity.class);
            intent.putExtra("routineID", id);
            context.startActivity(intent);
        });

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        if (routines.size() > 0) {
            holder.RoutineLayout.setVisibility(View.VISIBLE);
            holder.NullLayout.setVisibility(View.GONE);
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
        exercises = sqLiteUtil.SelectExercise(routine.getID());
        Collections.sort(exercises, new ExerciseComparator());

        adapter = new ExerciseListAdapter(exercises, true); // 나중에 routine
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }
}
