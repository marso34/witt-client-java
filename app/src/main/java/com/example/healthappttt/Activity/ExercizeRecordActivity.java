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
    private LinearLayout StopWatch;     // 스톱워치, 타이머 레이아웃
    private TextView StopWatchTextView; // 운동 시간 보여주는 뷰
    private TextView TimerTextView;     // 휴식시간 보여주는 뷰
    private RecyclerView recyclerView;
    private ExercizeAdapter adapter;
    private Button StopBtn; // 종료 버튼

    private Timer TimerCall;
    private TimerTask timerTask;
    private ArrayList<Exercize> recordExercizes; // 실제 운동을 기록, 어댑터에서 한 기록을 받아옴

    private Boolean isRunning = true;

    private long startTime; // 운동 시작 시간
    private int runTime;    // 총 운동 시간
    private long reStTime;  // 총 운동 시간과 휴식 시간을
    private long pauseTime; // 계산하기 위한 시간 -> 스톱워치 눌렀을 때 현재 시간을 저장

    String exercizeNotes; // 지금은 필요없는 변수, 운동 메모 없어짐

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercize_record);

        StopWatch = (LinearLayout) findViewById(R.id.stopWatch);
        StopWatchTextView = (TextView) findViewById(R.id.stopWatchView);
        TimerTextView = (TextView) findViewById(R.id.timerView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        StopBtn = (Button) findViewById(R.id.stopBtn);

        init(); // 초기화

        setExercizeRecyclerView(createRoutine());   // 운동 리사이클러 뷰 생성

        StopWatch.setOnClickListener(v -> _start(v)); // 스톱워치 눌렀을 때
        StopBtn.setOnClickListener(v -> _stop()); // 운동 종료 버튼 눌렀을 때
    }

    private void init() {
        startTime = 0;
        runTime = 0;
        reStTime = 0;
        pauseTime = 0;

        recordExercizes = new ArrayList<>();
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

    private void _stop() {
        if (startTime != 0) {
            AlertDialog.Builder alert_ex = new AlertDialog.Builder(ExercizeRecordActivity.this);
            alert_ex.setMessage("운동을 끝낼까요?");
            alert_ex.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    isRunning = false;
                    startTime = 0;
                    pauseTime = 0; // 스톱버튼 누를 시 전부 초기화
                    timerTask.cancel();

                    Intent intent = new Intent(getApplicationContext(), ExercizeResultActivity.class);
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
    } // 종료 버튼 눌렀을 때

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

    private Routine createRoutine() {
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

        return routine;
    } // DB 연동 전까지 사용할 루틴 만드는 메서드

    public Routine getRecord() {
        Routine record = new Routine("운동 테스트", "전신", recordExercizes);
        record.setNotes(exercizeNotes);
        record.setStartTime(Long.toString(startTime));
        record.setEndTime(Long.toString(System.currentTimeMillis()));
        record.setRunTime(Long.toString(runTime));

        return record;
    } // 운동 기록을 토대로 루틴 객체를 만드는 메서드

    private void someWork() {
        Message msg = new Message(); // 원래 Message 사용 방법이었는데 수정된 상태 -> 나중에 수정 예정
        // msg.arg1 운동 시간, arg2 휴식시간
        if (isRunning) {
            msg.arg1 = (int) (System.currentTimeMillis() - reStTime); // 현재 시간 - 시작 버튼 누른 시간 = 동작 시간
            msg.arg2 = 0;
        }
        else
            msg.arg2 = (int) (System.currentTimeMillis() - pauseTime);

        handler.sendMessage(msg);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            runTime = msg.arg1;
            int mSec = msg.arg1 % 1000 / 10;
            int sec = (msg.arg1 / 1000) % 60;
            int min = (msg.arg1 / 1000) / 60 % 60;
            int hour = (msg.arg1 / 1000) / (60 * 60);

            int restMSec = msg.arg2 % 1000 / 10;
            int restSec = (msg.arg2 / 1000) % 60;
            int restMin = (msg.arg2 / 1000) / 60;

            @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d", hour, min, sec);
            @SuppressLint("DefaultLocale") String result2 = String.format("%02d:%02d", restMin, restSec);
            StopWatchTextView.setText(result);
            TimerTextView.setText(result2);
        }
    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    } // 뒤로가기 버튼 눌렀을 때 홈화면과 동일한 효과를 내도록, 일단 유지
}