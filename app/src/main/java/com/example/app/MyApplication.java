package com.example.app;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by shenxl on 2018/7/21.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
