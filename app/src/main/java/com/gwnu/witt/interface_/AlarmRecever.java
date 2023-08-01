package com.gwnu.witt.interface_;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class AlarmRecever extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent in = new Intent(context, RestartService.class);
            context.startForegroundService(in);
            Log.d(TAG, "알람리시버로 리스타트 서비스 켬");
        } else {
            Intent in = new Intent(context, DataReceiverService.class);
            context.startService(in);
        }
    }

}