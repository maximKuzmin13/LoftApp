package com.example.koinapp.util;

import androidx.annotation.NonNull;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class TestShedulers implements RxShedulers {
    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.trampoline();
    }

    @NonNull
    @Override
    public Scheduler cnp() {
        return Schedulers.trampoline();
    }

    @NonNull
    @Override
    public Scheduler main() {
        return Schedulers.trampoline();
    }
}
