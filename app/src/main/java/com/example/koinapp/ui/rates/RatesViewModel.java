package com.example.koinapp.ui.rates;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.koinapp.data.Coin;
import com.example.koinapp.data.CoinReposytory;
import com.example.koinapp.data.CurrencyRepository;
import com.example.koinapp.data.SortBy;
import com.example.koinapp.util.RxShedulers;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

class RatesViewModel extends ViewModel {
    private final Subject<Boolean> isRefreshing = BehaviorSubject.create();
    private final Subject<Class<?>> pullToRefresh = BehaviorSubject.createDefault(Void.TYPE);
    private final Observable<List<Coin>> coins;
    private final Subject<SortBy> sortBy = BehaviorSubject.createDefault(SortBy.RANK);
    private final AtomicBoolean forceUpdate = new AtomicBoolean();
    private int sortingIndex = 1;
    private final RxShedulers shedulers;
    private final Subject<Throwable> error = PublishSubject.create();
    private final Subject<Class<?>> onRetry = PublishSubject.create();

    @Inject
    RatesViewModel(CoinReposytory coinReposytory, CurrencyRepository currencyRepository, RxShedulers shedulers) {
        this.shedulers = shedulers;
        this.coins =
                pullToRefresh.map((ptr) -> CoinReposytory.Query.builder())
                        .switchMap((qb) -> currencyRepository.currency().map((c) -> qb.currency(c.code()))
                        )
                        .doOnNext((qb) -> forceUpdate.set(true))
                        .doOnNext((qb) -> isRefreshing.onNext(true))
                        .switchMap((qb) -> sortBy.map(qb::sortBy))
                        .map((qb) -> qb.forceUpdate(forceUpdate.getAndSet(false)))
                        .map(CoinReposytory.Query.Builder::build)
                        .switchMap((q) -> coinReposytory.listings(q)
                                .doOnError(error::onNext)
                                .onErrorReturnItem(Collections.emptyList()))
                        .doOnEach((ntf) -> isRefreshing.onNext(false))
                        .replay()
                        .autoConnect();

    }

    @NonNull
    Observable<List<Coin>> coins() {
        return coins.observeOn(shedulers.main());
    }

    @NonNull
    Observable<Boolean> isRefreshing() {
        return isRefreshing.observeOn(shedulers.main());
    }

    final void refresh() {
        pullToRefresh.onNext(Void.TYPE);
    }

    @NonNull
    Observable<Throwable> onError() {
        return error.observeOn(shedulers.main());
    }

    void switchSortingOrder() {
        sortBy.onNext(SortBy.values()[sortingIndex++ % SortBy.values().length]);
    }

    void retry() {
        onRetry.onNext(Void.class);
    }
}
