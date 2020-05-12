package com.example.koinapp.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;

public interface CurrencyRepository {

    @NonNull
    LiveData<List<Currency>> actualCurrencies();

    @NonNull
    LiveData<Currency> currency();

    void updateCurrency(@NonNull Currency currency);
}
