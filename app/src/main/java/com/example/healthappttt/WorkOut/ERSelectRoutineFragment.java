package com.example.healthappttt.WorkOut;

import android.content.Context;
import android.os.Bundle;

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

import java.util.ArrayList;

import io.reactivex.rxjava3.annotations.NonNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ERSelectRoutineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ERSelectRoutineFragment extends Fragment {
    private TextView Edit, DayOfWeekTxt;

    private RecyclerView recyclerView;
    private RoutineAdapter adapter;

    private ArrayList<RoutineData> routines;
    private int dayOfWeek;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onSelectRoutine(RoutineData routine);
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
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ERSlectRoutineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ERSelectRoutineFragment newInstance(String param1, String param2) {
        ERSelectRoutineFragment fragment = new ERSelectRoutineFragment();
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
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_er_select_routine, container, false);

//        Edit = view.findViewById(R.id.edit);
        DayOfWeekTxt = view.findViewById(R.id.dayOfWeek);
        recyclerView = view.findViewById(R.id.recyclerView);

        if (getArguments() != null) {
            routines = (ArrayList<RoutineData>) getArguments().getSerializable("routines");
            dayOfWeek = getArguments().getInt("dayOfWeek");
        }

        switch (dayOfWeek) {
            case 0: DayOfWeekTxt.setText("오늘은 일요일"); break;
            case 1: DayOfWeekTxt.setText("오늘은 월요일"); break;
            case 2: DayOfWeekTxt.setText("오늘은 화요일"); break;
            case 3: DayOfWeekTxt.setText("오늘은 수요일"); break;
            case 4: DayOfWeekTxt.setText("오늘은 목요일"); break;
            case 5: DayOfWeekTxt.setText("오늘은 금요일"); break;
            case 6: DayOfWeekTxt.setText("오늘은 토요일"); break;
        }

        setRecyclerView();

        return view;
    }

    private void setRecyclerView() {
        adapter = new RoutineAdapter(getContext(), routines, 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        if (adapter != null) {
            adapter.setOnClickRoutineListener(new RoutineAdapter.OnClickRoutine() {
                @Override
                public void onClickRoutine(RoutineData r) {
                    mListener.onSelectRoutine(r);
                }
            });
        }
    }
}