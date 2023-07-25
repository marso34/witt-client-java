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

import com.example.healthappttt.R;
import com.example.healthappttt.databinding.FragmentCrSetTimeBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CRSetTimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CRSetTimeFragment extends Fragment {
    FragmentCrSetTimeBinding binding;

    private static final String Backgrount_2 = "#D1D8E2";
    private static final String Signature = "#05C78C";
    private static final String Orange = "#FC673F";
    private static final String Yellow = "#F2BB57";
    private static final String Blue = "#579EF2";
    private static final String Purple = "#8C5AD8";
    private static final String White = "#ffffff";

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
        if (getArguments() != null) {
            schedule = getArguments().getIntArray("schedule");
            // 프래그먼트 시작할 때 시작시간, 종료시간을 넘겨 받을 떄 동장 정의
        }

        binding = FragmentCrSetTimeBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.morning.setOnClickListener(v -> {

        });

        binding.afternoon.setOnClickListener(v -> {});

        binding.evening.setOnClickListener(v -> {});

        binding.dawn.setOnClickListener(v -> {});

        binding.nextBtn.setOnClickListener(v -> {
            if (runTime > 0) {
//                mListener.onRoutineSetTime(startTime, endTime);
            }
        });
    }

    @Override
    public void onDestroyView() {  //프레그먼트가 보여주는 뷰들이 Destroy(파괴)되어도 프레그먼트 객체가 살아있을 수 있기에 뷰들이 메모리에서 없어질때 바인딩클래스의 인스턴스도 파괴함으로서 메모리누수를 방지할 수 있음.
        super.onDestroyView();

        binding = null; //바인딩 객체를 GC(Garbage Collector) 가 없애도록 하기 위해 참조를 끊기
    }

    private void setNextBtn() {
        if (runTime > 0) {
            binding.nextBtn.setBackground(getContext().getDrawable(R.drawable.rectangle_green_20dp));
            binding.nextBtn.setTextColor(Color.parseColor(White));
        } else {
//            binding.nextBtn.setBackgroundColor(Color.parseColor("#D1D8E2"));
//            binding.nextBtn.setTextColor(Color.parseColor("#9AA5B8"));
        }
    }
}