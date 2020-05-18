package com.example.koinapp.ui.rates;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.koinapp.data.Coin;
import com.example.koinapp.data.CoinReposytory;
import com.example.koinapp.data.CurrencyRepository;
import com.example.koinapp.data.SortBy;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

public class RatesViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isRefreshing = new MutableLiveData<>();
    private final MutableLiveData<AtomicBoolean> forceRefresh = new MutableLiveData<>(new AtomicBoolean(true));
    private final LiveData<List<Coin>> coins;
    private final MutableLiveData<SortBy> sortBy = new MutableLiveData<>(SortBy.RANK);
    private int sortingIndex = 1;

    @Inject
    RatesViewModel(CoinReposytory coinReposytory, CurrencyRepository currencyRepository) {

        final LiveData<CoinReposytory.Query> query = Transformations.switchMap(forceRefresh, (r) -> {
            return Transformations.switchMap(currencyRepository.currency(), (c) -> {
                r.set(true);
                isRefreshing.postValue(true);
                return Transformations.map(sortBy, (s) -> {
                    return CoinReposytory.Query.builder()
                            .currency(c.code())
                            .forceUpdate(r.getAndSet(false))
                            .sortBy(s)
                            .build();
                });
            });
        });

        final LiveData<List<Coin>> coins = Transformations.switchMap(query, coinReposytory::listings);
        this.coins = Transformations.map(coins, (c) -> {
            isRefreshing.postValue(false);
            return c;
        });
    }

    @NonNull
    LiveData<List<Coin>> coins() {
        return coins;
    }

    @NonNull
    LiveData<Boolean> isRefreshing() {
        return isRefreshing;
    }

    final void refresh() {
        forceRefresh.postValue(new AtomicBoolean(true));
    }

    void switchSortingOrder() {
        sortBy.postValue(SortBy.values()[sortingIndex++ % SortBy.values().length]);
    }
}
