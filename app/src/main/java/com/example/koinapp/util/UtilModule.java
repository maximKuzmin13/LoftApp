package com.example.koinapp.util;

import androidx.lifecycle.ViewModelProvider;

import com.example.koinapp.widget.Notifier;
import com.example.koinapp.widget.NotifierImpl;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class UtilModule {
    @Binds
    abstract ViewModelProvider.Factory viewModelFactory(ViewModelFactory impl);

    @Binds
    abstract ImageLoader imageLoader(PicassoImageLoader impl);

    @Binds
    abstract RxShedulers shedulers(RxShedulersImpl impl);

    @Binds
    abstract Notifier notifier(NotifierImpl impl);

}
