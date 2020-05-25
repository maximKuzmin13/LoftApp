package com.example.koinapp;

import android.content.Context;

import com.example.koinapp.data.CoinReposytory;
import com.example.koinapp.data.CurrencyRepository;
import com.example.koinapp.data.WalletsRepository;
import com.example.koinapp.util.ImageLoader;
import com.example.koinapp.util.RxShedulers;
import com.example.koinapp.widget.Notifier;

public interface BaseComponent {
    Context context();

    CoinReposytory coinReposytory();

    CurrencyRepository currencyRepository();

    ImageLoader imageLoader();

    RxShedulers schedulers();

    WalletsRepository walletsRepository();

    Notifier notifier();
}
