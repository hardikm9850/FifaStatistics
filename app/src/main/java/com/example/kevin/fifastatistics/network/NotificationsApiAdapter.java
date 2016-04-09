package com.example.kevin.fifastatistics.network;

import com.example.kevin.fifastatistics.models.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Kevin on 4/8/2016.
 */
public class NotificationsApiAdapter
{
    private static NotificationsApi api;
    private static HttpLoggingInterceptor loggingInterceptor;
    private static OkHttpClient httpClient;
    private static Retrofit retrofit;

    public static NotificationsApi getService()
    {
        if (api == null) {
            initializeNotificationsApi();
        }
        return api;
    }

    private static void initializeNotificationsApi()
    {
        initializeLoggingInterceptor();
        initializeHttpClient();
        initializeRetrofitObject();
        api = retrofit.create(NotificationsApi.class);
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
                .build();
    }

    private static void initializeRetrofitObject()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.NOTIFICATIONS_API_ENDPOINT)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
}
