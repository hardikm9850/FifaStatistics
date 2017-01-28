package com.example.kevin.fifastatistics.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import rx.Observer;

/**
 * Adapter class to interact with the FifaApi.
 */
public class ApiAdapter {

    public static final Observer<Void> EMPTY_OBSERVER = new Observer<Void>() {
        @Override
        public void onCompleted() {}
        @Override
        public void onError(Throwable e) {}
        @Override
        public void onNext(Void aVoid) {}
    };

    private static final String FIFA_API_ENDPOINT = "https://fifastatisticsapi.azurewebsites.net/";
    private static final String NOTIFICATIONS_API_ENDPOINT = "https://gcm-http.googleapis.com/gcm/";
    private static final int CONNECT_TIMEOUT_DURATION = 4;

    private static FifaApi fifaApi;
    private static NotificationsApi notificationsApi;

    public static NotificationsApi getNotificationsApi() {
        if (notificationsApi == null) {
            notificationsApi = (initializeApi(NotificationsApi.class, NOTIFICATIONS_API_ENDPOINT));
        }
        return notificationsApi;
    }

    public static FifaApi getFifaApi() {
        if (fifaApi == null) {
            fifaApi = initializeApi(FifaApi.class, FIFA_API_ENDPOINT);
        }
        return fifaApi;
    }

    private static <T> T initializeApi(Class<T> api, String baseUrl) {
        HttpLoggingInterceptor loggingInterceptor = initializeLoggingInterceptor();
        OkHttpClient httpClient = initializeHttpClient(loggingInterceptor);
        Retrofit retrofit = initializeRetrofitObject(httpClient, baseUrl);
        return retrofit.create(api);
    }

    private static HttpLoggingInterceptor initializeLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    private static OkHttpClient initializeHttpClient(HttpLoggingInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(CONNECT_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .readTimeout(CONNECT_TIMEOUT_DURATION, TimeUnit.SECONDS)
                .build();
    }

    private static Retrofit initializeRetrofitObject(OkHttpClient httpClient, String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }
}
