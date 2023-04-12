package com.example.healthappttt.Fragment;

import android.annotation.SuppressLint;
import android.media.Image;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.healthappttt.R;

import org.w3c.dom.Text;

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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        // Inflate the layout for this fragment
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

        StartTimeUP.setOnClickListener(v -> {
            String str = setText((String) StartTime.getText(), 5);
            StartTime.setText(str);
        });

        StartTimeDown.setOnClickListener(v -> {
            String str = setText((String) StartTime.getText(), -5);
            StartTime.setText(str);
        });

        EndTimeUP.setOnClickListener(v -> {
            String str = setText((String) EndTime.getText(), 5);
            EndTime.setText(str);
        });

        EndTimeDown.setOnClickListener(v -> {
            String str = setText((String) EndTime.getText(), -5);
            EndTime.setText(str);
        });


        return view;
    }

    private String setText(String Time, int num) { // 시작 시간 및 종료 시간을 전역으로 가지고 종료시간이 시작 시간보다 빠를 수 없도록 수정할 것
        String am_pm = Time.substring(0, Time.lastIndexOf(" "));
        String temp = Time.substring(Time.lastIndexOf(" ")+1);
        String hour = temp.substring(0, temp.lastIndexOf(":"));
        String min = temp.substring(temp.lastIndexOf(":")+1);

        int T = Integer.parseInt(hour) * 10 + Integer.parseInt(min) / 6;

        if (am_pm.equals("오후")) T += 120;
        if (T == 120 || T == 125 || T == 240 || T == 245) T-= 120;

        T += num;

        if (T >= 240) T-= 240;

        if (T < 120) {
            am_pm = "오전";
            if (T < 10) T += 120;
        }
        else {
            am_pm = "오후";
            if (T >= 130) T-= 120;
        }

        @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d", T/10, T % 10 * 6);

        return am_pm + " " + result;
    }
}