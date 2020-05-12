package com.example.koinapp.data;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import java.io.IOException;
import java.util.List;

public interface CoinReposytory {
    @NonNull
    @WorkerThread
    List<? extends Coin> listings(@NonNull String currency) throws IOException;
}
