package com.example.koinapp.ui.currency;

import androidx.lifecycle.ViewModelProvider;

import com.example.koinapp.BaseComponent;
import com.example.koinapp.util.ViewModelModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        CurrencyModule.class,
        ViewModelModule.class
}, dependencies = {
        BaseComponent.class
})
abstract class CurrencyComponent {

    abstract ViewModelProvider.Factory viewModelFactory();

}