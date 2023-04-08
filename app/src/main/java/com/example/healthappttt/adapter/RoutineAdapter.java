package com.example.healthappttt.adapter;

import static java.security.AccessController.getContext;

import android.content.Context;
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
        public TextView am_pm1, am_pm2, time1, time2;
        public TextView Edit;
        private RecyclerView recyclerView;
        private ExerciseAdapter adapter;

        public MainViewHolder(View view) {
            super(view);

            this.RoutineLayout = (LinearLayout) view.findViewById(R.id.routineLayout);
            this.NullLayout = (LinearLayout) view.findViewById(R.id.nullLayout);

            this.am_pm1 = (TextView) view.findViewById(R.id.am_pm1);
            this.am_pm2 = (TextView) view.findViewById(R.id.am_pm2);
            this.time1 = (TextView) view.findViewById(R.id.time1);
            this.time2 = (TextView) view.findViewById(R.id.time2);
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
//            holder.am_pm1.setText(routines.get(position).getStartTime());
//            holder.time1.setText(routines.get(position).getStartTime());
//            holder.am_pm2.setText(routines.get(position).getStartTime());
//            holder.time2.setText(routines.get(position).getStartTime());
//            setRecyclerView(holder.recyclerView, holder.adapter, routines.get(position));
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

    private void setRecyclerView(RecyclerView recyclerView, ExerciseAdapter adapter, Routine routine) {
        adapter = new ExerciseAdapter(routine); // 나중에 routine
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }
}
