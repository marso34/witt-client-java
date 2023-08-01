package com.gwnu.witt;


import android.app.Application;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        setupLifecycleObserver();
    }

    private void setupLifecycleObserver(){
        registerActivityLifecycleCallbacks(new AppLifeCycleTracker());
    }
}
