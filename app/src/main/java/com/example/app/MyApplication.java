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
        LeakCanary.install(this);
    }
}
