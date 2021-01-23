package com.creatifsoftware.filonova;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.creatifsoftware.filonova.di.AppInjector;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * Created by kerembalaban on 22.07.2018 at 16:28.
 */
public final class FilonovaServiceApp extends Application implements HasActivityInjector {

    public static FilonovaServiceApp instance;
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    public FilonovaServiceApp() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppInjector.init(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
