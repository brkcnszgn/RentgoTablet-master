package com.creatifsoftware.rentgoservice.di;

import androidx.lifecycle.ViewModelProvider;

import com.creatifsoftware.rentgoservice.BuildConfig;
import com.creatifsoftware.rentgoservice.service.api.BasicAuthInterceptor;
import com.creatifsoftware.rentgoservice.service.api.ConnectivityInterceptor;
import com.creatifsoftware.rentgoservice.service.api.JsonApi;
import com.creatifsoftware.rentgoservice.viewmodel.RentgoViewModelFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(subcomponents = ViewModelSubComponent.class)
class AppModule {
    private static final int CONNECT_TIME_OUT = 120;
    private static final int WRITE_TIME_OUT = 120;
    private static final int READ_TIME_OUT = 120;

    @Singleton
    @Provides
    JsonApi provideApiService() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor((new BasicAuthInterceptor(BuildConfig.API_USERNAME, BuildConfig.API_PASSWORD)))
                .addInterceptor(new ConnectivityInterceptor())
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .cache(null)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVICE_API_URL)
                //.baseUrl(ApplicationUtils.instance.getLiveSwitchIsChecked(context) ? ConnectionUtils.instance.getLiveCrmApiConnectionUrl() : ConnectionUtils.instance.getDevCrmApiConnectionUrl())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(JsonApi.class);
    }

    @Singleton
    @Provides
    ViewModelProvider.Factory provideViewModelFactory(
            ViewModelSubComponent.Builder viewModelSubComponent) {

        return new RentgoViewModelFactory(viewModelSubComponent.build());
    }
}
