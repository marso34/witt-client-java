package com.example.healthappttt.WorkOut;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.healthappttt.Data.Exercise.ExerciseData;
import com.example.healthappttt.R;
import com.example.healthappttt.databinding.FragmentErRecordingBinding;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ERRecordingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ERRecordingFragment extends Fragment {
    FragmentErRecordingBinding binding;
    private ExerciseRecordAdapter adapter;
    private ProgressBar AdapterProgressBar;
    private TextView AdapterTxtView;

    private Timer TimerCall;
    private TimerTask timerTask;

    private Boolean isRunning;
    private long startTime, clickTime, pauseTime; // 운동 시작 시간// 총 운동 시간과 휴식 시간을 // 계산하기 위한 시간 -> 스톱워치 눌렀을 때 현재 시간을 저장
    private int runTime;    // 총 운동 시간

    private ArrayList<ExerciseData> exercises;
    private ArrayList<ExerciseData> recordExercises; // 실제 운동을 기록, 어댑터에서 한 기록을 받아옴
    private int recordPosition;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onRecordExercises(long StartTime, long EndTime, int runTime, ArrayList<ExerciseData> recordExercises);
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

    public ERRecordingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ERRecordingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ERRecordingFragment newInstance(String param1, String param2) {
        ERRecordingFragment fragment = new ERRecordingFragment();
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
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_er_recording, container, false);

        binding = FragmentErRecordingBinding.inflate(inflater);

        exercises = new ArrayList<>();
        recordExercises = new ArrayList<>();

        if (getArguments() != null) {
            exercises = (ArrayList<ExerciseData>) getArguments().getSerializable("exercises");

            for (int i = 0; i < exercises.size(); i++) {
                recordExercises.add(new ExerciseData(exercises.get(i)));
                recordExercises.get(i).setSetOrTime(0);
            }
        }

        init();

        if (exercises != null)
            setRecyclerView();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog.Builder alert_ex = new AlertDialog.Builder(getContext());
                alert_ex.setMessage("운동이 기록되지 않습니다");
                alert_ex.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isRunning = false;
                        pauseTime = 0; // 스톱버튼 누를 시 전부 초기화
                        if (timerTask != null) {
                            timerTask.cancel();
                            TimerCall.purge();
                        }
                        requireActivity().finish();
                    }
                });
                alert_ex.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                  // 아니오 누를 시 아무것도 안 함
                    }
                });
                alert_ex.setTitle("운동을 끝내시겠습니까?");
                AlertDialog alert = alert_ex.create();
                alert.show();

            }
        });

        binding.stopWatch.setOnClickListener(v -> StartStopWatch());
        binding.stopBtn.setOnClickListener(v -> EndRocording());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    private void init() {
        startTime = 0;
        runTime = 0;
        clickTime = 0;
        pauseTime = 0;
        isRunning = false;

        recordPosition = -1;
    }

    private void setRecyclerView() {
        adapter = new ExerciseRecordAdapter(exercises);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);

        if (adapter != null) {
            adapter.setOnExerciseClickListener(new ExerciseRecordAdapter.OnExerciseClick() {
                @Override
                public void onExerciseClick(int position, TextView CardioTxtView, ProgressBar progressBar) {
                    if (isRunning) {
                        ExerciseData e = exercises.get(position);

                        if (e.getCat() == 0x40) { // 유산소의 경우
                            if (AdapterProgressBar == null) {
                                recordPosition = position;
                                AdapterProgressBar = progressBar;
                                AdapterTxtView = CardioTxtView;
                            } else if (AdapterProgressBar == progressBar) {
                                recordPosition = -1;
                                AdapterProgressBar = null;
                                AdapterTxtView = null;
                            }
                        } else {
                            int count = progressBar.getProgress();
                            count++;

                            progressBar.setProgress(count);
                            recordExercises.get(position).setSetOrTime(count);
                        }
                    }
                }
            });
        }
    }

    private void StartStopWatch() {
        if (startTime == 0) {
            startTime = clickTime = System.currentTimeMillis();
            isRunning = true;

            binding.stopTxt.setTextColor(Color.parseColor("#ffffff"));
            binding.stopTxt.setBackgroundColor(Color.parseColor("#05c78c"));

            binding.runTimeTxt.setTextColor(Color.parseColor("#1B202D"));
            binding.runTimeView.setTextColor(Color.parseColor("#1B202D"));
            binding.restTimeTxt.setTextColor(Color.parseColor("#9AA5B8"));
            binding.restTimeView.setTextColor(Color.parseColor("#9AA5B8"));

            TimerCall = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    someWork();
                }
            };
            TimerCall.schedule(timerTask,0,10); // 0.01초

        } else {
            isRunning = !isRunning;

            if (isRunning) {
                clickTime += (System.currentTimeMillis() - pauseTime);
                binding.runTimeTxt.setTextColor(Color.parseColor("#1B202D"));  // 나중에 메서드화
                binding.runTimeView.setTextColor(Color.parseColor("#1B202D"));
                binding.restTimeTxt.setTextColor(Color.parseColor("#9AA5B8"));
                binding.restTimeView.setTextColor(Color.parseColor("#9AA5B8"));
            } else {
                pauseTime = System.currentTimeMillis();
                binding.runTimeTxt.setTextColor(Color.parseColor("#9AA5B8"));
                binding.runTimeView.setTextColor(Color.parseColor("#9AA5B8"));
                binding.restTimeTxt.setTextColor(Color.parseColor("#1B202D"));
                binding.restTimeView.setTextColor(Color.parseColor("#1B202D"));
            }
        }
    }

    private void EndRocording() {
        if (startTime != 0) {
            AlertDialog.Builder alert_ex = new AlertDialog.Builder(getContext());
            alert_ex.setMessage("운동을 끝낼까요?");
            alert_ex.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    isRunning = false;
                    pauseTime = 0; // 스톱버튼 누를 시 전부 초기화
                    timerTask.cancel();
                    TimerCall.purge();

                    mListener.onRecordExercises(startTime, System.currentTimeMillis(), runTime, recordExercises); // 시작시간, 종료시간(현재시간), 운동시간, 운동
                }
            });
            alert_ex.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                  // 아니오 누를 시 아무것도 안 함
                }
            });
            alert_ex.setTitle("수고하셨습니다!");
            AlertDialog alert = alert_ex.create();
            alert.show();
        }
    }

    private void someWork() {
        Message msg = new Message();

        if (isRunning)
            runTime = (int) (System.currentTimeMillis() - clickTime); // 현재 시간 - 시작 버튼 누른 시간 = 동작 시간
        else
            msg.arg1 = (int) (System.currentTimeMillis() - pauseTime); // 휴식시간

        handler.sendMessage(msg);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int mSec = runTime % 1000 / 10; // msec은 언젠가 쓸 수도 있음
            int sec = (runTime / 1000) % 60;
            int min = (runTime / 1000) / 60 % 60;
            int hour = (runTime / 1000) / (60 * 60);

            int restMSec = msg.arg1 % 1000 / 10; // msec은 언젠가 쓸 수도 있음
            int restSec = (msg.arg1 / 1000) % 60;
            int restMin = (msg.arg1 / 1000) / 60 % 60;
            int restHour = (msg.arg1 / 1000) / (60 * 60);

            @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d", hour, min, sec);
            @SuppressLint("DefaultLocale") String result2 = String.format("%02d:%02d:%02d", restHour, restMin, restSec);

            binding.runTimeView.setText(result);
            binding.restTimeView.setText(result2);

            if (AdapterProgressBar != null) { // 이 부분은 수정 필요
                int progress = AdapterProgressBar.getProgress();

                if (isRunning && progress < AdapterProgressBar.getMax()) {
                    progress++; // 이 부분 수정 필요, 지금은 스레드를 활용하여 0.01초마다 1씩 추가하는 코드 -> 스레드를 활용하다보니 지연 가능성 있음
                    int Asec  = (progress / 100) % 60;
                    int Amin  = (progress / 100) / 60 % 60;
                    int Ahour = (progress / 100) / (60 * 60);

                    @SuppressLint("DefaultLocale") String resultT = String.format("%02d:%02d:%02d", Ahour, Amin, Asec);

                    AdapterProgressBar.setProgress(progress);
                    AdapterTxtView.setText(resultT);

                    recordExercises.get(recordPosition).setSetOrTime(progress/6000);
                }

                if (progress == AdapterProgressBar.getMax()) {
                    adapter.notifyDataSetChanged();
                    AdapterProgressBar = null;
                    AdapterTxtView = null;
                }
            }
        }
    };
}