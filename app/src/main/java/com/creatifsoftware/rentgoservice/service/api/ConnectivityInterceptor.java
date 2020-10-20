package com.creatifsoftware.rentgoservice.service.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.creatifsoftware.rentgoservice.RentgoServiceApp;
import com.creatifsoftware.rentgoservice.utils.NetworkUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by kerembalaban on 2019-09-13 at 12:49.
 */
public class ConnectivityInterceptor implements Interceptor {

    private final Context mContext;

    public ConnectivityInterceptor() {
        mContext = RentgoServiceApp.getContext();
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!NetworkUtils.instance.isOnline(mContext)) {
            throw new NoConnectivityException();
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }

}

class NoConnectivityException extends IOException {
    @Override
    public String getMessage() {
        return "Lütfen internet bağlantınızı kontrol edin";
    }
}