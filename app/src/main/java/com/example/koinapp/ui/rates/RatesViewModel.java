package com.example.koinapp.ui.rates;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.koinapp.data.Coin;
import com.example.koinapp.data.CoinReposytory;
import com.example.koinapp.data.CoinReposytoryImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RatesViewModel extends ViewModel {
    private final MutableLiveData<List<Coin>> coins = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isRefreshing = new MutableLiveData<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final CoinReposytory reposytory;

    public RatesViewModel() {
        reposytory = new CoinReposytoryImpl();
        refresh();
    }

    private Future<?> future;

    @NonNull
    LiveData<List<Coin>> coins() {
        return coins;
    }

    @NonNull
    LiveData<Boolean> isRefreshing() {
        return isRefreshing;
    }

    final void refresh() {
        isRefreshing.postValue(true);
        future = executor.submit(() -> {
            try {
                coins.postValue(new ArrayList<>(reposytory.listings("USD")));
                isRefreshing.postValue(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onCleared() {
        if (future != null) {
            future.cancel(true);
        }
    }
}
