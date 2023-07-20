package com.example.healthappttt.interface_;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.AlarmManager;
import android.app.NotificationManager;
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
import androidx.core.app.NotificationCompat;

import com.example.healthappttt.Data.Chat.MSD;
import com.example.healthappttt.Data.Chat.SocketSingleton;
import com.example.healthappttt.Data.Exercise.RoutineData;
import com.example.healthappttt.Data.SQLiteUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DataReceiverService extends Service {
    private static boolean normalExit = false;
    public static boolean isServiceRunning = false;
    private Handler handler;
    private SocketSingleton socketSingleton;
    private static final int NOTIFICATION_ID = 123;
    private AlarmManager alarmManager;
    private GregorianCalendar mCalender;

    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;
    SQLiteUtil sqLiteUtil;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();
        normalExit = false;
        // SocketSingleton 인스턴스 생성 및 연결
        socketSingleton = SocketSingleton.getInstance(getBaseContext());
        socketSingleton.initialize();
        handler = new Handler();
        sqLiteUtil = SQLiteUtil.getInstance();
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        mCalender = new GregorianCalendar();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스 로직 수행
        isServiceRunning = true;
        Log.d(TAG, "onStartCommand: 나 시작함");
        handler.post(reconnectRunnable);

        showRoutineAlarm();
        return START_STICKY;
    }

    public static void setNormalExit(boolean normalExit) {
        DataReceiverService.normalExit = normalExit;
    }
    public void showRoutineAlarm() {
        Intent receiverIntent = new Intent(this, AlarmRecevier.class);
        sqLiteUtil.setInitView(getBaseContext(), "RT_TB");
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        ArrayList<RoutineData> routineList = sqLiteUtil.SelectRoutine(dayOfWeek - 1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date currentDate = calendar.getTime();
        for (RoutineData routine : routineList) {
            try {
                Date routineStartTime = dateFormat.parse(routine.getStartTime());
                calendar.setTimeInMillis(System.currentTimeMillis());

// 현재 시간의 시분초를 14:00:00으로 설정
                calendar.set(Calendar.HOUR_OF_DAY, routineStartTime.getHours());
                calendar.set(Calendar.MINUTE, routineStartTime.getMinutes());
                calendar.set(Calendar.SECOND, routineStartTime.getSeconds());
                calendar.set(Calendar.MILLISECOND, 0);

                long timeDifferenceInMillis = calendar.getTimeInMillis() - ( 43 * 60 * 1000);
                Log.d(TAG, "showRoutineAlarm:." + timeDifferenceInMillis+"ss" +currentDate.getTime());
                int i=0;
                if (timeDifferenceInMillis > currentDate.getTime()) {
                    Log.d(TAG, "showRoutineAlarm: 알림실행됨.");
                    calendar.setTimeInMillis(timeDifferenceInMillis);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i++, receiverIntent, PendingIntent.FLAG_IMMUTABLE);
                    alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
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
