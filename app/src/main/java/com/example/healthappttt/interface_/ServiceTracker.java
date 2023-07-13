package com.example.healthappttt.interface_;

import android.app.ActivityManager;
import android.content.Context;

public class ServiceTracker {

    public static int countRunningServices(Context context, Class<?> serviceClass) {
        int count = 0;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    count++;
                }
            }
        }
        return count;
    }
}
