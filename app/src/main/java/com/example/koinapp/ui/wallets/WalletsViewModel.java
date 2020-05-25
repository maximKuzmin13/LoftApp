package com.example.koinapp.ui.wallets;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.koinapp.data.CurrencyRepository;
import com.example.koinapp.data.Wallet;
import com.example.koinapp.data.WalletsRepository;
import com.example.koinapp.util.RxShedulers;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

class WalletsViewModel extends ViewModel {

    private final Observable<List<Wallet>> wallets;

    private final RxShedulers shedulers;

    @Inject
    WalletsViewModel(WalletsRepository walletsRepository,
                     CurrencyRepository currencyRepository,
                     RxShedulers shedulers) {
        wallets = currencyRepository.currency().switchMap(walletsRepository::wallets);
        this.shedulers = shedulers;
    }

    @NonNull
    Observable<List<Wallet>> wallets() {
        return wallets.observeOn(shedulers.main());
    }
}
