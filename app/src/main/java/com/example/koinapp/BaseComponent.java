package com.example.koinapp;

import android.content.Context;

import com.example.koinapp.data.CoinReposytory;
import com.example.koinapp.data.CurrencyRepository;
import com.example.koinapp.util.ImageLoader;

public interface BaseComponent {
    Context context();

    CoinReposytory coinReposytory();

    CurrencyRepository currencyRepository();

    ImageLoader imageLoader();
}
