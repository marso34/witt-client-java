package com.example.healthappttt.Home;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.healthappttt.R;

public class AlarmManagerCustom {

    private static AlarmManagerCustom instance;
    private Context context;
    private static int alarmID = 0;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1;
    private static boolean enableVibration;
    private static boolean enableSound;
    // 프라이빗 생성자
    private AlarmManagerCustom(Context context) {
        this.context = context.getApplicationContext();
    }

    // 싱글톤 인스턴스를 가져오는 메서드
    public static synchronized AlarmManagerCustom getInstance(Context context) {
        if (instance == null) {
            instance = new AlarmManagerCustom(context);
        }
        return instance;
    }

    // 알림 채널 생성
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Witt_Channel";
            String channelName = "Witt_Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);

            // 알림 채널에 대한 추가 설정
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            channel.enableVibration(true);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public void setSounds(int flag){
        if(flag == 1) {// 소리 키고 진동 끄기
            enableVibration = false;
            enableSound = true;
        }
        else if(flag == 2){//소리 끄고 진동 켜기
            enableVibration = true;
            enableSound = false;
        }
        else {// 소리, 진동 다 끄기
            enableVibration = false;
            enableSound = false;
        }
    }

    // 커스텀 알림 표시
    public void showCustomNotification(String title, String content) {
       try {
           createNotificationChannel();
       }finally {


           NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Witt_Channel")
                   .setSmallIcon(R.drawable.notification_icon)
                   .setContentTitle(title)
                   .setContentText(content)
                   .setPriority(NotificationCompat.PRIORITY_DEFAULT);

           // 알림을 탭할 때 수행할 동작을 지정
//        Intent tapIntent = new Intent(context, MainActivity.class);
//        PendingIntent tapPendingIntent = PendingIntent.getActivity(context, 0, tapIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(tapPendingIntent);
           RemoteViews customLayout = new RemoteViews(context.getPackageName(), R.layout.custom_notification_layout);
           //이미지 뭐 띄울지.
           customLayout.setTextViewText(R.id.notification_title, title);
           customLayout.setTextViewText(R.id.notification_content, content);
           builder.setCustomContentView(customLayout);
           Log.d(TAG, "showCustomNotification: "+enableVibration +"   "+enableSound);
           // 진동 설정
           if (enableVibration) {
               builder.setVibrate(new long[]{100, 200, 300, 400, 500});
           } else {
               builder.setVibrate(new long[]{0}); // 진동 안울리게 설정
           }

           // 소리 설정
           if (enableSound) {
               builder.setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI);
           } else {
               builder.setDefaults(NotificationCompat.DEFAULT_ALL & ~NotificationCompat.DEFAULT_SOUND); // 소리 안나게 설정
           }

           // 알림 생성 및 표시
           NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
           notificationManager.notify(alarmID, builder.build());
           alarmID++;
       }
    }

    // 알림 해제
    public void handleNotificationDismiss(int notificationId) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(notificationId);
    }
}
