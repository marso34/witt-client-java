package com.gwnu.witt.WorkOut;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.healthappttt.R;
import com.example.healthappttt.databinding.FragmentErRecordingBinding;
import com.gwnu.witt.Data.Exercise.ExerciseData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ERRecordingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ERRecordingFragment extends Fragment {
    FragmentErRecordingBinding binding;

    private static final String Body = "#4A5567";
    private static final String Background_1 = "#F2F5F9";
    private static final String Background_2 = "#D1D8E2";
    private static final String Background_3 = "#9AA5B8";
    private static final String Standard = "#1B202D";
    private static final String Signature = "#05C78C";
    private static final String Yellow = "#F2BB57";
    private static final String Blue = "#579EF2";
    private static final String White = "#ffffff";


    private ExerciseRecordAdapter adapter;
    private ProgressBar AdapterProgressBar;
    private TextView AdapterTxtView;

    private Timer TimerCall;
    private TimerTask timerTask;

    private Boolean isRunning, isUnLock;
    private long startTime, clickTime, pauseTime; // 운동 시작 시간// 총 운동 시간과 휴식 시간을 // 계산하기 위한 시간 -> 스톱워치 눌렀을 때 현재 시간을 저장
    private int runTime;    // 총 운동 시간

    private ArrayList<ExerciseData> recordExercises; // 실제 운동을 기록, 어댑터에서 한 기록을 받아옴
    private int recordPosition, recordSize;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_EXERCISES = "exercises";

    // TODO: Rename and change types of parameters
    private ArrayList<ExerciseData> exercises;

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onRecordExercises(String StartTime, String EndTime, String runTime, ArrayList<ExerciseData> recordExercises);
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
     * @param exercises Parameter 1.
     * @return A new instance of fragment ERRecordingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ERRecordingFragment newInstance(ArrayList<ExerciseData> exercises) {
        ERRecordingFragment fragment = new ERRecordingFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EXERCISES, exercises);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            exercises = (ArrayList<ExerciseData>) getArguments().getSerializable(ARG_EXERCISES);
            recordExercises = new ArrayList<>();

            for (int i = 0; i < exercises.size(); i++) {
                recordExercises.add(new ExerciseData(exercises.get(i)));
                recordExercises.get(i).setSetOrTime(0);
            }
        }
        isUnLock = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentErRecordingBinding.inflate(inflater);

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
                            timerTask = null;
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

        binding.stopWatchSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                isRunning = !isChecked;

                if (isChecked) { // 이때가 휴식 중, isRunning == false
                    binding.playIcon.setColorFilter(Color.parseColor(Background_2));
                    binding.playTxt.setTextColor(Color.parseColor(Background_2));
                    binding.pauseIcon.setColorFilter(Color.parseColor(Blue));
                    binding.pauseTxt.setTextColor(Color.parseColor(Blue));
                    binding.runTimeView.setTextColor(Color.parseColor(Blue));

                    if (startTime != 0)
                        pauseTime = System.currentTimeMillis();
                } else { // 이 때가 운동 중, isRunning == true
                    binding.playIcon.setColorFilter(Color.parseColor(Body));
                    binding.playTxt.setTextColor(Color.parseColor(Body));
                    binding.pauseIcon.setColorFilter(Color.parseColor(Background_2));
                    binding.pauseTxt.setTextColor(Color.parseColor(Background_2));
                    binding.runTimeView.setTextColor(Color.parseColor(Standard));

                    if (startTime != 0)
                        clickTime += (System.currentTimeMillis() - pauseTime);
                }

                if (startTime == 0) {
                    startTime = clickTime = System.currentTimeMillis();

                    TimerCall = new Timer();
                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            someWork();
                        }
                    };
                    TimerCall.schedule(timerTask,0,10); // 0.01초
                }
            }
        });

        binding.floating.setOnClickListener(v -> {
            if (isUnLock) {
                binding.lockIcon.setVisibility(View.VISIBLE);
                binding.stopBtn.setCardBackgroundColor(Color.parseColor(Background_2));
                binding.stopTxt.setTextColor(Color.parseColor(Background_3));

                binding.floatingIcon.setImageResource(R.drawable.ic_baseline_info_24);
                binding.floatingIcon.setColorFilter(Color.parseColor(Yellow));
                binding.floatingTxt.setText("길게 누르면 잠금이 해제돼요");

                isUnLock = false;
            }
        });

        binding.stopBtn.setOnClickListener(v -> EndRocording());

        binding.stopBtn.setOnLongClickListener(v -> {
            if (!isUnLock) {
                binding.lockIcon.setVisibility(View.GONE);
                binding.stopBtn.setCardBackgroundColor(Color.parseColor(Signature));
                binding.stopTxt.setTextColor(Color.parseColor(White));

                binding.floatingIcon.setImageResource(R.drawable.baseline_https_24);
                binding.floatingIcon.setColorFilter(Color.parseColor(Background_3));
                binding.floatingTxt.setText("다시 잠그기");

                isUnLock = true;
            }

            return true;
        });
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
        adapter = new ExerciseRecordAdapter(getContext(), exercises);
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

                            if (count < progressBar.getMax()) {
                                count++;
                                progressBar.setProgress(count);
                                recordExercises.get(position).setSetOrTime(count);

                                if (count == progressBar.getMax())
                                    recordSize++;

                                if (recordSize == recordExercises.size())
                                    Complete();
                            }
                        }
                    }
                }
            });
        }
    }

    private String TimeToString(long time) {
        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String result = dateFormat.format(date);

        return result;
    }

    private void Complete() {
        isUnLock = true;

        binding.lockIcon.setVisibility(View.GONE);
        binding.stopBtn.setCardBackgroundColor(Color.parseColor(Signature));
        binding.stopTxt.setTextColor(Color.parseColor(White));
        binding.floating.setVisibility(View.GONE);

        binding.stopWatchSwitch.setVisibility(View.GONE);
        binding.stopWatchTxt.setVisibility(View.GONE);
        binding.endLayout.setVisibility(View.VISIBLE);

        if (timerTask != null) {
            timerTask.cancel();
            TimerCall.purge();
            timerTask = null;
        }
    }

    private void EndRocording() {
        if (startTime != 0 && isUnLock) {
            isRunning = false;
            pauseTime = 0; // 스톱버튼 누를 시 전부 초기화

            if (timerTask != null) {
                timerTask.cancel();
                TimerCall.purge();
                timerTask = null;
            }

            mListener.onRecordExercises(TimeToString(startTime), TimeToString(System.currentTimeMillis()), binding.runTimeView.getText().toString(), recordExercises); // 시작시간, 종료시간(현재시간), 운동시간, 운동
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
//            binding.restTimeView.setText(result2);

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

                    if (progress == AdapterProgressBar.getMax())
                        recordSize++;

                    if (recordSize == recordExercises.size())
                        Complete();
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