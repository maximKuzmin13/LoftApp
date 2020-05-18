package com.example.koinapp.ui.rates;

import androidx.lifecycle.ViewModelProvider;

import com.example.koinapp.BaseComponent;
import com.example.koinapp.util.ViewModelModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        RatesModule.class,
        ViewModelModule.class
}, dependencies = {
        BaseComponent.class
})
abstract class RatesComponent {

    abstract ViewModelProvider.Factory viewModelFactory();

    abstract RatesAdapter ratesAdapter();

}