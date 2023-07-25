package com.example.healthappttt.interface_;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.healthappttt.Home.AlarmManagerCustom;
import com.example.healthappttt.R;

public class AlarmRecevier extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";
    private Context context;
    private int alarmID = 5;
    //로컬에 저장하는 코드 작성할것
    AlarmManagerCustom alarmManagerCustom;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        alarmManagerCustom = AlarmManagerCustom.getInstance(context);
       createNotificationChannel();
    //
    }

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
        showCustomNotification();
    }

    // 커스텀 알림 표시
    public void showCustomNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Witt_Channel")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(alarmManagerCustom.getTitle())
                .setContentText(alarmManagerCustom.getContent())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // 알림을 탭할 때 수행할 동작을 지정
//        Intent tapIntent = new Intent(context, MainActivity.class);
//        PendingIntent tapPendingIntent = PendingIntent.getActivity(context, 0, tapIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(tapPendingIntent);
        RemoteViews customLayout = new RemoteViews(context.getPackageName(), R.layout.custom_notification_layout);
        //이미지 뭐 띄울지.
        customLayout.setTextViewText(R.id.notification_title, alarmManagerCustom.getTitle());
        customLayout.setTextViewText(R.id.notification_content, alarmManagerCustom.getContent());
        builder.setCustomContentView(customLayout);
        Log.d(TAG, "showCustomNotification: "+alarmManagerCustom.getTitle()+alarmManagerCustom.getContent()+alarmManagerCustom.getEnableVibration() +"   "+alarmManagerCustom.getEnableSound());
        // 진동 설정
        if (alarmManagerCustom.getEnableVibration()) {
            builder.setVibrate(new long[]{100, 200, 300, 400, 500});
        } else {
            builder.setVibrate(new long[]{0}); // 진동 안울리게 설정
        }

        // 소리 설정
        if (alarmManagerCustom.getEnableSound()) {
            builder.setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI);
        } else {
            builder.setDefaults(NotificationCompat.DEFAULT_ALL & ~NotificationCompat.DEFAULT_SOUND); // 소리 안나게 설정
        }

        // 알림 생성 및 표시
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(alarmID, builder.build());
        alarmID++;

    }

    // 알림 해제
    public void handleNotificationDismiss(int notificationId) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(notificationId);
    }
}
