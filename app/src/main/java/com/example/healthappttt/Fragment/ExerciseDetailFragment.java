package com.example.healthappttt.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.ExerciseInputAdapter;
import com.example.healthappttt.adapter.ExerciseListAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExerciseDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseDetailFragment extends Fragment {
    private TextView ResetBtn, ScheduleTxt;
    private CardView NextBtn;
    private TextView NextTxt;

    private RecyclerView recyclerView;
    private ExerciseInputAdapter adapter;

    private ArrayList<Exercise> exercises;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onRoutineExDetail(ArrayList<Exercise> exercises);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public ExerciseDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExerciseDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExerciseDetailFragment newInstance(String param1, String param2) {
        ExerciseDetailFragment fragment = new ExerciseDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_exercise_detail, container, false);

        ResetBtn = view.findViewById(R.id.reset);
        ScheduleTxt = view.findViewById(R.id.schedule);
        recyclerView = view.findViewById(R.id.recyclerView);

        NextBtn = view.findViewById(R.id.nextBtn);
        NextTxt = view.findViewById(R.id.nextTxt);

        exercises = new ArrayList<>();

        if (getArguments() != null) {
            exercises = (ArrayList<Exercise>) getArguments().getSerializable("exercises");
            int[] schedule = getArguments().getIntArray("schedule");
            setRoutineTime(schedule[0], schedule[1], schedule[2]);
        }

        setRecyclerView();

        NextTxt.setBackgroundColor(Color.parseColor("#05c78c"));
        NextTxt.setTextColor(Color.parseColor("#ffffff"));

        NextBtn.setOnClickListener(v -> {
            mListener.onRoutineExDetail(adapter.getInputData());
        });

        return view;
    }

    private void setRoutineTime(int dayOfWeek, int startTime, int endTime) {

        String DayOfWeek = "";

        switch (dayOfWeek) {
            case 0: DayOfWeek = "일요일"; break;
            case 1: DayOfWeek = "월요일"; break;
            case 2: DayOfWeek = "화요일"; break;
            case 3: DayOfWeek = "수요일"; break;
            case 4: DayOfWeek = "목요일"; break;
            case 5: DayOfWeek = "금요일"; break;
            case 6: DayOfWeek = "토요일"; break;
        }

        String StartTime = TimeToString(startTime);
        String EndTime = TimeToString(endTime);
        String result = DayOfWeek + " · " + StartTime + " - " + EndTime;

        ScheduleTxt.setText(result);
    }

    private String TimeToString(int Time) {
        String am_pm = "";

        if (Time >= 240) Time-= 240;

        if (Time < 120) {
            am_pm = "오전";
            if (Time < 10) Time += 120;
        } else {
            am_pm = "오후";
            if (Time >= 130) Time-= 120;
        }

        @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d", Time/10, Time % 10 * 6);

        return am_pm + " " + result;
    }

    private void setRecyclerView() {
        adapter = new ExerciseInputAdapter(exercises);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}