package com.example.koinapp.ui.converter;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.koinapp.data.Coin;
import com.example.koinapp.data.CoinReposytory;
import com.example.koinapp.data.CurrencyRepository;
import com.example.koinapp.util.RxShedulers;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

class ConverterViewModel extends ViewModel {

    private final Subject<Coin> fromCoin = BehaviorSubject.create();

    private final Subject<Coin> toCoin = BehaviorSubject.create();

    private final Subject<String> fromValue = BehaviorSubject.create();

    private final Subject<String> toValue = BehaviorSubject.create();

    private final Observable<List<Coin>> topCoins;

    private final Observable<Double> factor;

    private final RxShedulers shedulers;

    @Inject
    ConverterViewModel(CoinReposytory coinReposytory, CurrencyRepository currencyRepository, RxShedulers shedulers) {
        this.shedulers = shedulers;

        topCoins = currencyRepository.currency()
                .switchMap(coinReposytory::topCoins)
                .doOnNext((coins) -> fromCoin.onNext(coins.get(0)))
                .doOnNext((coins) -> toCoin.onNext(coins.get(1)))
                .replay(1)
                .autoConnect();

        factor = fromCoin
                .flatMap((fc) -> toCoin
                        .map((tc) -> fc.price() / tc.price())
                )
                .replay(1)
                .autoConnect();
    }

    @NonNull
    Observable<List<Coin>> topCoins() {
        return topCoins.observeOn(shedulers.main());
    }

    @NonNull
    Observable<Coin> fromCoin() {
        return fromCoin.observeOn(shedulers.main());
    }

    @NonNull
    Observable<Coin> toCoin() {
        return toCoin.observeOn(shedulers.main());
    }

    @NonNull
    Observable<String> fromValue() {
        return fromValue.observeOn(shedulers.main());
    }

    @NonNull
    Observable<String> toValue() {
        return fromValue
                .observeOn(shedulers.cnp())
                .map((s) -> s.isEmpty() ? "0.0" : s)
                .map(Double::parseDouble)
                .flatMap((value) -> factor.map((f) -> value * f))
                .map(v -> String.format(Locale.US, "%.2f", v))
                .map((v) -> "0.0".equals(v) ? "" : v)
                .observeOn(shedulers.main());
    }

    void fromCoin(Coin coin) {
        fromCoin.onNext(coin);
    }

    void toCoin(Coin coin) {
        toCoin.onNext(coin);
    }

    void fromValue(CharSequence text) {
        fromValue.onNext(text.toString());
    }

    void toValue(CharSequence text) {
        toValue.onNext(text.toString());
    }

}
