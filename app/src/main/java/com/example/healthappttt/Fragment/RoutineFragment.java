package com.example.healthappttt.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.Data.ExerciseName;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.R;
import com.example.healthappttt.adapter.RoutineAdapter;
import com.example.healthappttt.adapter.setExerciseAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;
import java.util.Date;

public class RoutineFragment extends Fragment {
    Context context;

    private Button[] weekBtn;
    private ToggleButton[] exerciseTxt;

    private RecyclerView recyclerView;
    private setExerciseAdapter adapter;
    private CardView addCard;

    private Routine routine;

    private int dayOfWeek;
    private String testStr;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public RoutineFragment() {
    }

    public static RoutineFragment newInstance(String param1, String param2) {
        RoutineFragment fragment = new RoutineFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_routine, container, false);

        weekBtn = new Button[7];
        exerciseTxt = new ToggleButton[7];

        weekBtn[0] = (Button) view.findViewById(R.id.sun);
        weekBtn[1] = (Button) view.findViewById(R.id.mon);
        weekBtn[2] = (Button) view.findViewById(R.id.tue);
        weekBtn[3] = (Button) view.findViewById(R.id.wed);
        weekBtn[4] = (Button) view.findViewById(R.id.thu);
        weekBtn[5] = (Button) view.findViewById(R.id.fri);
        weekBtn[6] = (Button) view.findViewById(R.id.sat);

        exerciseTxt[0] = (ToggleButton) view.findViewById(R.id.chestTxt);
        exerciseTxt[1] = (ToggleButton) view.findViewById(R.id.backTxt);
        exerciseTxt[2] = (ToggleButton) view.findViewById(R.id.shoulderTxt);
        exerciseTxt[3] = (ToggleButton) view.findViewById(R.id.lowBodyTxt);
        exerciseTxt[4] = (ToggleButton) view.findViewById(R.id.armTxt);
        exerciseTxt[5] = (ToggleButton) view.findViewById(R.id.absTxt);
        exerciseTxt[6] = (ToggleButton) view.findViewById(R.id.cardioTxt);


        addCard = (CardView) view.findViewById(R.id.plusExercise);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        getCurrentWeek();
        setRoutine();

        final ExerciseListFragment exerciseListFragment = new ExerciseListFragment(getContext()); // 운동 부위 정보도 추가

        exerciseListFragment.setOnExerciseClickListener(new ExerciseListFragment.OnExerciseClick() {
            @Override
            public void onExerciseClick(ExerciseName exerciseName) {
                routine.addExercise(new Exercise(exerciseName.getName(), exerciseName.getCat()));
                setRecyclerView();
            }
        });

        for (Button btn : weekBtn) {
            btn.setOnClickListener(v -> clickBtn(v));
        }

        for (ToggleButton txt : exerciseTxt) {
            txt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        compoundButton.setBackgroundResource(R.drawable.green_border_layout);
                        compoundButton.setTextColor(Color.parseColor("#05c78c"));
                    } else {
                        compoundButton.setBackgroundResource(R.drawable.border_layout);
                        compoundButton.setTextColor(Color.parseColor("#747474"));
                    }
                }
            });
        }

        addCard.setOnClickListener(v -> {
            exerciseListFragment.show(getActivity().getSupportFragmentManager(), exerciseListFragment.getTag());
        });


        return view;
    }

    public void getCurrentWeek() {
        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

        dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        switch (dayOfWeek) {
            case 0: weekBtn[0].setBackgroundResource(R.drawable.round_button_green); weekBtn[0].setTextColor(Color.parseColor("#ffffff") ); break; // 일
            case 1: weekBtn[1].setBackgroundResource(R.drawable.round_button_green); weekBtn[1].setTextColor(Color.parseColor("#ffffff") ); break; // 월
            case 2: weekBtn[2].setBackgroundResource(R.drawable.round_button_green); weekBtn[2].setTextColor(Color.parseColor("#ffffff") ); break; // 화
            case 3: weekBtn[3].setBackgroundResource(R.drawable.round_button_green); weekBtn[3].setTextColor(Color.parseColor("#ffffff") ); break; // 수
            case 4: weekBtn[4].setBackgroundResource(R.drawable.round_button_green); weekBtn[4].setTextColor(Color.parseColor("#ffffff") ); break; // 목
            case 5: weekBtn[5].setBackgroundResource(R.drawable.round_button_green); weekBtn[5].setTextColor(Color.parseColor("#ffffff") ); break; // 금
            case 6: weekBtn[6].setBackgroundResource(R.drawable.round_button_green); weekBtn[6].setTextColor(Color.parseColor("#ffffff") ); break; // 토
        }
    }

    private void setRoutine() {

        // DB 접근해서 요일에 맞는 루틴 가져와서 루틴 생성
        // dayOfWeek 요일 정보

        routine = new Routine("월", "전신");
        setRecyclerView();
    }

    private void setRecyclerView() {
        adapter = new setExerciseAdapter(routine); // 나중에 routine
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }


    public void clickBtn(View v) {
        for (Button btn : weekBtn) {
            btn.setBackgroundResource(R.color.transparent);
            btn.setTextColor(Color.parseColor("#000000"));
        }

        Button button = (Button) v;

        button.setBackgroundResource(R.drawable.round_button_green);
        button.setTextColor(Color.parseColor("#ffffff") );
        dayOfWeek = Integer.parseInt((String) v.getTag()); // v.getTag가 (int)로 형변환 안 됨

        setRoutine();
    }
}