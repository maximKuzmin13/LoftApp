package com.example.koinapp.ui.main;

import com.example.koinapp.BaseComponent;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        MainModule.class
}, dependencies = {
        BaseComponent.class
}
)
public abstract class MainComponent {

    abstract void inject(MainActivity activity);
}
