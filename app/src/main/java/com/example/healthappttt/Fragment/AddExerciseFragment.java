package com.example.healthappttt.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.ExerciseAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddExerciseFragment extends Fragment {
    private TextView DirectInputBtn, ScheduleTxt;
    private SearchView searchView;
    private TabLayout tabLayout;
    private CardView NextBtn;
    private TextView NextTxt;
    private RecyclerView recyclerView;
//    private



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onRoutineAddEx(ArrayList<Exercise> exercises);
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

    public AddExerciseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addExerciseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddExerciseFragment newInstance(String param1, String param2) {
        AddExerciseFragment fragment = new AddExerciseFragment();
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
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_add_exercise, container, false);

        DirectInputBtn = view.findViewById(R.id.directInput);
        ScheduleTxt = view.findViewById(R.id.schedule);

        searchView = view.findViewById(R.id.search);
        tabLayout = view.findViewById(R.id.tab_layout);
        recyclerView = view.findViewById(R.id.recyclerView);

        NextBtn = view.findViewById(R.id.nextBtn);
        NextTxt = view.findViewById(R.id.nextTxt);

        if (getArguments() != null) {
            int[] schedule = getArguments().getIntArray("schedule");
            setRoutineTime(schedule[0], schedule[1], schedule[2]);
        }

        NextBtn.setOnClickListener(v -> {
//            mListener.onRoutineAddEx();
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
//        adapter = new Adapter();
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);

//        if (adapter != null) {
//            adapter.setOnExerciseClickListener(new ExerciseAdapter.OnExerciseClick() { // 어댑터 데이터를 전송 받기 위한 인터페이스 콜백
//                @Override
//                public void onExerciseClick(int position, TextView CountView, TextView CardioTxtView, ProgressBar CardioBar) { // 운동 기록과 운동 메모를 전달 받아
//                    if (isRunning) {
//                        Exercise e = routine.getExercises().get(position);
//                        String cat = e.getState();
//                        int count = e.getCount();
//                        int SetCnt = recordExercises.get(position).getCount();
//
//                        if (cat.equals("유산소")) {
//                            if (AdapterCardioBar == null) {
//                                AdapterCardioBar = CardioBar;
//                                AdapterCardioBar.setProgressDrawable(getDrawable(R.drawable.progressbar_exercise1));
//                                AdapterCardioTxtView = CardioTxtView;
//                            } else if (AdapterCardioBar == CardioBar) {
//                                AdapterCardioBar.setProgressDrawable(getDrawable(R.drawable.progressbar_exercise2));
//                                AdapterCardioBar = null;
//                                AdapterCardioTxtView = null;
//                            }
//                        } else {
//                            if (SetCnt < count)
//                                SetCnt++;
//
//                            recordExercises.get(position).setCount(SetCnt);
//                            CountView.setText(Integer.toString(SetCnt) + "/" + Integer.toString(count));
//                        }
//                    }
//                }
//            });
//        }
    } // 리사이클러 뷰 생성 -> 추후 수정 필요
}