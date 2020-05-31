package com.example.koinapp.ui.welcome;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.koinapp.databinding.ActivityWelcomeBinding;
import com.example.koinapp.ui.main.MainActivity;
import com.example.koinapp.widget.CircleIndicator;

public class WelcomeActivity extends AppCompatActivity {

    public static final String KEY_WELCOME = "welcome";
    private SnapHelper helper;
    private ActivityWelcomeBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.rvWelcome.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.rvWelcome.setAdapter(new WelcomeAdapter());
        binding.rvWelcome.addItemDecoration(new CircleIndicator(this));
        helper = new PagerSnapHelper();
        helper.attachToRecyclerView(binding.rvWelcome);
        binding.button.setOnClickListener((v) -> {
            PreferenceManager.getDefaultSharedPreferences(this).edit()
                    .putBoolean(KEY_WELCOME, false)
                    .apply();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        helper.attachToRecyclerView(null);
        binding.rvWelcome.setAdapter(null);
        super.onDestroy();
    }
}
