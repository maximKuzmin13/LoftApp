package com.example.koinapp;

import android.app.Application;
import android.os.StrictMode;

import com.example.koinapp.util.DebugTree;
import com.google.firebase.iid.FirebaseInstanceId;

import timber.log.Timber;

public class KoinApp extends Application {

    private BaseComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            StrictMode.enableDefaults();
            Timber.plant(new DebugTree());
        }
        component = DaggerDaggerComponent.builder()
                .application(this)
                .build();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult ->
                Timber.d("fcm: %s", instanceIdResult.getToken()));
    }


    public BaseComponent getComponent() {
        return component;
    }

}
