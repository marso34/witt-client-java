package com.example.healthappttt.interface_;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.healthappttt.Data.Chat.MSD;
import com.example.healthappttt.Data.Chat.SocketSingleton;

import java.util.Calendar;

public class DataReceiverService extends Service {
    private static boolean normalExit = false;
    public static boolean isServiceRunning = false;
    private Handler handler;
    private SocketSingleton socketSingleton;
    private static final int NOTIFICATION_ID = 123;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();
        normalExit = false;
        // SocketSingleton 인스턴스 생성 및 연결
        socketSingleton = SocketSingleton.getInstance(getBaseContext());
        socketSingleton.initialize();
        handler = new Handler();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스 로직 수행
        isServiceRunning = true;
        handler.post(reconnectRunnable);
        return START_STICKY;
    }

    public static void setNormalExit(boolean normalExit) {
        DataReceiverService.normalExit = normalExit;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServiceRunning = false;
        if (!normalExit) {
            setAlarmTimer();
        } else {
            Log.d(TAG, "onDestroy:QQQ ");
            socketSingleton.disconnect();
        }
    }

    private Runnable reconnectRunnable = new Runnable() {
        @Override
        public void run() {
            if(socketSingleton !=null && !socketSingleton.getSocket().connected()) {
                socketSingleton.connect();
                Log.d(TAG, "run: 부활시킴");
            }
            // 일정 시간 간격으로 소켓 연결 상태 확인 및 재연결 작업 수행
            handler.postDelayed(this, 20000); // 5초마다 반복 실행
        }
    };

    private void setAlarmTimer() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 1);

        Intent intent = new Intent(this, AlarmRecever.class);
        // ...

        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void handleReceivedMessage(MSD message) {
        // 받은 메시지를 처리하는 로직을 구현
        Log.d("DataReceiverService", "Received message: " + message);
        // 처리 로직 작성
    }
}
