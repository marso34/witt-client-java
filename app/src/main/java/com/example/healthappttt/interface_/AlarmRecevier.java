package com.example.healthappttt.interface_;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.healthappttt.MainActivity;
import com.example.healthappttt.R;

public class AlarmRecevier extends BroadcastReceiver {

    private static final String TAG = "AlarmReceiver";
    private Context context;
    private int alarmID = 5;
    //로컬에 저장하는 코드 작성할것
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        // 알림창 클릭 시 activity 화면 부름
        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, PendingIntent.FLAG_IMMUTABLE);
        createNotificationChannel();
        showCustomNotification("30","새로운 위트가 왔어요!");
    //
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "chat_channel";
            String channelName = "Chat Channel";
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

    // 커스텀 알림 레이아웃을 사용하여 알림 생성
    private void showCustomNotification(String chatRoomID, String MSG) {
        // 알림 빌더 생성
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "chat_channel")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(chatRoomID)
                .setContentText(MSG)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // 알림 레이아웃을 커스텀한 RemoteViews 생성
        RemoteViews customLayout = new RemoteViews(context.getPackageName(), R.layout.custom_notification_layout);
        customLayout.setTextViewText(R.id.notification_title, chatRoomID);
        customLayout.setTextViewText(R.id.notification_content, MSG);
        builder.setCustomContentView(customLayout);

        // 알림 매니저를 통해 알림 생성
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(alarmID, builder.build());
        alarmID++;
    }


    // 스와이프로 알림 제거 처리
    private void handleNotificationDismiss(int notificationId) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(notificationId);
    }
}
