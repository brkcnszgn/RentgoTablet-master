package com.creatifsoftware.rentgoservice;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.creatifsoftware.rentgoservice.di.AppInjector;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * Created by kerembalaban on 22.07.2018 at 16:28.
 */
public final class RentgoServiceApp extends Application implements HasActivityInjector {

    public static RentgoServiceApp instance;
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    public RentgoServiceApp() {
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
}
