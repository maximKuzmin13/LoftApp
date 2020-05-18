package com.example.koinapp.util;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

public class CoinFragmentFactory extends FragmentFactory {

    private final Map<Class<?>, Provider<Fragment>> providers;

    @Inject
    CoinFragmentFactory(Map<Class<?>, Provider<Fragment>> providers) {
        this.providers = providers;
    }

    @NonNull
    @Override
    public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
        try {
            final Class<?> classKey = Class.forName(className);
            final Provider<Fragment> provider = providers.get(classKey);
            if (provider != null) {
                return provider.get();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return super.instantiate(classLoader, className);
    }
}
