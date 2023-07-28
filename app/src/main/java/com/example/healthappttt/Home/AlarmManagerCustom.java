package com.example.healthappttt.Home;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.healthappttt.R;
import com.example.healthappttt.Sign.LoginActivity;

public class AlarmManagerCustom {

    private static AlarmManagerCustom instance;
    private Context context;
    private  int alarmID = 0;
    private  final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1;
    private  boolean enableVibration;
    private  boolean enableSound;
    private String title;
    private String content;
    private int flags;
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
    public void onAlarm(String title, String content){
        this.title = title;
        this.content = content;
        createNotificationChannel();
        setSounds(1);


//        Intent receiverIntent = new Intent(context, AlarmRecevier.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmID, receiverIntent, PendingIntent.FLAG_IMMUTABLE);
//        Calendar calendar = Calendar.getInstance();
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
    }
    // 알림 채널 생성
    public String getTitle(){
        return this.title;
    }
    public int getAlarmID(){
        return this.alarmID;
    }
    public void AddAlarmID(){
        ++alarmID;
    }
    public String getContent(){
        return this.content;
    }
    public boolean getEnableVibration(){
        return enableVibration;
    }
    public boolean getEnableSound(){
        return enableSound;
    }
    public void setSounds(int flag){ // 진동 소리 컨트롤 함수
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
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Witt_Channel";
            String channelName = "WittChannel";
            // 진동 설정
            int imp = NotificationManager.IMPORTANCE_HIGH;
//            if (enableVibration) {
//                imp = NotificationManager.IMPORTANCE_LOW;
//            }
//            if (enableSound) {
//                imp = NotificationManager.IMPORTANCE_HIGH;
//            }
//            else if(!enableSound){
//                imp = NotificationManager.IMPORTANCE_LOW;
//            }
            NotificationChannel channel = new NotificationChannel(channelId, channelName, imp);

            // 알림 채널에 대한 추가 설정



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
                .setContentTitle(title)
                .setContentText(content);
        Intent tapIntent = new Intent(context, LoginActivity.class);
        tapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent tapPendingIntent = PendingIntent.getActivity(context, 0, tapIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(tapPendingIntent);
        RemoteViews customLayout = new RemoteViews(context.getPackageName(), R.layout.custom_notification_layout);
        //이미지 뭐 띄울지.
        customLayout.setTextViewText(R.id.notification_title, title);
        customLayout.setTextViewText(R.id.notification_content, content);
        builder.setCustomContentView(customLayout);


        // 알림 생성 및 표시
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(alarmID++, builder.build());
    }

    // 알림 해제
    public void handleNotificationDismiss(int notificationId) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(notificationId);
    }

}
