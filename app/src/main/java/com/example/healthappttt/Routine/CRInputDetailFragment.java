package com.example.healthappttt.Routine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthappttt.Data.Exercise.ExerciseData;
import com.example.healthappttt.Data.Exercise.RoutineData;
import com.example.healthappttt.R;
import com.example.healthappttt.databinding.FragmentCrInputDetailBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CRInputDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CRInputDetailFragment extends Fragment {
    FragmentCrInputDetailBinding binding;

    private static final String Background_1 = "#F2F5F9";
    private static final String Background_2 = "#D1D8E2";
    private static final String Background_3 = "#9AA5B8";
    private static final String Signature = "#05C78C";
    private static final String Orange = "#FC673F";
    private static final String Yellow = "#F2BB57";
    private static final String Blue = "#579EF2";
    private static final String Purple = "#8C5AD8";
    private static final String White = "#ffffff";

    private ExerciseInputAdapter adapter;
    private ArrayList<ExerciseData> exercises;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ROUTINE = "routine";

    // TODO: Rename and change types of parameters
    int time, dayOfWeek;
    private RoutineData routine;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onRoutineExDetail(ArrayList<ExerciseData> exercises, boolean isFinish);
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

    public CRInputDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param routine Parameter 1.
     * @return A new instance of fragment ExerciseDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CRInputDetailFragment newInstance(RoutineData routine) {
        CRInputDetailFragment fragment = new CRInputDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ROUTINE, routine);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            routine = (RoutineData) getArguments().getSerializable(ARG_ROUTINE);
            time = routine.getTime();
            exercises = routine.getExercises();
            dayOfWeek = routine.getDayOfWeek();

            for (ExerciseData e : exercises)
                Log.d("테스트", e.getExerciseName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCrInputDetailBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switch (time) { // routine.getTime();
            case 0:
                binding.timeIcon.setImageResource(R.drawable.baseline_brightness_5_24);
                binding.timeTxt.setText("아침");
                binding.timeTxt.setTextColor(Color.parseColor(Orange));
                binding.timeDetail.setText("오전 6시 ~ 오후 12시");
                break;
            case 1:
                binding.timeIcon.setImageResource(R.drawable.baseline_wb_sunny_24);
                binding.timeTxt.setText("점심");
                binding.timeTxt.setTextColor(Color.parseColor(Yellow));
                binding.timeDetail.setText("오후 12시 ~ 오후 6시");
                break;
            case 2:
                binding.timeIcon.setImageResource(R.drawable.baseline_brightness_3_24);
                binding.timeTxt.setText("저녁");
                binding.timeTxt.setTextColor(Color.parseColor(Blue));
                binding.timeDetail.setText("오후 6시 ~ 오전 12시");
                break;
            case 3:
                binding.timeIcon.setImageResource(R.drawable.baseline_flare_24);
                binding.timeTxt.setText("새벽");
                binding.timeTxt.setTextColor(Color.parseColor(Purple));
                binding.timeDetail.setText("오전 12시 ~ 오전 6시");
                break;
        }

        switch (dayOfWeek) {
            case 0: binding.dayOfWeek.setText("일요일"); break;
            case 1: binding.dayOfWeek.setText("월요일"); break;
            case 2: binding.dayOfWeek.setText("화요일"); break;
            case 3: binding.dayOfWeek.setText("수요일"); break;
            case 4: binding.dayOfWeek.setText("목요일"); break;
            case 5: binding.dayOfWeek.setText("금요일"); break;
            case 6: binding.dayOfWeek.setText("토요일"); break;
        }

        if (exercises != null)
            setRecyclerView();

        binding.backBtn.setOnClickListener(v -> mListener.onRoutineExDetail(exercises, false));

        binding.nextBtn.setOnClickListener(v -> {
            if (exercises.size() > 0)
                mListener.onRoutineExDetail(exercises, true);
            else
                Toast.makeText(getContext(), "운동을 추가해주세요", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    private void setRecyclerView() {
        adapter = new ExerciseInputAdapter(getContext(), exercises);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }
}