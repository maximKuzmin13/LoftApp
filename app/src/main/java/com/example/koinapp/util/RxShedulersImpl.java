package com.example.koinapp.util;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Singleton
class RxShedulersImpl implements RxShedulers {

    private final Scheduler ioScheduler;

    @Inject
    RxShedulersImpl(ExecutorService executor) {
        ioScheduler = Schedulers.from(executor);

    }

    @NonNull
    @Override
    public Scheduler io() {
        return ioScheduler;
    }

    @NonNull
    @Override
    public Scheduler cnp() {
        return Schedulers.computation();
    }

    @NonNull
    @Override
    public Scheduler main() {
        return AndroidSchedulers.mainThread();
    }
}
