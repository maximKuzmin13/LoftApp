package com.example.koinapp;


import android.app.Application;

import com.example.koinapp.data.DataModule;
import com.example.koinapp.util.UtilModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(
        modules = {
                DaggerModule.class,
                DataModule.class,
                UtilModule.class
        }
)
abstract class DaggerComponent implements BaseComponent {
    @Component.Builder
    static abstract class Builder {
        @BindsInstance
        abstract Builder application(Application application);

        abstract DaggerComponent build();
    }
}
