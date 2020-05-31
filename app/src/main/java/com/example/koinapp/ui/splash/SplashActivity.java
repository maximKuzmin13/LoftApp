package com.example.koinapp.ui.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.koinapp.R;
import com.example.koinapp.ui.main.MainActivity;
import com.example.koinapp.ui.welcome.WelcomeActivity;

public class SplashActivity extends AppCompatActivity {

    private Runnable goNext;
    private final Handler handler = new Handler();
    @VisibleForTesting
    Idling idling = new NoopIdling();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean(WelcomeActivity.KEY_WELCOME, true)) {
            goNext = () -> {
                startActivity(new Intent(this, WelcomeActivity.class));
                idling.idle();
            };
        } else {
            startActivity(new Intent(this, MainActivity.class));
            idling.idle();
        }
        handler.postDelayed(goNext, 1500);
        idling.busy();
    }

    @Override
    protected void onStop() {
        handler.removeCallbacks(goNext);
        super.onStop();
    }

    private static class NoopIdling implements Idling {

        @Override
        public void busy() {

        }

        @Override
        public void idle() {

        }

    }
}
