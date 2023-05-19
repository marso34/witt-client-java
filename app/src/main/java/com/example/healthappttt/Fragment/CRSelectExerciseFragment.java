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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.ExerciseListAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CRSelectExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CRSelectExerciseFragment extends Fragment {
    private TextView DirectInputBtn, ScheduleTxt;
    private EditText searchView;
    private ImageView removeTxtBtn;

    private TabLayout tabLayout;
    private CardView NextBtn;
    private TextView NextTxt;

    private RecyclerView recyclerView;
    private ExerciseListAdapter adapter;

    private ArrayList<Exercise> chestExercises;
    private ArrayList<Exercise> shoulderExercises;
    private ArrayList<Exercise> backExercises;
    private ArrayList<Exercise> lowbodyExercises;
    private ArrayList<Exercise> armExercises;
    private ArrayList<Exercise> cardioExercises;

    private ArrayList<Exercise> exercises;
    private ArrayList<Exercise> selectExercises;

    private int[] tabPosition;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onRoutineAddEx(ArrayList<Exercise> selectExerciseNames);
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
        removeTxtBtn = view.findViewById(R.id.removeTxt);

        tabLayout = view.findViewById(R.id.tabLayout);
        recyclerView = view.findViewById(R.id.recyclerView);

        NextBtn = view.findViewById(R.id.nextBtn);
        NextTxt = view.findViewById(R.id.nextTxt);

        tabLayout.addTab(tabLayout.newTab().setText("가슴"));
        tabLayout.addTab(tabLayout.newTab().setText("어깨"));
        tabLayout.addTab(tabLayout.newTab().setText("등"));
        tabLayout.addTab(tabLayout.newTab().setText("하체"));
        tabLayout.addTab(tabLayout.newTab().setText("팔"));
        tabLayout.addTab(tabLayout.newTab().setText("복근"));
        tabLayout.addTab(tabLayout.newTab().setText("유산소"));

        tabPosition = new int[7];
        exercises = new ArrayList<>();
        selectExercises = new ArrayList<>();

        if (getArguments() != null) {
            selectExercises = (ArrayList<Exercise>) getArguments().getSerializable("exercises");
            // selectExercises를 이용해서 운동 리시트에 이미 체크한 운동을 처리
            int[] schedule = getArguments().getIntArray("schedule");
            setRoutineTime(schedule[0], schedule[1], schedule[2]);
        }


        chestExercises = new ArrayList<>();
        shoulderExercises = new ArrayList<>();
        backExercises = new ArrayList<>();
        lowbodyExercises = new ArrayList<>();
        armExercises = new ArrayList<>();
        cardioExercises = new ArrayList<>();


        chestExercises.add(new Exercise("가슴",0x1));
        chestExercises.add(new Exercise("벤치프레스",0x1));
        chestExercises.add(new Exercise("인클라인 벤치 프레스",0x1));
        chestExercises.add(new Exercise("케이블 크로스 오버",0x1));
        chestExercises.add(new Exercise("펙덱 플라인 머신",0x1));
        tabPosition[0] = 0;

        shoulderExercises.add(new Exercise("어깨",0x4));
        shoulderExercises.add(new Exercise("사이드 레터럴 레이즈",0x4));
        shoulderExercises.add(new Exercise("밀리터리 프레스",0x4));
        shoulderExercises.add(new Exercise("벤트 오버 레터럴 레이즈",0x4));
        tabPosition[1] = chestExercises.size();

        backExercises.add(new Exercise("등",0x2));
        backExercises.add(new Exercise("렛 풀 다운",0x2));
        backExercises.add(new Exercise("케이블 시티드 로우",0x2));
        backExercises.add(new Exercise("풀 업",0x2));
        backExercises.add(new Exercise("원 암 덤벨 로우",0x2));
        tabPosition[2] = tabPosition[1] + shoulderExercises.size();

        lowbodyExercises.add(new Exercise("하체",0x8));
        lowbodyExercises.add(new Exercise("레그 프레스",0x8));
        lowbodyExercises.add(new Exercise("루마니안 데드리프트",0x8));
        lowbodyExercises.add(new Exercise("바벨 스쿼트",0x8));
        lowbodyExercises.add(new Exercise("덤벨 스쿼트",0x8));
        tabPosition[3] = tabPosition[2] + lowbodyExercises.size();

        armExercises.add(new Exercise("팔",0x10));
        armExercises.add(new Exercise("바벨 컬",0x10));
        armExercises.add(new Exercise("덤벨 컬",0x10));
        armExercises.add(new Exercise("트레이셉스 프레스 다운 케이블",0x10));
        tabPosition[4] = tabPosition[3] + armExercises.size();

        ArrayList<Exercise> absExercises = new ArrayList<>();
        absExercises.add(new Exercise("복근",0x20));
        absExercises.add(new Exercise("싯 업",0x20));
        absExercises.add(new Exercise("크런치",0x20));
        tabPosition[5] = tabPosition[4] + absExercises.size();

        cardioExercises.add(new Exercise("유산소",0x40));
        cardioExercises.add(new Exercise("사이클",0x40));
        cardioExercises.add(new Exercise("트레드 밀",0x40));
        cardioExercises.add(new Exercise("인클라인 트레드 밀",0x40));
        tabPosition[6] = tabPosition[5] + cardioExercises.size();

        exercises.addAll(chestExercises);
        exercises.addAll(shoulderExercises);
        exercises.addAll(backExercises);
        exercises.addAll(lowbodyExercises);
        exercises.addAll(armExercises);
        exercises.addAll(absExercises);
        exercises.addAll(cardioExercises);

        if (exercises != null)
            setRecyclerView();

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = searchView.getText().toString();

                if (str != null) {
                    removeTxtBtn.setVisibility(View.VISIBLE);
                } else {
                    removeTxtBtn.setVisibility(View.INVISIBLE);
                }
            }
        });

        removeTxtBtn.setOnClickListener(v -> {
            searchView.setText(null);
            removeTxtBtn.setVisibility(View.INVISIBLE);
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tabPosition[tab.getPosition()];

                recyclerView.smoothScrollToPosition(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        NextBtn.setOnClickListener(v -> {
            if (selectExercises.size() > 0)
                mListener.onRoutineAddEx(selectExercises);
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
        adapter = new ExerciseListAdapter(exercises);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        if (adapter != null) {
            adapter.setOnSelectExerciseListener(new ExerciseListAdapter.OnSelectExercise() {
                @Override
                public void onSelectExercise(Exercise exercise, boolean add) {
                    if (add)    selectExercises.add(exercise);
                    else        selectExercises.remove(exercise);

                    if(selectExercises.size() > 0) {
                        NextTxt.setBackgroundColor(Color.parseColor("#05c78c"));
                        NextTxt.setTextColor(Color.parseColor("#ffffff"));
                    } else {
                        NextTxt.setBackgroundColor(Color.parseColor("#D1D8E2"));
                        NextTxt.setTextColor(Color.parseColor("#9AA5B8"));
                    }
                }
            });
        }
    }
}