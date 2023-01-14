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
import android.widget.TextView;

import com.example.healthappttt.Data.Exercize;
import com.example.healthappttt.adapter.ExercizeAdapter;
import com.example.healthappttt.R;
import com.example.healthappttt.Data.Routine;
import com.example.healthappttt.Data.Exercize;
import com.example.healthappttt.Data.Set;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ExercizeRecordActivity extends AppCompatActivity {
    private CardView timerView;
    private RecyclerView recyclerView;
    private ExercizeAdapter adapter;

    private Button StartBtn, StopBtn, PauseBtn;
    private Button TimerStBtn, TimerReBtn, MinuteBtn, MinuteHarfBtn, SecBtn, SecBtn2;
    private TextView StopWatchTextView;
    private TextView TimerTextView;

    private Timer TimerCall;
    private TimerTask timerTask;

    private ArrayList<Exercize> recordExercizes; // 실제 운동을 기록, 어댑터에서 한 기록을 받아옴

    private Boolean isRunning = true;
    private Boolean TimerRunning = false;

    private long _startTime; // 진짜 시작 시간
    private long startTime;  // 운동시작 계산 위한 시작 시간
    private long pauseTime; // 일시정지 버튼 누른 시간
    private int runTime; // 운동 시간
    private int xrunTime; // 운동 시간 // 타이머 위한
    private int timer;
    private int _timer;

    String exercizeNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercize_record);

        init();

        // 현재 ui 일부 변경으로 주석과 차이가 있음..
        
        _startTime = 0;
        startTime = 0;
        pauseTime = 0;
        runTime = 0;

        recordExercizes = new ArrayList<>();

        ArrayList<Set> set1 = new ArrayList<>(); // 여기부터
        set1.add(new Set("10", "10", ""));
        set1.add(new Set("20", "7", ""));
        set1.add(new Set("30", "3", ""));
        set1.add(new Set("50", "20", ""));

        ArrayList<Exercize> exercizes = new ArrayList<>();
        exercizes.add(new Exercize("팔굽혀펴기", "기본", set1));
        exercizes.add(new Exercize("스쿼트", "기본", set1));
        exercizes.add(new Exercize("턱걸이", "기본", set1));
        exercizes.add(new Exercize("딥스", "기본", set1));
        exercizes.add(new Exercize("런치", "기본", set1));
        exercizes.add(new Exercize("런치", "기본", set1));
        exercizes.add(new Exercize("런치", "기본", set1));
        exercizes.add(new Exercize("런치", "기본", set1));

        Routine routine = new Routine("기본 루틴", "전신", exercizes); // 여기까지 클래스 테스트

        setExercizeRecyclerView(routine);   // 운동 리사이클러 뷰 생성.

        StartBtn.setVisibility(View.GONE);
        PauseBtn.setVisibility(View.GONE);  // 초기에 시작 버튼을 제외한
//        StopBtn.setVisibility(View.GONE);   // 일시정지, 종료 버튼 표시 안 함
        timerView.setVisibility(View.GONE); // 타이머는 운동중일 때만 표시

        StopWatchTextView.setOnClickListener(v -> _start(v));
//        StartBtn.setOnClickListener(v -> _start(v));
//        PauseBtn.setOnClickListener(v -> _pause());
        StopBtn.setOnClickListener(v -> _stop());

        TimerStBtn.setOnClickListener(v -> _timerStart()); // 여기서부터는 타이머
        TimerReBtn.setOnClickListener(v -> _timerReset());
        MinuteBtn.setOnClickListener(v -> timerPlus(60)); // sec만큼 시간 추가
        MinuteHarfBtn.setOnClickListener(v -> timerPlus(30));
        SecBtn.setOnClickListener(v -> timerPlus(10));
        SecBtn2.setOnClickListener(v -> timerPlus(5));
    }

    private void init() {
        timerView = (CardView) findViewById(R.id.timer);
        recyclerView = findViewById(R.id.recyclerView);

        StartBtn = (Button) findViewById(R.id.startBtn); // 스톱워치
        StopBtn = (Button) findViewById(R.id.stopBtn);
        PauseBtn = (Button) findViewById(R.id.pauseBtn);
        StopWatchTextView = (TextView) findViewById(R.id.stopWatchView);

        TimerStBtn = (Button) findViewById(R.id.timerStBtn); // 타이머
        TimerReBtn = (Button) findViewById(R.id.timerReBtn);
        MinuteBtn = (Button) findViewById(R.id.minuteBtn);
        MinuteHarfBtn = (Button) findViewById(R.id.minuteHarfBtn);
        SecBtn = (Button) findViewById(R.id.secBtn);
        SecBtn2 = (Button) findViewById(R.id.secBtn2);
        TimerTextView = (TextView) findViewById(R.id.timerView);
    }

    private void setExercizeRecyclerView(Routine routine) {
        adapter = new ExercizeAdapter(this, routine);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        if (adapter != null) {
            adapter.setOnExercizeClickListener(new ExercizeAdapter.OnExercizeClick() { // 어댑터 데이터를 전송 받기 위한 인터페이스 콜백
                @Override
                public void onExercizeClick(ArrayList<Exercize> exercizes, String notes) { // 운동 기록과 운동 메모를 전달 받아
                    recordExercizes = new ArrayList<>(); // 액티비티(recordExercizes)에 저장
                    exercizeNotes = notes;

                    for (Exercize i : exercizes) {
                        recordExercizes.add(i);
                    }
                }
            });
        }
    } // 리사이클러 뷰 생성

    public Routine getRecord() {
        Routine record = new Routine("운동 테스트", "전신", recordExercizes);
        record.setNotes(exercizeNotes);
        record.setStartTime(Long.toString(_startTime));
        record.setEndTime(Long.toString(System.currentTimeMillis()));
        record.setRunTime(Long.toString(runTime));

        return record;
    } // 운동 기록을 토대로 루틴 객체를 만드는 함수

    private void _start(View v) {
//        v.setVisibility(View.GONE);
//        PauseBtn.setVisibility(View.VISIBLE);
//        StopBtn.setVisibility(View.VISIBLE);
//        timerView.setVisibility(View.VISIBLE);

        if (_startTime == 0) {
            _startTime = startTime = System.currentTimeMillis(); // 시작 버튼 누를 시 현재 시간 저장
            isRunning = true;

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
//            PauseBtn.setText("일시정지");
                startTime += (System.currentTimeMillis() - pauseTime); // 다시 시작할 때 정지한 시간 만큼
//            timerView.setVisibility(View.VISIBLE);                 // 운동 시간에서 제외
            } else {
//            PauseBtn.setText("재시작");
                pauseTime = System.currentTimeMillis(); // 정지 버튼 누른 시간 -> 정지한 시간 기록용
//            timerView.setVisibility(View.GONE);
            }
        }
    } // 시작 버튼 눌렀을 때 동작

    private void _pause() {
        isRunning = !isRunning;

        if (isRunning) {
//            PauseBtn.setText("일시정지");
            startTime += (System.currentTimeMillis() - pauseTime); // 다시 시작할 때 정지한 시간 만큼
//            timerView.setVisibility(View.VISIBLE);                 // 운동 시간에서 제외
        } else {
//            PauseBtn.setText("재시작");
            pauseTime = System.currentTimeMillis(); // 정지 버튼 누른 시간 -> 정지한 시간 기록용
//            timerView.setVisibility(View.GONE);
        }
    } // 일시정지 버튼 눌렀을 때 동작

    private void _stop() {
        AlertDialog.Builder alert_ex = new AlertDialog.Builder(ExercizeRecordActivity.this);
        alert_ex.setMessage("운동을 끝낼까요?");
        alert_ex.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isRunning = false;
                TimerRunning = false;
                startTime = 0;
                pauseTime = 0;
                timer = 0;
                _timer = 0;// 스톱버튼 누를 시 전부 초기화
                // runTime -> 운동시간은 기록해야 하니 제외

                timerTask.cancel();

                Intent intent = new Intent(getApplicationContext(), ExercizeResultActivity.class);
                intent.putExtra("record", getRecord());
                startActivity(intent);
                finish();
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
    } // 종료 버튼 눌렀을 때

    private void _timerStart() {
        if (_timer == 0)
            _timer = timer;

        if (timer != 0)
            TimerRunning = !TimerRunning;

        if (TimerRunning)
            TimerStBtn.setText("중지");
        else
            TimerStBtn.setText("시작");
    } // 타이머 시작

    private void _timerReset() {
        TimerRunning = false;
        TimerStBtn.setText("시작");
        timer = 0;
        _timer = 0;
    } // 타이머 초기화

    private void timerPlus(int sec) {
        timer += sec;
    }

    private void someWork() {
        Message msg = new Message();

        if (isRunning)
            runTime = (int)(System.currentTimeMillis() - startTime); // 현재 시간 - 시작 버튼 누른 시간 = 동작 시간

        handler.sendMessage(msg);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int mSec = runTime % 1000 / 10;
            int sec = (runTime / 1000) % 60;
            int min = (runTime / 1000) / 60 % 60;
            int hour = (runTime / 1000) / (60 * 60);

            // 1000이 1초 1000*60 은 1분 1000*60*10은 10분 1000*60*60은 한시간

            if (TimerRunning && timer > 0 && xrunTime/1000 < runTime/1000)
                timer--; // 1초에 1씩 줄어들게

            int sec2 = timer % 60; // 타이머는 1이 1초
            int min2 = timer / 60 % 60;

            if (timer == 0) {
                TimerRunning = false;
                TimerStBtn.setText("시작");
                timer = _timer;
                _timer = 0;
            }

            xrunTime = runTime; // 이전 운동 시간을 기록

            @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d", hour, min, sec);
            @SuppressLint("DefaultLocale") String result2 = String.format("%02d:%02d", min2, sec2);
            StopWatchTextView.setText(result);
            TimerTextView.setText(result2);
        }
    };

    public boolean workRunning() {
        return isRunning;
    }

    @Override
    public void onBackPressed() { 
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    } // 뒤로가기 버튼 눌렀을 때 홈화면과 동일한 효과를 내도록
}