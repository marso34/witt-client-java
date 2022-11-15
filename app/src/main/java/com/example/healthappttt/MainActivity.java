package com.example.healthappttt;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.wifi.aware.DiscoverySession;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Timer TimerCall;
    TimerTask timerTask;

    private LinearLayout timerLayout;

    private Button StartBtn, StopBtn, PauseBtn;
    private Button TimerStBtn, TimerReBtn, MinuteBtn, MinuteHarfBtn, SecBtn, SecBtn2;
    private TextView StopWatchTextView;
    private TextView TimerTextView;

    private Boolean isRunning = true;
    private Boolean TimerRunning = false;

    private int timer;

    private long startTime; // 스톱워치 시작 버튼 누른 시간
    private long pauseTime; // 일시정지 버튼 누른 시간

    private int runTime; // 운동 시간
    private int xrunTime; // 운동 시간

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startTime = 0;
        pauseTime = 0;
        runTime = 0;

        TimerCall = new Timer();
        timerLayout = (LinearLayout) findViewById(R.id.timer);
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
        timerLayout.setVisibility(View.GONE);

        // 스톱워치
        StartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                PauseBtn.setVisibility(View.VISIBLE);
                StopBtn.setVisibility(View.VISIBLE);
                timerLayout.setVisibility(View.VISIBLE);

                startTime = System.currentTimeMillis();
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
                    startTime += (System.currentTimeMillis() - pauseTime);
                    timerLayout.setVisibility(View.VISIBLE);
                } else {
                    PauseBtn.setText("재시작");
                    pauseTime = System.currentTimeMillis();
                    timerLayout.setVisibility(View.GONE);
                }
            }
        });

        StopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = false;
                TimerRunning = false;
                startTime = 0;
                pauseTime = 0;
                timer = 0;

                StartBtn.setVisibility(View.VISIBLE);
                PauseBtn.setVisibility(View.GONE);
                StopBtn.setVisibility(View.GONE);
//                StopWatchTextView.setText("00:00:00");
            }
        });

        // 여기서부터는 타이머
        TimerStBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private void someWork() {
        Message msg = new Message();

        if (isRunning)
            runTime = (int)(System.currentTimeMillis() - startTime);

        handler.sendMessage(msg);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int mSec = runTime % 1000;
            int sec = (runTime / 1000) % 60;
            int min = (runTime / 1000) / 60 % 60;
            int hour = (runTime / 1000) / (60 * 60);

            if (TimerRunning && timer > 0 && xrunTime/1000 < runTime/1000)
                timer--; // 1초에 1씩 줄어들게

            int sec2 = timer % 60;
            int min2 = timer / 60 % 60;
            int hour2 = timer / (60 * 60);

            //1000이 1초 1000*60 은 1분 1000*60*10은 10분 1000*60*60은 한시간

            if (timer == 0) {
                TimerRunning = false;
                TimerStBtn.setText("시작");
            }

            xrunTime = runTime;

            @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d", hour, min, sec, mSec);
            @SuppressLint("DefaultLocale") String result2 = String.format("%02d:%02d:%02d", hour2, min2, sec2);
            StopWatchTextView.setText(result);
            TimerTextView.setText(result2);
        }
    };
}