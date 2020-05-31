package com.example.koinapp.ui.wallets;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.koinapp.data.Coin;
import com.example.koinapp.data.CurrencyRepository;
import com.example.koinapp.data.Transaction;
import com.example.koinapp.data.Wallet;
import com.example.koinapp.data.WalletsRepository;
import com.example.koinapp.util.RxShedulers;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import timber.log.Timber;

class WalletsViewModel extends ViewModel {
    private final Subject<Integer> walletPosition = BehaviorSubject.createDefault(0);

    private final Observable<List<Wallet>> wallets;

    private final Observable<List<Transaction>> transactions;

    private final WalletsRepository walletsReposytory;

    private final CurrencyRepository currencyRepository;

    private final RxShedulers shedulers;

    @Inject
    WalletsViewModel(WalletsRepository walletsReposytory, CurrencyRepository currencyRepository, RxShedulers shedulers) {
        this.walletsReposytory = walletsReposytory;
        this.currencyRepository = currencyRepository;
        this.shedulers = shedulers;

        wallets = currencyRepository.currency()
                .switchMap(walletsReposytory::wallets)
                .replay(1)
                .autoConnect();

        transactions = wallets
                .filter((w) -> !w.isEmpty())
                .switchMap((wallets) -> walletPosition
                        .map((pos) -> Math.min(pos, wallets.size() - 1))
                        .map(wallets::get)
                )
                .switchMap(walletsReposytory::transactions)
                .doOnNext((t) -> Timber.d("%s", t))
                .replay(1)
                .autoConnect();
    }

    @NonNull
    Observable<List<Wallet>> wallets() {
        return wallets.observeOn(shedulers.main());
    }

    @NonNull
    Observable<List<Transaction>> transactions() {
        return transactions.observeOn(shedulers.main());
    }

    @NonNull
    Completable addWallet() {
        return wallets
                .firstOrError()
                .flatMap((list) -> Observable
                        .fromIterable(list)
                        .map(Wallet::coin)
                        .map(Coin::id)
                        .toList()
                        .doOnSuccess(u -> Timber.d("%s", u))
                )
                .flatMapCompletable((ids) -> currencyRepository
                        .currency()
                        .doOnNext(u -> Timber.d("%s", u))
                        .flatMapCompletable((c) -> walletsReposytory.addWallet(c, ids))
                )
                .observeOn(shedulers.main());
    }

    void changeWallet(int position) {
        walletPosition.onNext(position);
    }

}
