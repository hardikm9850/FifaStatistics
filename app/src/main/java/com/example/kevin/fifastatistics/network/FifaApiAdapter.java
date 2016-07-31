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
    private static final int CONNECT_TIMEOUT_DURATION = 4;

    private static FifaApi api;

    public static FifaApi getService()
    {
        if (api == null) {
            initializeFifaApi();
        }
        return api;
    }

    private static void initializeFifaApi()
    {
        HttpLoggingInterceptor loggingInterceptor = initializeLoggingInterceptor();
        OkHttpClient httpClient = initializeHttpClient(loggingInterceptor);
        Retrofit retrofit = initializeRetrofitObject(httpClient);
        api = retrofit.create(FifaApi.class);
    }

    private static HttpLoggingInterceptor initializeLoggingInterceptor()
    {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    private static OkHttpClient initializeHttpClient(HttpLoggingInterceptor interceptor)
    {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(CONNECT_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(CONNECT_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .build();
    }

    private static Retrofit initializeRetrofitObject(OkHttpClient httpClient)
    {
        return new Retrofit.Builder()
                .baseUrl(FIFA_API_ENDPOINT)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
}
