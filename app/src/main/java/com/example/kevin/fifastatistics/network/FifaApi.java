package com.example.kevin.fifastatistics.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Adapter class to interact with the UserApi.
 */
public class FifaApi {

//    private static final String FIFA_API_ENDPOINT = "https://fifastatisticsapi.azurewebsites.net/";
    private static final String FIFA_API_ENDPOINT = "http://192.168.1.110:8080/";
    private static final String NOTIFICATIONS_API_ENDPOINT = "https://gcm-http.googleapis.com/gcm/";
    private static final int CONNECT_TIMEOUT_DURATION = 30;

    private static UserApi userApi;
    private static MatchApi matchApi;
    private static SeriesApi seriesApi;
    private static LeagueApi leagueApi;
    private static NotificationsApi notificationsApi;

    public static NotificationsApi getNotificationsApi() {
        if (notificationsApi == null) {
            notificationsApi = (initializeApi(NotificationsApi.class, NOTIFICATIONS_API_ENDPOINT));
        }
        return notificationsApi;
    }

    public static UserApi getUserApi() {
        if (userApi == null) {
            userApi = initializeApi(UserApi.class, FIFA_API_ENDPOINT);
        }
        return userApi;
    }

    public static MatchApi getMatchApi() {
        if (matchApi == null) {
            matchApi = initializeApi(MatchApi.class, FIFA_API_ENDPOINT);
        }
        return matchApi;
    }

    public static SeriesApi getSeriesApi() {
        if (seriesApi == null) {
            seriesApi = initializeApi(SeriesApi.class, FIFA_API_ENDPOINT);
        }
        return seriesApi;
    }

    public static LeagueApi getLeagueApi() {
        if (leagueApi == null) {
            leagueApi = initializeApi(LeagueApi.class, FIFA_API_ENDPOINT);
        }
        return leagueApi;
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
