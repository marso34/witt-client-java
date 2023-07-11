package com.example.healthappttt.Routine;

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

import com.example.healthappttt.Data.Exercise.ExerciseData;
import com.example.healthappttt.R;

import java.util.ArrayList;

public class ExerciseListPAdapter extends RecyclerView.Adapter<ExerciseListPAdapter.MainViewHolder> {
    private Context context;
    private ArrayList<ExerciseData> exercises;
    private ArrayList<String> selectExerciseIndex;

    public ExerciseListPAdapter(Context context, ArrayList<ExerciseData> exercises, ArrayList<String> selectExerciseIndex) {
        this.context = context;
        this.exercises = exercises;
        this.selectExerciseIndex = selectExerciseIndex;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ParentLayout;
        public TextView ListCatTxt;
        public RecyclerView recyclerView;
        public ExerciseListAdapter adapter;

        public MainViewHolder(@NonNull View view) {
            super(view);

            this.ParentLayout = view.findViewById(R.id.parentLayout);
            this.ListCatTxt = view.findViewById(R.id.listCat);
            this.recyclerView = view.findViewById(R.id.recyclerView);
        }
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exercise_list_p, parent, false);
        final MainViewHolder mainViewHolder = new MainViewHolder(view);

        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        String str = "?";
        int eCat = 0;

        switch (position) {
            case 0: str = "가슴";   eCat = 0x1;  break;
            case 1: str = "등";     eCat = 0x2;  break;
            case 2: str = "어깨";   eCat = 0x4;  break;
            case 3: str = "하체";   eCat = 0x8;  break;
            case 4: str = "팔";     eCat = 0x10; break;
            case 5: str = "복근";   eCat = 0x20; break;
            case 6: str = "유산소"; eCat = 0x40; break;
        }

        ArrayList<ExerciseData> exerciseList = new ArrayList<>();

        for (ExerciseData e : exercises) {
            if (e.getCat() != eCat)
                continue;
            exerciseList.add(new ExerciseData(e));
        }

        holder.ListCatTxt.setText(str);
        setRecyclerView(holder.recyclerView, holder.adapter, exerciseList);

        if (exerciseList.size() > 0) {
            holder.ParentLayout.setVisibility(View.VISIBLE);
            holder.ListCatTxt.setVisibility(View.VISIBLE);
            holder.recyclerView.setVisibility(View.VISIBLE);
        } else {
            holder.ParentLayout.setVisibility(View.GONE);
            holder.ListCatTxt.setVisibility(View.GONE);
            holder.recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() { return 7; }

    private void setRecyclerView(RecyclerView recyclerView, ExerciseListAdapter adapter, ArrayList<ExerciseData> exerciseList) {
        adapter = new ExerciseListAdapter(exerciseList, selectExerciseIndex);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }
}
