package com.example.healthappttt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.aware.DiscoverySession;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ExercizeRecordActivity extends AppCompatActivity {
    private Timer TimerCall;
    TimerTask timerTask;

    private CardView timerView;

    private Button StartBtn, StopBtn, PauseBtn;
    private Button TimerStBtn, TimerReBtn, MinuteBtn, MinuteHarfBtn, SecBtn, SecBtn2;
    private TextView StopWatchTextView;
    private TextView TimerTextView;

    private Boolean isRunning = true;
    private Boolean TimerRunning = false;

    private int timer;
    private int _timer;

    private long _startTime; // 진짜 시작 시간
    private long startTime;  // 운동시작 계산 위한 시작 시간
    private long pauseTime; // 일시정지 버튼 누른 시간

    private int runTime; // 운동 시간
    private int xrunTime; // 운동 시간 // 타이머 위한


    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercize_record);

        startTime = 0;
        pauseTime = 0;
        runTime = 0;

        TimerCall = new Timer();
        timerView = (CardView) findViewById(R.id.timer);
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

        PauseBtn.setVisibility(View.GONE);
        StopBtn.setVisibility(View.GONE);
        timerView.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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

        Rutin rutin = new Rutin("기본 루틴", "전신", exercizes); // 여기까지 클래스 테스트

        adapter = new ExercizeAdapter(this, rutin);
        recyclerView.setAdapter(adapter);

        StartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                PauseBtn.setVisibility(View.VISIBLE);
                StopBtn.setVisibility(View.VISIBLE);
                timerView.setVisibility(View.VISIBLE);

                _startTime = startTime = System.currentTimeMillis(); // 시작 버튼 누를 시 현재 시간 저장
                isRunning = true;

                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        someWork();
                    }
                };
                TimerCall.schedule(timerTask,0,10); // 0.01초
            }
        });

        PauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = !isRunning;

                if (isRunning) {
                    PauseBtn.setText("일시정지");
                    startTime += (System.currentTimeMillis() - pauseTime); // 다시 시작할 때 정지한 시간 만큼
                    timerView.setVisibility(View.VISIBLE);                 // 운동 시간에서 제외
                } else {
                    PauseBtn.setText("재시작");
                    pauseTime = System.currentTimeMillis(); // 정지 버튼 누른 시간 -> 정지한 시간 기록용
                    timerView.setVisibility(View.GONE);
                }
            }
        });

        StopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        intent.putExtra("record", new Rutin("sss", "ss"));
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
            }
        });

        // 여기서부터는 타이머
        TimerStBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_timer == 0)
                    _timer = timer;

                if (timer != 0)
                    TimerRunning = !TimerRunning;

                if (TimerRunning) {
                    TimerStBtn.setText("중지");
                } else {
                    TimerStBtn.setText("시작");
                }
            }
        });

        TimerReBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimerRunning = false;
                TimerStBtn.setText("시작");
                timer = 0;
                _timer = 0;
            }
        });

        MinuteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer += 60; // 1분 추가
            }
        });

        MinuteHarfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer += 30; // 30초 추가
            }
        });

        SecBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer += 10; // 10초 추가
            }
        });

        SecBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer += 5; // 5초 추가
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

            int sec2 = timer % 60; ; // 타이머는 1이 1초
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
}