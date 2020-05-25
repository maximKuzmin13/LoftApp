package com.example.koinapp.util;

import androidx.annotation.NonNull;

import io.reactivex.Scheduler;

public interface RxShedulers {
    @NonNull
    Scheduler io();

    @NonNull
    Scheduler cnp();

    @NonNull
    Scheduler main();
}
