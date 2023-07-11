package com.example.healthappttt.Routine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.healthappttt.databinding.FragmentCrSetTimeBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CRSetTimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CRSetTimeFragment extends Fragment {
    FragmentCrSetTimeBinding binding;

    private int[] schedule;
    private int runTime, startTime, endTime;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onRoutineSetTime(int startTime, int endTime);
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

    public CRSetTimeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SetRoutineTimeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CRSetTimeFragment newInstance(String param1, String param2) {
        CRSetTimeFragment fragment = new CRSetTimeFragment();
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
//        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_cr_set_time, container, false);
        binding = FragmentCrSetTimeBinding.inflate(inflater);

        if (getArguments() != null) {
            schedule = getArguments().getIntArray("schedule");
            // 프래그먼트 시작할 때 시작시간, 종료시간을 넘겨 받을 떄 동장 정의
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

        binding.startTimeUP.setOnClickListener(v -> {
            if (startTime < endTime) {
                startTime += 5;
                runTime = endTime - startTime;

                binding.startTime.setText(TimeToString(startTime));
                binding.runTime.setText(RuntimeToString(runTime));
                setNextBtn();
            }
        });

        binding.startTimeDown.setOnClickListener(v -> {
            if (startTime > 0) {
                startTime -= 5;
                runTime = endTime - startTime;

                binding.startTime.setText(TimeToString(startTime));
                binding.runTime.setText(RuntimeToString(runTime));
                setNextBtn();
            }
        });

        binding.endTimeUP.setOnClickListener(v -> {
            if (endTime < 240) {
                endTime += 5;
                runTime = endTime - startTime;

                binding.endTime.setText(TimeToString(endTime));
                binding.runTime.setText(RuntimeToString(runTime));
                setNextBtn();
            }
        });

        binding.endTimeDown.setOnClickListener(v -> {
            if (endTime > startTime) {
                endTime -= 5;
                runTime = endTime - startTime;

                binding.endTime.setText(TimeToString(endTime));
                binding.runTime.setText(RuntimeToString(runTime));
                setNextBtn();
            }
        });

        binding.nextBtn.setOnClickListener(v -> {
            if (runTime > 0) {
                mListener.onRoutineSetTime(startTime, endTime);
            }
        });
    }

    @Override
    public void onDestroyView() {  //프레그먼트가 보여주는 뷰들이 Destroy(파괴)되어도 프레그먼트 객체가 살아있을 수 있기에 뷰들이 메모리에서 없어질때 바인딩클래스의 인스턴스도 파괴함으로서 메모리누수를 방지할 수 있음.
        super.onDestroyView();

        binding = null; //바인딩 객체를 GC(Garbage Collector) 가 없애도록 하기 위해 참조를 끊기
    }

    private void init() {
        startTime = TimeToInt((String) binding.startTime.getText());
        endTime = TimeToInt((String) binding.endTime.getText());

        Log.d("운동 시간", startTime + " " + endTime);
        runTime = endTime - startTime;
    }

    private void setNextBtn() {
        if (runTime > 0) {
//            NextBtn = (CardView) view.findViewById(R.id.nextBtn);
            binding.nextTxt.setBackgroundColor(Color.parseColor("#05c78c"));
            binding.nextTxt.setTextColor(Color.parseColor("#ffffff"));
        } else {
            binding.nextTxt.setBackgroundColor(Color.parseColor("#D1D8E2"));
            binding.nextTxt.setTextColor(Color.parseColor("#9AA5B8"));
        }
    }

    private int TimeToInt(String Time) { // 시작 시간 및 종료 시간을 전역으로 가지고 종료시간이 시작 시간보다 빠를 수 없도록 수정할 것
        String am_pm = Time.substring(0, Time.indexOf(" "));
        String temp = Time.substring(Time.indexOf(" ")+1);
        String hour = temp.substring(0, temp.indexOf(":"));
        String min = temp.substring(temp.indexOf(":")+1);

        int T = Integer.parseInt(hour) * 10 + Integer.parseInt(min) / 6;

        if (am_pm.equals("오후")) T += 120;
        if (T == 120 || T == 125 || T == 240 || T == 245) T-= 120;

        return T;
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

    private String RuntimeToString(int Time) {
        @SuppressLint("DefaultLocale") String result1 = String.format("%d분", Time % 10 * 6);
        @SuppressLint("DefaultLocale") String result2 = String.format("%d시간", Time/10);
        @SuppressLint("DefaultLocale") String result3 = String.format("%d시간 %d분", Time/10, Time % 10 * 6);

        if (Time < 10)
            return result1;

        if (Time % 10 == 0)
            return result2;

        return result3;
    }
}