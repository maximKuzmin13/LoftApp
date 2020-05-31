package com.example.koinapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.example.koinapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class CurrencyRepositoryImpl implements CurrencyRepository {

    private final Map<String, Currency> avaliableCurrencies = new HashMap<>();

    private SharedPreferences preferences;

    private static final String KEY_CURRENCY = "currency";

    @Inject
    CurrencyRepositoryImpl(@NonNull Context context) {
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
    public Observable<Currency> currency() {
        return Observable.create(emitter -> {
            SharedPreferences.OnSharedPreferenceChangeListener listener = (preferences, key) -> {
                if (!emitter.isDisposed() && KEY_CURRENCY.equals(key)) {
                    emitter.onNext(avaliableCurrencies.get(preferences.getString(key, "USD")));
                }
            };
            preferences.registerOnSharedPreferenceChangeListener(listener);
            emitter.setCancellable(() -> preferences.unregisterOnSharedPreferenceChangeListener(listener));
            emitter.onNext(avaliableCurrencies.get(preferences.getString(KEY_CURRENCY, "USD")));
        });
    }

    @Override
    public void updateCurrency(@NonNull Currency currency) {
        preferences.edit().putString(KEY_CURRENCY, currency.code()).apply();
    }

}
