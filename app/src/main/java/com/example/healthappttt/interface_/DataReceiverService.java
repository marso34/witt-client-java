package com.example.healthappttt.interface_;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.healthappttt.Chat.ChatActivity;
import com.example.healthappttt.MainActivity;
import com.example.healthappttt.Data.Chat.MSD;
import com.example.healthappttt.Data.Chat.SocketSingleton;
import com.example.healthappttt.R;

import io.socket.client.Socket;

public class DataReceiverService extends Service {

    private Handler handler;
    private Runnable reconnectRunnable;
    private Socket socket;
    private ChatActivity chatActivity;
    private SocketSingleton socketSingleton;

    private static final int NOTIFICATION_ID = 123;

    @Override
    public void onCreate() {
        super.onCreate();

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


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 소켓 연결 상태 확인 및 재연결 작업 시작
        handler.postDelayed(reconnectRunnable, 5000); // 5초 후에 시작

        // 서비스 로직 수행

        createNotification();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        // 소켓 연결 해제
//        if (socketSingleton != null && socketSingleton.getSocket().connected()) {
//            socketSingleton.disconnect();
//        }

        // Handler 작업 중지
//        handler.removeCallbacks(reconnectRunnable);
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

    private void createNotification() {
        // 알림 클릭 시 실행될 액티비티 설정
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        // 알림 채널 생성 (Android 8 이상에서 필요)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // 알림 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("서비스 실행 중")
                .setContentText("데이터 수신 중입니다.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Foreground 서비스로 설정
        Notification notification = builder.build();
        startForeground(NOTIFICATION_ID, notification);
    }

}
