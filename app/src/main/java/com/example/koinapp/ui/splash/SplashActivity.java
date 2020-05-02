package com.example.koinapp.ui.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.koinapp.R;
import com.example.koinapp.ui.main.MainActivity;
import com.example.koinapp.ui.welcome.WelcomeActivity;

public class SplashActivity extends AppCompatActivity {

    private Runnable goNext;
    private SharedPreferences sharedPreferences;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean(WelcomeActivity.KEY_WELCOME, true)) {
            goNext = () -> {
                startActivity(new Intent(this, WelcomeActivity.class));
            };
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        handler.postDelayed(goNext, 1500);
    }

    @Override
    protected void onStop() {
        handler.removeCallbacks(goNext);
        super.onStop();
    }
}
