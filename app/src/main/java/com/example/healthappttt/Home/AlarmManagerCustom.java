package com.example.healthappttt.Home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.healthappttt.interface_.AlarmRecevier;

import java.util.Calendar;

public class AlarmManagerCustom {

    private static AlarmManagerCustom instance;
    private Context context;
    private  int alarmID = 0;
    private  final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1;
    private  boolean enableVibration = true;
    private  boolean enableSound = false;
    private String title;
    private String content;
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
        Intent receiverIntent = new Intent(context, AlarmRecevier.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmID, receiverIntent, PendingIntent.FLAG_IMMUTABLE);
        Calendar calendar = Calendar.getInstance();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
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

}
