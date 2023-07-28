package com.example.healthappttt.WorkOut;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.healthappttt.Data.Exercise.RoutineData;
import com.example.healthappttt.R;
import com.example.healthappttt.Routine.RoutineAdapter;
import com.example.healthappttt.databinding.FragmentErSelectRoutineBinding;

import java.util.ArrayList;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ERSelectRoutineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ERSelectRoutineFragment extends Fragment {
    FragmentErSelectRoutineBinding binding;

    private TextView Edit, DayOfWeekTxt;

    private RecyclerView recyclerView;
    private RoutineAdapter adapter;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DAY_OF_WEEK = "dayOfWeek";
    private static final String ARG_ROUTINE = "routine";

    // TODO: Rename and change types of parameters
    private RoutineData routine;
    private int dayOfWeek;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onSelectRoutine(boolean isBack);
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

    public ERSelectRoutineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param routine Parameter 1.
     * @return A new instance of fragment ERSlectRoutineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ERSelectRoutineFragment newInstance(int dayOfWeek, RoutineData routine) {
        ERSelectRoutineFragment fragment = new ERSelectRoutineFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_DAY_OF_WEEK, dayOfWeek);
        args.putSerializable(ARG_ROUTINE, routine);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dayOfWeek = getArguments().getInt(ARG_DAY_OF_WEEK);
            routine = (RoutineData) getArguments().getSerializable(ARG_ROUTINE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentErSelectRoutineBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switch (dayOfWeek) {
            case 0: binding.dayOfWeek.setText("오늘은 일요일"); break;
            case 1: binding.dayOfWeek.setText("오늘은 월요일"); break;
            case 2: binding.dayOfWeek.setText("오늘은 화요일"); break;
            case 3: binding.dayOfWeek.setText("오늘은 수요일"); break;
            case 4: binding.dayOfWeek.setText("오늘은 목요일"); break;
            case 5: binding.dayOfWeek.setText("오늘은 금요일"); break;
            case 6: binding.dayOfWeek.setText("오늘은 토요일"); break;
        }

        setRecyclerView();

        binding.backBtn.setOnClickListener(v -> mListener.onSelectRoutine(true));

        binding.nextBtn.setOnClickListener(v -> {
            if (routine != null) {
                mListener.onSelectRoutine(false);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    private void setRecyclerView() {
        ArrayList<RoutineData> routines = new ArrayList<>();
        if (routine != null) {
            routines.add(routine);
            binding.nextBtn.setBackground(getContext().getDrawable(R.drawable.rectangle_green_20dp));
            binding.nextBtn.setTextColor(Color.parseColor("#ffffff"));
        }

        adapter = new RoutineAdapter(getContext(), routines, 1);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }
}