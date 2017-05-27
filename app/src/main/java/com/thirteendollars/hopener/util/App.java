package com.thirteendollars.hopener.util;

import android.app.Application;
import android.telecom.ConnectionService;

import com.thirteendollars.hopener.BuildConfig;
import com.thirteendollars.hopener.model.ControlService;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Damian Nowakowski on 03/05/2017.
 * mail: thirteendollars.com@gmail.com
 */

public class App extends Application {

    private Retrofit mRetrofit;
    private ControlService mControlService;

    @Override
    public void onCreate() {
        super.onCreate();
        initRetrofit();

    }

    private void initRetrofit() {

        mRetrofit= new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.ENDPOINT)
                .client( SslHelper.getCustomHttpClient(getApplicationContext()) )
                .build();
        mControlService = mRetrofit.create(ControlService.class);
    }

    public ControlService getControlService() {
        return mControlService;
    }


}
