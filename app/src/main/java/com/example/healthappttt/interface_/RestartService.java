package com.example.healthappttt.interface_;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.healthappttt.MainActivity;
import com.example.healthappttt.R;

public class RestartService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(null);
        builder.setContentText(null);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                    new NotificationChannel(
                            "default",
                            "기본 채널",
                            NotificationManager.IMPORTANCE_NONE
                    )
            );
        }

        Notification notification = builder.build();
        startForeground(9, notification);
        Log.d(TAG, "  ");
        Intent serviceIntent = new Intent(this, DataReceiverService.class);
        // ...
        if(DataReceiverService.isServiceRunning == false)
            startService(serviceIntent);
        stopForeground(true);
        stopSelf();

        return START_NOT_STICKY;
    }
}
