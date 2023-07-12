package com.example.healthappttt.Routine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.healthappttt.Data.Exercise.ExerciseData;
import com.example.healthappttt.Data.Exercise.RoutineData;
import com.example.healthappttt.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CRSelectExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CRSelectExerciseFragment extends Fragment {
    private TextView DirectInputBtn, ScheduleTxt, NextTxt;
    private TabLayout tabLayout;
    private CardView NextBtn;

    private SearchView searchView;

    private RecyclerView recyclerView;
    private ExerciseListPAdapter adapter;

    private ArrayList<ExerciseData> exercises;
    private ArrayList<ExerciseData> searchList;
    public ArrayList<String> selectExerciseIndex;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onRoutineAddEx(ArrayList<ExerciseData> selectExercises);
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

    public CRSelectExerciseFragment() {
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
    public static CRSelectExerciseFragment newInstance(String param1, String param2) {
        CRSelectExerciseFragment fragment = new CRSelectExerciseFragment();
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
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_cr_select_exercise, container, false);

        DirectInputBtn = view.findViewById(R.id.directInput);
        ScheduleTxt = view.findViewById(R.id.schedule);

        searchView = view.findViewById(R.id.search);

        tabLayout = view.findViewById(R.id.tabLayout);
        recyclerView = view.findViewById(R.id.recyclerView);

        NextBtn = view.findViewById(R.id.nextBtn);
        NextTxt = view.findViewById(R.id.nextTxt);

        exercises = new ArrayList<>();
        selectExerciseIndex = new ArrayList<>();

        if (getArguments() != null) {
//            selectExercises = (ArrayList<ExerciseData>) getArguments().getSerializable("exercises");
            // selectExercises를 이용해서 운동 리시트에 이미 체크한 운동을 처리
            int[] schedule = getArguments().getIntArray("schedule");
            RoutineData routine = (RoutineData) getArguments().getSerializable("routine");
            selectExerciseIndex = new ArrayList<>();

            for (ExerciseData e : routine.getExercises())
                selectExerciseIndex.add((e.getCat() + " " + e.getExerciseName()));

            setRoutineTime(routine.getDayOfWeek(), routine.getStartTime(), routine.getEndTime());
        }

        parseExercise();

        if (exercises != null)
            setRecyclerView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchExercise(newText);
                return false;
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                recyclerView.smoothScrollToPosition(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        NextBtn.setOnClickListener(v -> {
            if (selectExerciseIndex.size() > 0) {

                ArrayList<ExerciseData> selectExercises = new ArrayList<>();

                for (String str : selectExerciseIndex) {
                    String strCat = str.substring(0, str.indexOf(" "));
                    String name = str.substring(str.indexOf(" ")+1);

                    int cat = 0;

                    switch (strCat) {
                        case "가슴"   : cat = 0x1;  break;
                        case "등"     : cat = 0x2;  break;
                        case "어깨"   : cat = 0x4;  break;
                        case "하체"   : cat = 0x8;  break;
                        case "팔"     : cat = 0x10; break;
                        case "복근"   : cat = 0x20; break;
                        case "유산소" : cat = 0x40; break;
                    }

                    selectExercises.add(new ExerciseData(name, cat));
                }

                mListener.onRoutineAddEx(selectExercises);
            }
        });

        return view;
    }

    private void parseExercise() { // 나중에 xml 파싱으로
        exercises.add(new ExerciseData("벤치프레스",0x1));
        exercises.add(new ExerciseData("인클라인 벤치 프레스",0x1));
        exercises.add(new ExerciseData("케이블 크로스 오버",0x1));
        exercises.add(new ExerciseData("펙덱 플라인 머신",0x1));

        exercises.add(new ExerciseData("사이드 레터럴 레이즈",0x4));
        exercises.add(new ExerciseData("밀리터리 프레스",0x4));
        exercises.add(new ExerciseData("벤트 오버 레터럴 레이즈",0x4));

        exercises.add(new ExerciseData("렛 풀 다운",0x2));
        exercises.add(new ExerciseData("케이블 시티드 로우",0x2));
        exercises.add(new ExerciseData("풀 업",0x2));
        exercises.add(new ExerciseData("원 암 덤벨 로우",0x2));

        exercises.add(new ExerciseData("레그 프레스",0x8));
        exercises.add(new ExerciseData("루마니안 데드리프트",0x8));
        exercises.add(new ExerciseData("바벨 스쿼트",0x8));
        exercises.add(new ExerciseData("덤벨 스쿼트",0x8));

        exercises.add(new ExerciseData("바벨 컬",0x10));
        exercises.add(new ExerciseData("덤벨 컬",0x10));
        exercises.add(new ExerciseData("트레이셉스 프레스 다운 케이블",0x10));

        exercises.add(new ExerciseData("싯 업",0x20));
        exercises.add(new ExerciseData("크런치",0x20));

        exercises.add(new ExerciseData("사이클",0x40));
        exercises.add(new ExerciseData("트레드 밀",0x40));
        exercises.add(new ExerciseData("인클라인 트레드 밀",0x40));

        searchList = (ArrayList<ExerciseData>) exercises.clone();
    }

    private String TimeParse(String time) {
        String hour = time.substring(0, time.indexOf(":"));
        String minSec = time.substring(time.indexOf(":")+1);
        String min = minSec.substring(0, minSec.indexOf(":"));
        String am_pm = "오전";

        int tempHour = Integer.parseInt(hour);

        if (tempHour > 12) {
            am_pm = "오후";
            tempHour -= 12;
        }

        @SuppressLint("DefaultLocale") String result = String.format("%02d:%s", tempHour, min);

        return am_pm + " " + result;
    }

    private void searchExercise(String searchTxt) {
        searchList.clear();

        for (ExerciseData e : exercises) {
            if (e.getExerciseName().contains(searchTxt) || e.getStrCat().equals(searchTxt))
                searchList.add(e);
        }

        adapter.notifyDataSetChanged();
    }

    private void setRecyclerView() {
        adapter = new ExerciseListPAdapter(getContext(), searchList, selectExerciseIndex);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setRoutineTime(int dayOfWeek, String startTime, String endTime) {
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

        String StartTime = TimeParse(startTime);
        String EndTime = TimeParse(endTime);
        String result = DayOfWeek + " · " + StartTime + " - " + EndTime;

        ScheduleTxt.setText(result);
    }
}