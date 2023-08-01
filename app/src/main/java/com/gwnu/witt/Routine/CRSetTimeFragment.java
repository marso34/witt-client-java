package com.gwnu.witt.Routine;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gwnu.witt.R;
import com.gwnu.witt.databinding.FragmentCrSetTimeBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CRSetTimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CRSetTimeFragment extends Fragment {
    FragmentCrSetTimeBinding binding;

    private static final String Background_1 = "#F2F5F9";
    private static final String Background_2 = "#D1D8E2";
    private static final String Background_3 = "#9AA5B8";
    private static final String Signature = "#05C78C";
    private static final String Orange = "#FC673F";
    private static final String Yellow = "#F2BB57";
    private static final String Blue = "#579EF2";
    private static final String Purple = "#8C5AD8";
    private static final String White = "#ffffff";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TIME = "time";
    private static final String ARG_DAY_OF_WEEK = "dayOfWeek";


    // TODO: Rename and change types of parameters
    private int time;
    private int dayOfWeek;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onRoutineSetTime(int Time);
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
     * @param time Parameter 1.
     * @return A new instance of fragment SetRoutineTimeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CRSetTimeFragment newInstance(int time, int dayOfWeek) {
        CRSetTimeFragment fragment = new CRSetTimeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TIME, time);
        args.putInt(ARG_DAY_OF_WEEK, dayOfWeek);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            time = getArguments().getInt(ARG_TIME);
            dayOfWeek = getArguments().getInt(ARG_DAY_OF_WEEK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCrSetTimeBinding.inflate(inflater);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() { mListener.onRoutineSetTime(-1); }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (-1 < time && time < 4)
            setTime(time);

        switch (dayOfWeek) {
            case 0: binding.dayOfWeek.setText("일요일"); break;
            case 1: binding.dayOfWeek.setText("월요일"); break;
            case 2: binding.dayOfWeek.setText("화요일"); break;
            case 3: binding.dayOfWeek.setText("수요일"); break;
            case 4: binding.dayOfWeek.setText("목요일"); break;
            case 5: binding.dayOfWeek.setText("금요일"); break;
            case 6: binding.dayOfWeek.setText("토요일"); break;
        }

        binding.backBtn.setOnClickListener(v -> mListener.onRoutineSetTime(-1));

        binding.morning.setOnClickListener(v -> setTime(0));

        binding.afternoon.setOnClickListener(v -> setTime(1));

        binding.evening.setOnClickListener(v -> setTime(2));

        binding.dawn.setOnClickListener(v -> setTime(3));

        binding.nextBtn.setOnClickListener(v -> {
            if (-1 < time && time < 4) {
                mListener.onRoutineSetTime(time);
            }
        });
    }

    @Override
    public void onDestroyView() {  //프레그먼트가 보여주는 뷰들이 Destroy(파괴)되어도 프레그먼트 객체가 살아있을 수 있기에 뷰들이 메모리에서 없어질때 바인딩클래스의 인스턴스도 파괴함으로서 메모리누수를 방지할 수 있음.
        super.onDestroyView();

        binding = null; //바인딩 객체를 GC(Garbage Collector) 가 없애도록 하기 위해 참조를 끊기
    }

    private void setTime(int t) {
        time = t;
//  ------------------------------------------------------------------------------------------------
        binding.morning.setStrokeWidth(0);
        binding.afternoon.setStrokeWidth(0);
        binding.evening.setStrokeWidth(0);
        binding.dawn.setStrokeWidth(0);
//  ------------------------------------------------------------------------------------------------
        binding.morningIcon.setColorFilter(Color.parseColor(Background_3));
        binding.afternoonIcon.setColorFilter(Color.parseColor(Background_3));
        binding.eveningIcon.setColorFilter(Color.parseColor(Background_3));
        binding.dawnIcon.setColorFilter(Color.parseColor(Background_3));
//  ------------------------------------------------------------------------------------------------
        binding.morningTxt.setTextColor(Color.parseColor(Background_3));
        binding.afternoonTxt.setTextColor(Color.parseColor(Background_3));
        binding.eveningTxt.setTextColor(Color.parseColor(Background_3));
        binding.dawnTxt.setTextColor(Color.parseColor(Background_3));
//  ------------------------------------------------------------------------------------------------
        binding.morningDetail.setTextColor(Color.parseColor(Background_3));
        binding.afternoonDetail.setTextColor(Color.parseColor(Background_3));
        binding.eveningDetail.setTextColor(Color.parseColor(Background_3));
        binding.dawnDetail.setTextColor(Color.parseColor(Background_3));
//  ------------------------------------------------------------------------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            binding.morning.setOutlineSpotShadowColor(Color.parseColor(Background_1));
            binding.afternoon.setOutlineSpotShadowColor(Color.parseColor(Background_1));
            binding.evening.setOutlineSpotShadowColor(Color.parseColor(Background_1));
            binding.dawn.setOutlineSpotShadowColor(Color.parseColor(Background_1));
        }
//  ---------------------------전부 끄고------------------------------------------------------------
        switch (t) { // 필요한 것만 켜기
            case 0:
                binding.morning.setStrokeWidth(2);
                binding.morningIcon.setColorFilter(Color.parseColor(Orange));
                binding.morningTxt.setTextColor(Color.parseColor(Orange));
                binding.morningDetail.setTextColor(Color.parseColor(Orange));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    binding.morning.setOutlineSpotShadowColor(Color.parseColor(Orange));
                break;
            case 1:
                binding.afternoon.setStrokeWidth(2);
                binding.afternoonIcon.setColorFilter(Color.parseColor(Yellow));
                binding.afternoonTxt.setTextColor(Color.parseColor(Yellow));
                binding.afternoonDetail.setTextColor(Color.parseColor(Yellow));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    binding.afternoon.setOutlineSpotShadowColor(Color.parseColor(Yellow));
                break;
            case 2:
                binding.evening.setStrokeWidth(2);
                binding.eveningIcon.setColorFilter(Color.parseColor(Blue));
                binding.eveningTxt.setTextColor(Color.parseColor(Blue));
                binding.eveningDetail.setTextColor(Color.parseColor(Blue));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    binding.evening.setOutlineSpotShadowColor(Color.parseColor(Blue));
                break;
            case 3:
                binding.dawn.setStrokeWidth(2);
                binding.dawnIcon.setColorFilter(Color.parseColor(Purple));
                binding.dawnTxt.setTextColor(Color.parseColor(Purple));
                binding.dawnDetail.setTextColor(Color.parseColor(Purple));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    binding.dawn.setOutlineSpotShadowColor(Color.parseColor(Purple));
                break;
            default: break;
        }

        setNextBtn();
    }

    private void setNextBtn() {
        binding.nextBtn.setBackground(getContext().getDrawable(R.drawable.rectangle_green_20dp));
        binding.nextBtn.setTextColor(Color.parseColor(White));
    }
}