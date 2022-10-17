package com.example.healthappttt;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.net.wifi.aware.DiscoverySession;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button StartBtn, StopBtn, PauseBtn;
    private Button TimerStBtn, TimerReBtn, MinuteBtn, MinuteHarfBtn, SecBtn, SecBtn2;
    private TextView StopWatchTextView;
    private TextView TimerTextView;

    private Thread timeThread = null;
    private Boolean isRunning = true;
    private Boolean TimerRunning = false;

    private int timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        // 스톱워치
        StartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                PauseBtn.setVisibility(View.VISIBLE);
                StopBtn.setVisibility(View.VISIBLE);

                timeThread = new Thread(new timeThread());
                timeThread.start();
            }
        });

        PauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = !isRunning;

                if (isRunning) {
                    PauseBtn.setText("일시정지");
                } else {
                    PauseBtn.setText("재시작");
                }
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

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int mSec = msg.arg1 % 100;
            int sec = (msg.arg1 / 100) % 60;
            int min = (msg.arg1 / 100) / 60 % 60;
            int hour = (msg.arg1 / 100) / (60 * 60);

            if (TimerRunning && timer > 0 && msg.arg1 % 100 == 0)
                timer--; // 1초에 1씩 줄어들게, 스톱워치 동작 중에만 동작

            int sec2 = timer % 60;
            int min2 = timer / 60;

            //1000이 1초 1000*60 은 1분 1000*60*10은 10분 1000*60*60은 한시간

            if (timer == 0) {
                TimerRunning = false;
                TimerStBtn.setText("시작");
            }

            @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d", hour, min, sec);
            @SuppressLint("DefaultLocale") String result2 = String.format("%02d:%02d", min2, sec2);
            StopWatchTextView.setText(result);
            TimerTextView.setText(result2);
        }
    };

    public class timeThread implements Runnable {
        @Override
        public void run() {
            int i = 0;

            while (true) {
                while (isRunning) { //일시정지를 누르면 멈춤
                    Message msg = new Message();
                    msg.arg1 = i++;
                    handler.sendMessage(msg);

                    try {
                        Thread.sleep(10); // 이 방식은 연산속도에 따라 딜레이 생기므로 
                    } catch (InterruptedException e) { // 다른 방식 고려할 것
                        e.printStackTrace();        // System.currentTimeMillis 이용해도 좋을듯
                            @Override               // 타임 관련 메서드 찾을 것
                            public void run() {
                                StopWatchTextView.setText("");
                                StopWatchTextView.setText("00:00:00");
                            }
                        });
                        return; // 인터럽트 받을 경우 return
                    }
                }
            }
        }
    }
}