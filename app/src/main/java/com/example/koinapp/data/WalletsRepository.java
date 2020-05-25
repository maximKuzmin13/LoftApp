package com.example.koinapp.data;


import androidx.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;

public interface WalletsRepository {
    @NonNull
    Observable<List<Wallet>> wallets(@NonNull Currency currency);

    @NonNull
    Observable<List<Transaction>> transactions(@NonNull Wallet wallet);
}
