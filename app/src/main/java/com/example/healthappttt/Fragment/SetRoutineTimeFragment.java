package com.example.healthappttt.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.healthappttt.R;

import org.w3c.dom.Text;

import java.sql.Time;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetRoutineTimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetRoutineTimeFragment extends Fragment {
    private TextView RunTime, StartTime, EndTime, ResetBtn;
    private ImageView StartTimeUP, StartTimeDown;
    private ImageView EndTimeUP, EndTimeDown;

    private CardView NextBtn;
    private TextView NextTxt;

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

    public SetRoutineTimeFragment() {
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
    public static SetRoutineTimeFragment newInstance(String param1, String param2) {
        SetRoutineTimeFragment fragment = new SetRoutineTimeFragment();
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
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_set_routine_time, container, false);

        RunTime = (TextView) view.findViewById(R.id.runTime);
        StartTime = (TextView) view.findViewById(R.id.startTime);
        EndTime = (TextView) view.findViewById(R.id.endTime);
        ResetBtn = (TextView) view.findViewById(R.id.reset);

        StartTimeUP = (ImageView) view.findViewById(R.id.startTimeUP);
        StartTimeDown = (ImageView) view.findViewById(R.id.startTimeDown);
        EndTimeUP = (ImageView) view.findViewById(R.id.endTimeUP);
        EndTimeDown = (ImageView) view.findViewById(R.id.endTimeDown);

        NextBtn = (CardView) view.findViewById(R.id.nextBtn);
        NextTxt = (TextView) view.findViewById(R.id.nextTxt);

        if (getArguments() != null) {
            int[] schedule = getArguments().getIntArray("schedule");
            // 프래그먼트 시작할 때 시작시간, 종료시간을 넘겨 받을 떄 동장 정의
        }

        init();

        StartTimeUP.setOnClickListener(v -> {
            if (startTime < endTime) {
                startTime += 5;
                runTime = endTime - startTime;
                StartTime.setText(TimeToString(startTime));
                RunTime.setText(RuntimeToString(runTime));
                setNextBtn();
            }
        });

        StartTimeDown.setOnClickListener(v -> {
            if (startTime > 0) {
                startTime -= 5;
                runTime = endTime - startTime;
                StartTime.setText(TimeToString(startTime));
                RunTime.setText(RuntimeToString(runTime));
                setNextBtn();
            }
        });

        EndTimeUP.setOnClickListener(v -> {
            if (endTime < 240) {
                endTime += 5;
                runTime = endTime - startTime;
                EndTime.setText(TimeToString(endTime));
                RunTime.setText(RuntimeToString(runTime));
                setNextBtn();
            }
        });

        EndTimeDown.setOnClickListener(v -> {
            if (endTime > startTime) {
                endTime -= 5;
                runTime = endTime - startTime;
                EndTime.setText(TimeToString(endTime));
                RunTime.setText(RuntimeToString(runTime));
                setNextBtn();
            }
        });

        NextBtn.setOnClickListener(v -> {
            if (runTime > 0) {
                mListener.onRoutineSetTime(startTime, endTime);
            }
        });

        return view;
    }

    private void init() {
        startTime = TimeToInt((String) StartTime.getText());
        endTime = TimeToInt((String) StartTime.getText());

        runTime = endTime - startTime;
    }

    private void setNextBtn() {
        if (runTime > 0) {
//            NextBtn = (CardView) view.findViewById(R.id.nextBtn);
            NextTxt.setBackgroundColor(Color.parseColor("#05c78c"));
            NextTxt.setTextColor(Color.parseColor("#ffffff"));
        } else {
            NextTxt.setBackgroundColor(Color.parseColor("#D1D8E2"));
            NextTxt.setTextColor(Color.parseColor("#9AA5B8"));
        }
    }

    private int TimeToInt(String Time) { // 시작 시간 및 종료 시간을 전역으로 가지고 종료시간이 시작 시간보다 빠를 수 없도록 수정할 것
        String am_pm = Time.substring(0, Time.lastIndexOf(" "));
        String temp = Time.substring(Time.lastIndexOf(" ")+1);
        String hour = temp.substring(0, temp.lastIndexOf(":"));
        String min = temp.substring(temp.lastIndexOf(":")+1);

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