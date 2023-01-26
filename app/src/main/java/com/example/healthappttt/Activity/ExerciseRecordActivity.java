package com.example.healthappttt.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.healthappttt.Data.Exercise;
import com.example.healthappttt.adapter.ExerciseAdapter;
import com.example.healthappttt.R;
import com.example.healthappttt.Data.Routine;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ExerciseRecordActivity extends AppCompatActivity {
    private LinearLayout StopWatch;     // 스톱워치, 타이머 레이아웃
    private TextView StopWatchTextView; // 운동 시간 보여주는 뷰
    private TextView TimerTextView;     // 휴식시간 보여주는 뷰
    private RecyclerView recyclerView;
    private ExerciseAdapter adapter;
    private Button StopBtn;  // 종료 버튼
    private TextView StopBtn2; // 오늘은 여기까지 버튼

    private TextView AdapterAerobicTxtView; // 어댑터 안에 있는 텍스트 뷰, 일단 임시
    private ProgressBar AdapterAerobicBar;

    private Timer TimerCall;
    private TimerTask timerTask;

    private Routine routine;
    private ArrayList<Exercise> recordExercises; // 실제 운동을 기록, 어댑터에서 한 기록을 받아옴

    private Boolean isRunning;
    private long startTime; // 운동 시작 시간
    private long reStTime;  // 총 운동 시간과 휴식 시간을
    private long pauseTime; // 계산하기 위한 시간 -> 스톱워치 눌렀을 때 현재 시간을 저장
    private int runTime;    // 총 운동 시간
    private int xRunTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_record);

        StopWatch = (LinearLayout) findViewById(R.id.stopWatch);
        StopWatchTextView = (TextView) findViewById(R.id.stopWatchView);
        TimerTextView = (TextView) findViewById(R.id.timerView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        StopBtn = (Button) findViewById(R.id.stopBtn);
        StopBtn2 = (TextView) findViewById(R.id.stopBtn2);

        Intent intent = getIntent();
        routine = (Routine) intent.getSerializableExtra("routine");

        init(); // 초기화
        setExerciseRecyclerView();   // 운동 리사이클러 뷰 생성

        StopWatch.setOnClickListener(v -> _start(v)); // 스톱워치 눌렀을 때
        StopBtn.setOnClickListener(v -> _stop(v)); // 운동 종료 버튼 눌렀을 때
        StopBtn2.setOnClickListener((v -> _stop(v))); // 여기까지 할래요 버튼
    }

    private void init() { // 운동 기록 부분 나중에 수정
        startTime = 0;
        runTime = 0;
        reStTime = 0;
        pauseTime = 0;
        isRunning = false;

        recordExercises = new ArrayList<Exercise>();
        ArrayList<Exercise> exer = routine.getExercises();

        for (Exercise i : exer) {
            recordExercises.add(new Exercise(i.getTitle(), i.getState(), 0, i.getVolume()));
        }
    }

    private void _start(View v) {
        if (startTime == 0) { // 처음 눌렀을 때
            startTime = reStTime = System.currentTimeMillis(); // 시작 버튼 누를 시 현재 시간 저장
            isRunning = true;

            TimerCall = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    someWork();
                }
            };
            TimerCall.schedule(timerTask,0,10); // 0.01초
        } else { // 그 다음부터
            isRunning = !isRunning;

            if (isRunning)
                reStTime += (System.currentTimeMillis() - pauseTime); // 다시 시작할 때 정지한 시간 만큼 운동 시간에서 제외
            else
                pauseTime = System.currentTimeMillis(); // 정지 버튼 누른 시간
        }
    } // 상단 시간 00:00:00 눌렀을 때 동작

    private void _stop(View v) {
        if (startTime != 0) {
            AlertDialog.Builder alert_ex = new AlertDialog.Builder(ExerciseRecordActivity.this);
            alert_ex.setMessage("운동을 끝낼까요?");
            alert_ex.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    isRunning = false;
                    startTime = 0;
                    pauseTime = 0; // 스톱버튼 누를 시 전부 초기화
                    timerTask.cancel();

                    Intent intent = new Intent(getApplicationContext(), ExerciseResultActivity.class);
                    intent.putExtra("record", getRecord());
                    startActivity(intent);
                    finish(); // 운동 결과 페이지 보여주고 종료
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
    } // 종료 버튼 눌렀을 때. 지금은 운동 시작 안 하면 종료 불가 -> 추후 수정 필요

    private void setExerciseRecyclerView() {
        adapter = new ExerciseAdapter(routine);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        if (adapter != null) {
            adapter.setOnExerciseClickListener(new ExerciseAdapter.OnExerciseClick() { // 어댑터 데이터를 전송 받기 위한 인터페이스 콜백
                @Override
                public void onExerciseClick(int position, CardView cardView, TextView CountView, TextView AerobicTxtView, ProgressBar AerobicBar) { // 운동 기록과 운동 메모를 전달 받아
                    if (isRunning) {
                        Exercise e = routine.getExercises().get(position);
                        String cat = e.getState();
                        int count = e.getCount();
                        int SetCnt = recordExercises.get(position).getCount();

                        if (cat.equals("유산소")) {
                            if (AdapterAerobicBar == null) {
                                AdapterAerobicBar = AerobicBar;
                                AdapterAerobicBar.setProgressDrawable(getDrawable(R.drawable.progressbar_exercise1));
//                                cardView.setBackground();
                                AdapterAerobicTxtView = AerobicTxtView;
                            } else if (AdapterAerobicBar == AerobicBar) {
                                AdapterAerobicBar.setProgressDrawable(getDrawable(R.drawable.progressbar_exercise2));
                                AdapterAerobicBar = null;
                                AdapterAerobicTxtView = null;
                            }
                        } else {
                            if (SetCnt < count)
                                SetCnt++;

                            recordExercises.get(position).setCount(SetCnt);
                            CountView.setText(Integer.toString(SetCnt) + "/" + Integer.toString(count));
                        }
                    }
                }
            });
        }
    } // 리사이클러 뷰 생성 -> 추후 수정 필요

    public Routine getRecord() {
        Routine record = new Routine("운동 테스트", "전신", recordExercises);
        record.setStartTime(Long.toString(startTime));
        record.setEndTime(Long.toString(System.currentTimeMillis()));
        record.setRunTime(Long.toString(runTime));

        return record;
    } // 운동 기록을 토대로 루틴 객체를 만드는 메서드

    private void someWork() {
        Message msg = new Message();
        // arg1 휴식시간
        if (isRunning)
            runTime = (int) (System.currentTimeMillis() - reStTime); // 현재 시간 - 시작 버튼 누른 시간 = 동작 시간
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

            StopWatchTextView.setText(result);
            TimerTextView.setText(result2);

            if (AdapterAerobicBar != null) {
                int t = AdapterAerobicBar.getProgress();

                if (xRunTime < (runTime / 100) && t < AdapterAerobicBar.getMax()) {
                    t++;
//                    recordExercises.get(position).setCount(t / 600); // position 지정 방법 필요, 일단 600으로 나눈 정수(분 단위) 저장
                    AdapterAerobicBar.setProgress(t);
                }

                if (t == AdapterAerobicBar.getMax()) {
                    AdapterAerobicBar.setProgressDrawable(getDrawable(R.drawable.progressbar_exercise2));
                    AdapterAerobicBar = null;
                }

                int Asec  = (t / 10) % 60;
                int Amin  = (t / 10) / 60 % 60;
                int Ahour = (t / 10) / (60 * 60);

                @SuppressLint("DefaultLocale") String resultT = String.format("%02d:%02d:%02d", Ahour, Amin, Asec);
                AdapterAerobicTxtView.setText(resultT);
            }

            xRunTime = runTime / 100;
        }
    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    } // 뒤로가기 버튼 눌렀을 때 홈화면과 동일한 효과를 내도록, 일단 유지
      // 나중에 뒤로가기 눌렀을 종료하시겠습니까? 저장 안 됩니다~ 뜨게 하고 종료하게 할지
      // 아니면 뒤로가고 다시 돌아왔을 때 운동 상택가 유지되게 할지 결정하고 수정
}