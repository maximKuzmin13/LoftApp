package com.example.koinapp;

import android.app.Application;
import android.os.StrictMode;

import com.example.koinapp.util.DebugTree;

import timber.log.Timber;

public class KoinApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
            Timber.plant(new DebugTree());
        }
    }

}
