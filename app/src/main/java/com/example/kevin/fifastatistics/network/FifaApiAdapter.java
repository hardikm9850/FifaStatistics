package com.example.kevin.fifastatistics.network;

import static com.example.kevin.fifastatistics.models.Constants.*;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Adapter class to interact with the FifaApi.
 */
public class FifaApiAdapter
{
    private static final int CONNECT_TIMEOUT_DURATION = 45;

    private static FifaApi api;
    private static HttpLoggingInterceptor loggingInterceptor;
    private static OkHttpClient httpClient;
    private static Retrofit retrofit;

    public static FifaApi getService()
    {
        if (api == null) {
            initializeFifaApi();
        }
        return api;
    }

    private static void initializeFifaApi()
    {
        initializeLoggingInterceptor();
        initializeHttpClient();
        initializeRetrofitObject();
        api = retrofit.create(FifaApi.class);
    }

    private static void initializeLoggingInterceptor()
    {
        loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    private static void initializeHttpClient()
    {
        httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(CONNECT_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(CONNECT_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .build();
    }

    private static void initializeRetrofitObject()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(FIFA_API_ENDPOINT)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
}
