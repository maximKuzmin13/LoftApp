package com.example.koinapp.ui.converter;

import androidx.lifecycle.ViewModelProvider;

import com.example.koinapp.BaseComponent;
import com.example.koinapp.util.ViewModelModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ConverterModule.class,
        ViewModelModule.class
}, dependencies = {
        BaseComponent.class
})
abstract class ConverterComponent {

    abstract ViewModelProvider.Factory viewModelFactory();

    abstract ConverterDialogAdapter converterDialogAdapter();

}
