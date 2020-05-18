package com.example.koinapp.ui.currency;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.koinapp.data.Currency;
import com.example.koinapp.data.CurrencyRepository;

import java.util.List;

import javax.inject.Inject;

class CurrencyViewModel extends ViewModel {

    private final CurrencyRepository repository;

    @Inject
    CurrencyViewModel(CurrencyRepository repository) {
        this.repository = repository;
    }

    @NonNull
    LiveData<List<Currency>> allCurrencies() {
        return repository.actualCurrencies();
    }

    void updateCurrency(@NonNull Currency currency) {
        repository.updateCurrency(currency);
    }
}
