package com.gwnu.witt;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

class AppLifeCycleTracker implements Application.ActivityLifecycleCallbacks {

    private int numStarted = 0;
    public static boolean on = false;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (numStarted == 0){
            //went to foreground
            on = true;
        }
        numStarted++;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        numStarted--;
        if (numStarted == 0){
            //went to background
            on = false;
        }

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}// end AppLifeCycleTracker