package com.example.koinapp.ui;

import android.app.Application;
import android.os.StrictMode;

import com.example.koinapp.BuildConfig;

public class KoinApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
        }
    }

}
