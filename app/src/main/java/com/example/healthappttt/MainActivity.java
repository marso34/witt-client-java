package com.example.healthappttt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private int xrunTime; // 운동 시간 // 타이머 위한


    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;


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


//
        StartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                PauseBtn.setVisibility(View.VISIBLE);
                StopBtn.setVisibility(View.VISIBLE);
                timerLayout.setVisibility(View.VISIBLE);

                startTime = System.currentTimeMillis(); // 시작 버튼 누를 시 현재 시간 저장
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
                    timerLayout.setVisibility(View.VISIBLE);               // 운동 시간에서 제외
                } else {
                    PauseBtn.setText("재시작");
                    pauseTime = System.currentTimeMillis(); // 정지 버튼 누른 시간 -> 정지한 시간 기록용
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
                timer = 0;  // 스톱버튼 누를 시 전부 초기화
                            // runTime -> 운동시간은 기록해야 하니 제외

                StartBtn.setVisibility(View.VISIBLE);
                PauseBtn.setVisibility(View.GONE);
                StopBtn.setVisibility(View.GONE);
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


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this); // 이건 나중에
        recyclerView.setLayoutManager(layoutManager);

        String[] Name =  {"팔굽혀펴기","스쿼트","테스트"};
        int[] SetNum =  {5, 10, 8};
        int[] Num =  {15, 10, 8};
        int[] Weight = {0, 30, 50};
        int[] setCnt = {0,0,0};

        adapter = new ExercizeAdapter(Name, SetNum, Num, Weight, setCnt);
        recyclerView.setAdapter(adapter);

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

            int sec2 = timer % 60;
            int min2 = timer / 60 % 60;
            int hour2 = timer / (60 * 60); // 타이머는 1이 1초

            if (timer == 0) {
                TimerRunning = false;
                TimerStBtn.setText("시작");
            }

            xrunTime = runTime; // 이전 운동 시간을 기록

            @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d", hour, min, sec);
            @SuppressLint("DefaultLocale") String result2 = String.format("%02d:%02d:%02d", hour2, min2, sec2);
            StopWatchTextView.setText(result);
            TimerTextView.setText(result2);
        }
    };
}