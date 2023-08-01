package com.gwnu.witt.Record;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gwnu.witt.Data.Exercise.ExerciseData;
import com.gwnu.witt.Data.Exercise.RecordData;
import com.gwnu.witt.Data.Exercise.RoutineData;
import com.gwnu.witt.R;
import com.gwnu.witt.User.AreaAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExerciseResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseResultFragment extends Fragment {
    FragmentExerciseResultBinding binding;

    private AreaAdapter catAdapter;
    private ExerciseResultAdapter Adapter;

    private int totalSet, totalCount, totalVolume;

    private static final String Orange = "#FC673F";
    private static final String Yellow = "#F2BB57";
    private static final String Blue = "#579EF2";
    private static final String Purple = "#8C5AD8";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ROUTINE = "routine";
    private static final String ARG_RECORD = "record";
    private static final String ARG_DATE = "date";


    // TODO: Rename and change types of parameters
    private RoutineData routine;
    private RecordData record;
    private String date;


    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onFinish();
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

    public ExerciseResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param routine Parameter 1.
     * @param record Parameter 2.
     * @return A new instance of fragment ExerciseResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExerciseResultFragment newInstance(RoutineData routine, RecordData record, String date) {
        ExerciseResultFragment fragment = new ExerciseResultFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ROUTINE, routine);
        args.putSerializable(ARG_RECORD, record);
        args.putString(ARG_DATE, date);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            routine = (RoutineData) getArguments().getSerializable(ARG_ROUTINE);
            record = (RecordData) getArguments().getSerializable(ARG_RECORD);
            date = getArguments().getString(ARG_DATE);
        }

        if (date.equals("")) {
            Date currentDate = new Date();
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(currentDate);
            SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 M월 d일");
            date = mFormat.format(currentDate);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExerciseResultBinding.inflate(inflater);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setTotals();
        setRecords();

        binding.day.setText(date);
        binding.set.setText(totalSet + "");
        binding.count.setText(totalCount + "");
        binding.volume.setText(totalVolume + "");
        binding.runTime.setText(record.getRunTime() + "");

        setRecyclerView();

        binding.nextBtn.setOnClickListener(v -> mListener.onFinish());
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

        if (h > 0)          result += h + "시간 ";
        if (m > 0)          result += m + "분 ";
        if (s > 0)          result += s + "초";
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

    private int getRecordsTime() {
        String startTime = record.getStartTime();
        String endTime = record.getEndTime();

        int st = Integer.parseInt(startTime.substring(0, startTime.indexOf(":")));
        int et = Integer.parseInt(endTime.substring(0, endTime.indexOf(":")));
        int time = st + (et-st)/2;

        if (time >= 0 && time < 6)           return 3;
        else if (time >= 6  && time < 12)    return 0;
        else if (time >= 12 && time < 18)    return 1;
        else if (time >= 18 && time < 24)    return 2;

        return 0;
    }

    private void setTotals() {
        for (ExerciseData e : record.getExercises()) {
            if (e.getCat() != 0x40) {
                totalSet += e.getSetOrTime(); // 모든 세트수
                totalCount += e.getSetOrTime() * e.getCntOrDis(); // 세트 수 X 횟수 = 모든 횟수
                totalVolume += e.getVolume() * e.getCntOrDis() * e.getSetOrTime(); // 세트 수 X 횟수 X 무게 = 총 무게
            }
        }
    }

    private void setRecords() {
        switch (getRecordsTime()) {
            case 0:
                binding.timeIcon.setImageResource(R.drawable.baseline_brightness_5_24);
                binding.timeTxt.setText("아침");
                binding.timeTxt.setTextColor(Color.parseColor(Orange));
                break;
            case 1:
                binding.timeIcon.setImageResource(R.drawable.baseline_wb_sunny_24);
                binding.timeTxt.setText("점심");
                binding.timeTxt.setTextColor(Color.parseColor(Yellow));
                break;
            case 2:
                binding.timeIcon.setImageResource(R.drawable.baseline_brightness_3_24);
                binding.timeTxt.setText("저녁");
                binding.timeTxt.setTextColor(Color.parseColor(Blue));
                break;
            case 3:
                binding.timeIcon.setImageResource(R.drawable.baseline_flare_24);
                binding.timeTxt.setText("새벽");
                binding.timeTxt.setTextColor(Color.parseColor(Purple));
                break;
        }

        setCatRecyclerView(record.getCat());
    }

    private void setCatRecyclerView(int cat) {
        ArrayList<String> eCat = new ArrayList<>();

        if ((cat & 0x1)  == 0x1)        eCat.add("가슴");
        if ((cat & 0x2)  == 0x2)        eCat.add("등");
        if ((cat & 0x4)  == 0x4)        eCat.add("어깨");
        if ((cat & 0x8)  == 0x8)        eCat.add("하체");
        if ((cat & 0x10) == 0x10)       eCat.add("팔");
        if ((cat & 0x20) == 0x20)       eCat.add("복근");
        if ((cat & 0x40) == 0x40)       eCat.add("유산소");

        catAdapter = new AreaAdapter(eCat); // 나중에 routine
        binding.catRecyclerView.setHasFixedSize(true);
        binding.catRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.catRecyclerView.setAdapter(catAdapter);
    }

    private void setRecyclerView() {
        Adapter = new ExerciseResultAdapter(routine.getExercises(), record.getExercises());
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(Adapter);
    }
}