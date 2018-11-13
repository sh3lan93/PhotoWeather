package com.shalan.photoweather.network;

import com.shalan.photoweather.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClientConfig {

    private static RestClientConfig INSTANCE;
    private static String baseUrl = BuildConfig.BASE_URL;

    private Retrofit.Builder builder;
    private Retrofit retrofit;
    private OkHttpClient.Builder clientBuilder;
    private HttpLoggingInterceptor interceptor;

    private RestClientConfig(){
        builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create());
        clientBuilder = new OkHttpClient.Builder();
        interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(interceptor);
        builder.client(clientBuilder.build());
        retrofit = builder.build();
    }

    public static RestClientConfig getInstance(){
        if (INSTANCE == null)
            INSTANCE = new RestClientConfig();
        return INSTANCE;
    }

    public <S> S createService(Class<S> s){
        return retrofit.create(s);
    }
}
