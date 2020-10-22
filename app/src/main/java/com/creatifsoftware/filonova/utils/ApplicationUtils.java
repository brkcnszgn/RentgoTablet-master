package com.creatifsoftware.filonova.utils;

import android.content.Context;

import com.creatifsoftware.filonova.BuildConfig;

/**
 * Created by kerembalaban on 11.04.2019 at 01:39.
 */
public class ApplicationUtils {
    public static final ApplicationUtils instance = new ApplicationUtils();

    public boolean getIsProductRelease() {
        return BuildConfig.IS_PROD;
    }

    public String getApplicationVersion(Context context) {
        return BuildConfig.VERSION_NAME;
    }

//    public boolean getLiveSwitchIsChecked(Context context){
//        //Context context1 = FilonovaServiceApp.getContext();
//        if (SharedPrefUtils.instance.getSavedObjectFromPreference(context,"live_switch",Boolean.class) == null){
//            return false;
//        }
//        return SharedPrefUtils.instance.getSavedObjectFromPreference(context,"live_switch",Boolean.class);
//    }
}