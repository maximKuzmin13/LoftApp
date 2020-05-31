package com.example.koinapp.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Observable;

public interface CurrencyRepository {

    @NonNull
    LiveData<List<Currency>> actualCurrencies();

    @NonNull
    Observable<Currency> currency();

    void updateCurrency(@NonNull Currency currency);
}
