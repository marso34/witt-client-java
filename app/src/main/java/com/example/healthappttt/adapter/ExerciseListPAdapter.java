package com.example.healthappttt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.ExerciseData;
import com.example.healthappttt.R;

import java.util.ArrayList;

public class ExerciseListPAdapter extends RecyclerView.Adapter<ExerciseListPAdapter.MainViewHolder> {
    private Context context;
    private ArrayList<ExerciseData> exercises;

    public ArrayList<ExerciseData> selectExercises;

    public ExerciseListPAdapter(Context context, ArrayList<ExerciseData> exercises) {
        this.context = context;
        this.exercises = exercises;
        selectExercises = new ArrayList<>();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public TextView ListCatTxt;
        public RecyclerView recyclerView;
        public ExerciseListAdapter adapter;

        public MainViewHolder(@NonNull View view) {
            super(view);

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

        holder.ListCatTxt.setText(str);
        setRecyclerView(holder.recyclerView, holder.adapter, eCat);
    }

    @Override
    public int getItemCount() { return 7; }

    private void setRecyclerView(RecyclerView recyclerView, ExerciseListAdapter adapter, int cat) {
        ArrayList<ExerciseData> exerciseList = new ArrayList<>();

        for (ExerciseData e : exercises) {
            if (e.getCat() != cat)
                continue;

            exerciseList.add(new ExerciseData(e));
        }

        adapter = new ExerciseListAdapter(exerciseList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        if (adapter != null) {

        }
    }
}
