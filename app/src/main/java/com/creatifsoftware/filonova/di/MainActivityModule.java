package com.creatifsoftware.filonova.di;

import com.creatifsoftware.filonova.view.activity.LoginActivity;
import com.creatifsoftware.filonova.view.activity.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract LoginActivity contributeLoginActivity();
}
