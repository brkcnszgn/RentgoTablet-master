package com.creatifsoftware.rentgoservice.di;

import com.creatifsoftware.rentgoservice.view.activity.LoginActivity;
import com.creatifsoftware.rentgoservice.view.activity.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = FragmentBuildersModule.class)
    abstract LoginActivity contributeLoginActivity();
}
