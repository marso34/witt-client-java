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

import com.example.healthappttt.Chat.ChatActivity;
import com.example.healthappttt.Data.Chat.MSD;
import com.example.healthappttt.Data.Chat.SocketSingleton;

import java.util.Calendar;

import io.socket.client.Socket;

public class DataReceiverService extends Service {
    private static boolean normalExit = false;
    private Handler handler;
    private Runnable reconnectRunnable;
    private Socket socket;
    private ChatActivity chatActivity;
    private SocketSingleton socketSingleton;
    private static Intent service;
    private static final int NOTIFICATION_ID = 123;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();
        normalExit = false;
        // SocketSingleton 인스턴스 생성 및 연결

        socketSingleton = SocketSingleton.getInstance(getBaseContext());
        socketSingleton.connect();
        handler = new Handler();
        reconnectRunnable = new Runnable() {
            @Override
            public void run() {
                if (socketSingleton != null && !socketSingleton.getSocket().connected()) {
                    socketSingleton.connect();
                    socket = socketSingleton.getSocket();
                    Log.d(TAG, "연결됨 id = " + socketSingleton.getSocketId());
                }
                // 일정 시간 간격으로 소켓 연결 상태 확인 및 재연결 작업 수행
                handler.postDelayed(this, 5000); // 5초마다 반복 실행
            }
        };

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 소켓 연결 상태 확인 및 재연결 작업 시작
        //handler.postDelayed(reconnectRunnable, 5000); // 5초 후에 시작
        // 서비스 로직 수행
//        createNotification();
        service = intent;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!normalExit) {
            setAlarmTimer();
        }
//        // 소켓 연결 해제
//        if (socketSingleton != null && socketSingleton.getSocket().connected()) {
//            socketSingleton.disconnect();
//        }

        // Handler 작업 중지
//        handler.removeCallbacks(reconnectRunnable);
    }
    private void setAlarmTimer() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 1);

        Intent intent = new Intent(this, AlarmRecever.class);
        // ...

        PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
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

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void createNotification() {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        builder.setContentTitle(null);
//        builder.setContentText(null);
//
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
//        builder.setContentIntent(pendingIntent);
//
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            manager.createNotificationChannel(new NotificationChannel(
//                    "default",
//                    "기본 채널",
//                    NotificationManager.IMPORTANCE_NONE
//            ));
//        }
//
//        Notification notification = builder.build();
//        startForeground(9, notification);
//        // Foreground 서비스로 정
//
//
//    }





}
