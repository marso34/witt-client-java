package com.gwnu.witt.Routine;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gwnu.witt.Data.Exercise.ExerciseData;
import com.gwnu.witt.Data.Exercise.RoutineData;
import com.gwnu.witt.R;
import com.gwnu.witt.databinding.FragmentCrSelectExerciseBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CRSelectExerciseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CRSelectExerciseFragment extends Fragment {
    FragmentCrSelectExerciseBinding binding;

    private static final String Background_1 = "#F2F5F9";
    private static final String Background_2 = "#D1D8E2";
    private static final String Background_3 = "#9AA5B8";
    private static final String Signature = "#05C78C";
    private static final String Orange = "#FC673F";
    private static final String Yellow = "#F2BB57";
    private static final String Blue = "#579EF2";
    private static final String Purple = "#8C5AD8";
    private static final String White = "#ffffff";

    private ExerciseListPAdapter adapter;

    private ArrayList<ExerciseData> exercises;
    private ArrayList<ExerciseData> searchList;
    public ArrayList<String> selectExerciseIndex;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ROUTINE = "routine";

    // TODO: Rename and change types of parameters
    private int time;
    private int dayOfWeek;
    private RoutineData routine;

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
     * @param routine Parameter 1.
     * @return A new instance of fragment addExerciseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CRSelectExerciseFragment newInstance(RoutineData routine) {
        CRSelectExerciseFragment fragment = new CRSelectExerciseFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ROUTINE, routine);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            routine = (RoutineData) getArguments().getSerializable(ARG_ROUTINE);
            time = routine.getTime();
            dayOfWeek = routine.getDayOfWeek();

            exercises = new ArrayList<>();
            selectExerciseIndex = new ArrayList<>();

            if (routine.getExercises() != null) {
                for (ExerciseData e : routine.getExercises())
                    selectExerciseIndex.add((e.getStrCat() + " " + e.getExerciseName()));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() { mListener.onRoutineAddEx(null); }
        });

        binding = FragmentCrSelectExerciseBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        parseExercise();

        switch (time) {
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

        binding.backBtn.setOnClickListener(v -> mListener.onRoutineAddEx(null));

        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        binding.nextBtn.setOnClickListener(v -> {
            Log.d("클릭", "추가하기 클릭");

            if (selectExerciseIndex.size() > 0) {
                Log.d("성공1", selectExerciseIndex.size() + "");
                ArrayList<ExerciseData> selectExercises = new ArrayList<>();

                int i = 0;
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

                    selectExercises.add(new ExerciseData(name, cat, i));
                    i++;
                }

                mListener.onRoutineAddEx(selectExercises);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    private void parseExercise() { // 나중에 xml 파싱으로
        exercises.add(new ExerciseData("벤치프레스",0x1));
        exercises.add(new ExerciseData("인클라인 벤치 프레스",0x1));
        exercises.add(new ExerciseData("디클라인 벤치 프레스",0x1));
        exercises.add(new ExerciseData("케이블 크로스 오버",0x1));
        exercises.add(new ExerciseData("덤벨 플라이",0x1));
        exercises.add(new ExerciseData("바벨 플라이",0x1));
        exercises.add(new ExerciseData("펙덱 플라이 머신",0x1));
        exercises.add(new ExerciseData("체스트 프레스 머신",0x1));
        exercises.add(new ExerciseData("팔굽혀펴기",0x1));
        exercises.add(new ExerciseData("딥스",0x1));

        exercises.add(new ExerciseData("렛 풀 다운",0x2));
        exercises.add(new ExerciseData("케이블 시티드 로우",0x2));
        exercises.add(new ExerciseData("풀 업",0x2));
        exercises.add(new ExerciseData("덤벨 벤트 오버 로우",0x2));
        exercises.add(new ExerciseData("원 암 덤벨 로우",0x2));
        exercises.add(new ExerciseData("바벨 로우",0x2));

        exercises.add(new ExerciseData("사이드 레터럴 레이즈",0x4));
        exercises.add(new ExerciseData("벤트 오버 레터럴 레이즈",0x4));
        exercises.add(new ExerciseData("프론트 레이즈",0x4));
        exercises.add(new ExerciseData("스미스 머신 숄더 프레스",0x4));
        exercises.add(new ExerciseData("숄더 프레스",0x4));
        exercises.add(new ExerciseData("밀리터리 프레스",0x4));
        exercises.add(new ExerciseData("푸쉬프레스",0x4));
        exercises.add(new ExerciseData("업라이트 로우",0x4));
        exercises.add(new ExerciseData("덤벨 슈러그",0x4));
        exercises.add(new ExerciseData("바벨 슈러그",0x4));
        exercises.add(new ExerciseData("트랩바 데드리프트",0x4));
        exercises.add(new ExerciseData("덤벨 데드리프트",0x4));

        exercises.add(new ExerciseData("레그 프레스",0x8));
        exercises.add(new ExerciseData("레그 익스텐션",0x8));
        exercises.add(new ExerciseData("레그 컬",0x8));
        exercises.add(new ExerciseData("런지",0x8));
        exercises.add(new ExerciseData("스티프 데드리프트",0x8));
        exercises.add(new ExerciseData("루마니안 데드리프트",0x8));
        exercises.add(new ExerciseData("브릿지",0x8));
        exercises.add(new ExerciseData("바벨 스쿼트",0x8));
        exercises.add(new ExerciseData("덤벨 스쿼트",0x8));
        exercises.add(new ExerciseData("카프 레이즈",0x8));

        exercises.add(new ExerciseData("바벨 컬",0x10));
        exercises.add(new ExerciseData("덤벨 컬",0x10));
        exercises.add(new ExerciseData("로프 케이블 컬",0x10));
        exercises.add(new ExerciseData("해머 컬",0x10));
        exercises.add(new ExerciseData("트라이셉스 프레스 다운 케이블",0x10));
        exercises.add(new ExerciseData("라잉 트라이셉스 익스텐션",0x10));
        exercises.add(new ExerciseData("스컬 크러셔",0x10));
        exercises.add(new ExerciseData("트라이셉스 푸쉬 다운",0x10));
        exercises.add(new ExerciseData("킥 백",0x10));

        exercises.add(new ExerciseData("싯 업",0x20));
        exercises.add(new ExerciseData("크런치",0x20));
        exercises.add(new ExerciseData("레그 레이즈",0x20));

        exercises.add(new ExerciseData("사이클",0x40));
        exercises.add(new ExerciseData("트레드 밀",0x40));
        exercises.add(new ExerciseData("인클라인 트레드 밀",0x40));
        exercises.add(new ExerciseData("스텝 밀",0x40));

        searchList = (ArrayList<ExerciseData>) exercises.clone();
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
        Log.d("리사이클러 뷰 할당", selectExerciseIndex.size() + "");
        adapter = new ExerciseListPAdapter(getContext(), searchList, selectExerciseIndex);

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }
}