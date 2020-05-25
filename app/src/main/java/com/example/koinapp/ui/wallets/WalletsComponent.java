package com.example.koinapp.ui.wallets;

import androidx.lifecycle.ViewModelProvider;

import com.example.koinapp.BaseComponent;
import com.example.koinapp.util.ViewModelModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        WalletsModule.class,
        ViewModelModule.class
}, dependencies = {
        BaseComponent.class
})
abstract class WalletsComponent {

    abstract ViewModelProvider.Factory viewModelFactory();

    abstract WalletAdapter walletAdapter();

//    abstract TransactionsAdapter transactionsAdapter();

}