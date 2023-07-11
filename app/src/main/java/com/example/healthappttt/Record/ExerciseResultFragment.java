package com.example.healthappttt.Record;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthappttt.Data.Exercise.RecordData;
import com.example.healthappttt.databinding.FragmentExerciseResultBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExerciseResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseResultFragment extends Fragment {
    FragmentExerciseResultBinding binding;

    private RecordData recordData;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ExerciseResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExerciseResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExerciseResultFragment newInstance(String param1, String param2) {
        ExerciseResultFragment fragment = new ExerciseResultFragment();
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
        binding = FragmentExerciseResultBinding.inflate(inflater);

        if (getArguments() != null) {
            recordData = (RecordData) getArguments().getSerializable("record");
            // 프래그먼트 시작할 때 시작시간, 종료시간을 넘겨 받을 떄 동장 정의
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String ExerciseTime = betweenTime(recordData.getStartTime(), recordData.getEndTime()); // 운동 시간, 휴식 시간 포함
        String RunTime = recordData.getRunTime();                                              // RunTime은 순수하게 운동한 시간, 휴식시간 제외 시간
        String RestTime = betweenTime(ExerciseTime, RunTime);                                  // RunTime은 휴식시간. 운동 시간 - RuntTime

        binding.runTime.setText(parse(RunTime));
        binding.restTime.setText(parse(RestTime));

        binding.nextBtn.setOnClickListener(v -> {
            getActivity().finish();
        });
    }

    @Override
    public void onDestroyView() {  //프레그먼트가 보여주는 뷰들이 Destroy(파괴)되어도 프레그먼트 객체가 살아있을 수 있기에 뷰들이 메모리에서 없어질때 바인딩클래스의 인스턴스도 파괴함으로서 메모리누수를 방지할 수 있음.
        super.onDestroyView();

        binding = null; //바인딩 객체를 GC(Garbage Collector) 가 없애도록 하기 위해 참조를 끊기
    }

    private String parse(String time) { // 함수명 나중에 바꾸기
        String hour = time.substring(0, time.indexOf(":"));
        String minSec = time.substring(time.indexOf(":")+1);
        String min = minSec.substring(0, minSec.indexOf(":"));
        String sec = minSec.substring(minSec.indexOf(":")+1);

        String result = "";
        int h = Integer.parseInt(hour), m = Integer.parseInt(min), s = Integer.parseInt(sec);

        if (h > 0)     result += h + "시간 ";
        if (m > 0)     result += m + "분 ";
        if (s > 0)     result += s + "초";
        if (result == null) result = "0초";

        return result;
    }

    private String betweenTime(String time1, String time2) {
        int intTime1 = TimeToInt(time1), intTime2 = TimeToInt(time2);

        int T = intTime1 - intTime2;

        if (T < 0) T *= -1;

        @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d", T/360, (T/60) % 60, T % 60);
        return result;
    }

    private int TimeToInt(String time) { // 시작 시간 및 종료 시간을 전역으로 가지고 종료시간이 시작 시간보다 빠를 수 없도록 수정할 것
        String hour = time.substring(0, time.indexOf(":"));
        String minSec = time.substring(time.indexOf(":")+1);
        String min = minSec.substring(0, minSec.indexOf(":"));
        String sec = minSec.substring(minSec.indexOf(":")+1);

        int T = Integer.parseInt(hour) * 360 + Integer.parseInt(min) * 60 + Integer.parseInt(sec);

        return T;
    }
}