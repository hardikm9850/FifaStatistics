package com.example.kevin.fifastatistics.network;

import com.example.kevin.fifastatistics.BuildConfig;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Adapter class to interact with the UserApi.
 */
public class FifaApi {

    private static final String FIFA_API_ENDPOINT = BuildConfig.SERVER_URL;
    private static final int CONNECT_TIMEOUT_DURATION = 60;

    private static UserApi userApi;
    private static MatchApi matchApi;
    private static SeriesApi seriesApi;
    private static LeagueApi leagueApi;

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
                .addInterceptor(getAuthorizationInterceptor())
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

    private static Interceptor getAuthorizationInterceptor() {
        User user = SharedPreferencesManager.getUser();
        return chain -> {
            Request request = chain.request();
            request = request.newBuilder()
                    .addHeader("Authorization", user == null ? "" : user.getId())
                    .build();
            return chain.proceed(request);
        };
    }
}
