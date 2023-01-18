package com.example.healthappttt.Activity;

import androidx.annotation.NonNull;
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
    private Button StopBtn;  // 종료 버튼
    private TextView[] AdapterTimerView; // 어댑터 안에 있는 텍스트 뷰, 일단 임시
//    private ProgressBar SetBar;

    private Timer TimerCall;
    private TimerTask timerTask;

    private Routine routine;
    private ArrayList<Exercize> recordExercizes; // 실제 운동을 기록, 어댑터에서 한 기록을 받아옴

    private Boolean isRunning = true;
    private long startTime; // 운동 시작 시간
    private int runTime;    // 총 운동 시간
    private long reStTime;  // 총 운동 시간과 휴식 시간을
    private long pauseTime; // 계산하기 위한 시간 -> 스톱워치 눌렀을 때 현재 시간을 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercize_record);

        StopWatch = (LinearLayout) findViewById(R.id.stopWatch);
        StopWatchTextView = (TextView) findViewById(R.id.stopWatchView);
        TimerTextView = (TextView) findViewById(R.id.timerView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        StopBtn = (Button) findViewById(R.id.stopBtn);

        routine = createRoutine();

        init(); // 초기화
        setExercizeRecyclerView();   // 운동 리사이클러 뷰 생성

        StopWatch.setOnClickListener(v -> _start(v)); // 스톱워치 눌렀을 때
        StopBtn.setOnClickListener(v -> _stop()); // 운동 종료 버튼 눌렀을 때
    }

    private void init() { // 운동 기록 부분 나중에 수정
        startTime = 0;
        runTime = 0;
        reStTime = 0;
        pauseTime = 0;
        isRunning = false;

        recordExercizes = new ArrayList<Exercize>();
        ArrayList<Exercize> exer = routine.getExercizes();

        for (Exercize i : exer) {
            recordExercizes.add(new Exercize(i.getTitle(), i.getState(), "00:00"));
        }

        AdapterTimerView = new TextView[exer.size()];
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
    } // 종료 버튼 눌렀을 때. 지금은 운동 시작 안 하면 종료 불가 -> 추후 수정 필요

    private void setExercizeRecyclerView() {
        adapter = new ExercizeAdapter(this, routine);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        if (adapter != null) {
            adapter.setOnExercizeClickListener(new ExercizeAdapter.OnExercizeClick() { // 어댑터 데이터를 전송 받기 위한 인터페이스 콜백
                @Override
                public void onExercizeClick(int position, TextView CountView) { // 운동 기록과 운동 메모를 전달 받아
                    String state = recordExercizes.get(position).getState();
                    AdapterTimerView[position] = CountView;

                    if (isRunning) {
                        String str1 = routine.getExercizes().get(position).getCount();
                        String str2 = recordExercizes.get(position).getCount();
                        String result = "";

                        if (state == "근력") {
                            String weight, RecordSet, defaultSet;

                            weight = str1.substring(0, str1.lastIndexOf(":")); // 무게, 루틴, 운동 기록 동일
                            defaultSet = str1.substring(str1.lastIndexOf(":")+1); // 루틴에서 가져온 세트 수
                            RecordSet = str2.substring(str2.lastIndexOf(":")+1); // 실제 기록할 세트 수

                            int cnt = Integer.parseInt(RecordSet); // 클릭 시 세트 수를 증가
                            cnt++;
                            RecordSet = Integer.toString(cnt);

                            recordExercizes.get(position).setCount(weight + ":" + RecordSet); // 증가 후 기록

                            result = RecordSet + " / " + defaultSet;
                            CountView.setText(result); // 화면 표시
                        } else { // 일단 여기는 문제가 많음. 시간이 바뀔 때마다 실시간으로 처리해야 하는데
                                 // 그렇게 하려면 handler에서 처리해야 하는데 그럼
                                 // 여기서 처리 못하고 어댑터에서 필요한 데이터 전부 액티비티에 저장해야함
                                 // ex) 프로그레스, 카운트텍스트, 포지션, 클릭 여부 등 뷰 홀더 각각 저장해야함 <- 최소 필요한 정보
                                 // 위 근력 운동의 경우 onExercizeClick에만 존재해도 문제 없음.
                            // 토글 식으로 누르면 시작 다시 누르면 정지
                            // boolean 변수 추개해서 처리
                            int minute = Integer.parseInt(str2.substring(0, str2.lastIndexOf(":")));
                            int sec = Integer.parseInt(str2.substring(str2.lastIndexOf(":")+1));
                            int xRunTime = 0; // 일단은 임시. 시간이 1초 증가한 걸 확인하기 위한 변수 -> 전역으로 처리하든가 해야함

                            if (xRunTime < runTime) {
                                sec++;
                            }

                            if (sec == 60) {
                                sec = 0;
                                minute++;
                            }

                            result = String.format("%02d:%02d", minute, sec);
                            recordExercizes.get(position).setCount(result);
                            CountView.setText(result);
                            xRunTime = runTime;
                        }
                    }
                }
            });
        }
    } // 리사이클러 뷰 생성 -> 추후 수정 필요

    private Routine createRoutine() {
        // 여기부터
        ArrayList<Exercize> exercizes = new ArrayList<>();
        exercizes.add(new Exercize("팔굽혀펴기", "근력", "20:5"));
        exercizes.add(new Exercize("스쿼트", "근력", "70:4"));
        exercizes.add(new Exercize("턱걸이", "근력", "0:3"));
        exercizes.add(new Exercize("딥스", "근력", "0:5"));
        exercizes.add(new Exercize("달리기", "유산소", "30:00"));
        exercizes.add(new Exercize("대충 유산소", "유산소", "20:50"));
        exercizes.add(new Exercize("이것도 유산소", "유산소", "20:50"));
        exercizes.add(new Exercize("이것도 유산소2", "유산소", "20:50"));

        Routine routine = new Routine("기본 루틴", "전신", exercizes); // 여기까지 클래스 테스트

        return routine;
    } // DB 연동 전까지 사용할 루틴 만드는 메서드 -> 추후 수정 필요

    public Routine getRecord() {
        Routine record = new Routine("운동 테스트", "전신", recordExercizes);
        record.setStartTime(Long.toString(startTime));
        record.setEndTime(Long.toString(System.currentTimeMillis()));
        record.setRunTime(Long.toString(runTime));

        return record;
    } // 운동 기록을 토대로 루틴 객체를 만드는 메서드

    private void someWork() {
        Message msg = new Message();
        // arg1 휴식시간
        if (isRunning) {
            runTime = (int) (System.currentTimeMillis() - reStTime); // 현재 시간 - 시작 버튼 누른 시간 = 동작 시간
        }
        else
            msg.arg1 = (int) (System.currentTimeMillis() - pauseTime);

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
            int restMin = (msg.arg1 / 1000) / 60;

            @SuppressLint("DefaultLocale") String result = String.format("%02d:%02d:%02d", hour, min, sec);
            @SuppressLint("DefaultLocale") String result2 = String.format("%02d:%02d", restMin, restSec);

            StopWatchTextView.setText(result);
            TimerTextView.setText(result2);

//            for (TextView i : AdapterTimerView) {
//                i.setText(result);
//            }
                // 여기서 어댑터에 출력할 시간, 프로그레스 바 처리
        }
    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    } // 뒤로가기 버튼 눌렀을 때 홈화면과 동일한 효과를 내도록, 일단 유지
      // 나중에 뒤로가기 눌렀을 종료하시겠습니까? 뜨게 하고 종료하게 할지
      // 아니면 뒤로가고 다시 돌아왔을 때 운동 상택가 유지되게 할지 결정하고 수정
}