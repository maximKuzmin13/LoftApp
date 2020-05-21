package com.example.koinapp.util;

import androidx.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class UtilModule {
    @Binds
    abstract ViewModelProvider.Factory viewModelFactory(ViewModelFactory impl);

    @Binds
    abstract ImageLoader imageLoader(PicassoImageLoader impl);
}
