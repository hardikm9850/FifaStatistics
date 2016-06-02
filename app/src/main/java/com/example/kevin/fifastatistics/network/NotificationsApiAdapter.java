package com.example.kevin.fifastatistics.network;

import com.example.kevin.fifastatistics.models.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Adapter class to interact with the GCM Notifications API.
 */
public class NotificationsApiAdapter
{
    private static NotificationsApi api;

    public static NotificationsApi getService()
    {
        if (api == null) {
            initializeNotificationsApi();
        }
        return api;
    }

    private static void initializeNotificationsApi()
    {
        HttpLoggingInterceptor loggingInterceptor = initializeLoggingInterceptor();
        OkHttpClient httpClient = initializeHttpClient(loggingInterceptor);
        Retrofit retrofit = initializeRetrofitObject(httpClient);
        api = retrofit.create(NotificationsApi.class);
    }

    private static HttpLoggingInterceptor initializeLoggingInterceptor()
    {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    private static OkHttpClient initializeHttpClient(HttpLoggingInterceptor loggingInterceptor)
    {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    private static Retrofit initializeRetrofitObject(OkHttpClient httpClient)
    {
        return new Retrofit.Builder()
                .baseUrl(Constants.NOTIFICATIONS_API_ENDPOINT)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
}
