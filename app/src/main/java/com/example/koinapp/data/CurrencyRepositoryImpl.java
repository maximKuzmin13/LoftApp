package com.example.koinapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.koinapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyRepositoryImpl implements CurrencyRepository {

    private final Map<String, Currency> avaliableCurrencies = new HashMap<>();

    private SharedPreferences preferences;

    private static final String KEY_CURRENCY = "currency";


    public CurrencyRepositoryImpl(@NonNull Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        avaliableCurrencies.put("USD", Currency.create("$", "USD", context.getString(R.string.usd)));
        avaliableCurrencies.put("EUR", Currency.create("E", "EUR", context.getString(R.string.eur)));
        avaliableCurrencies.put("RUB", Currency.create("р", "RUB", context.getString(R.string.rub)));
    }

    @NonNull
    @Override
    public LiveData<List<Currency>> actualCurrencies() {
        final MutableLiveData<List<Currency>> liveData = new MutableLiveData<>();
        liveData.setValue(new ArrayList<>(avaliableCurrencies.values()));
        return liveData;
    }

    @NonNull
    @Override
    public LiveData<Currency> currency() {
        return new CurrencyLiveData();
    }

    @Override
    public void updateCurrency(@NonNull Currency currency) {
        preferences.edit().putString(KEY_CURRENCY, currency.code()).apply();
    }

    private class CurrencyLiveData extends LiveData<Currency> implements SharedPreferences.OnSharedPreferenceChangeListener {


        @Override
        protected void onActive() {
            preferences.registerOnSharedPreferenceChangeListener(this);
            setValue(avaliableCurrencies.get(preferences.getString(KEY_CURRENCY, "USD")));
        }

        @Override
        protected void onInactive() {
            preferences.unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            setValue(avaliableCurrencies.get(prefs.getString(key, "USD")));
        }
    }

}
