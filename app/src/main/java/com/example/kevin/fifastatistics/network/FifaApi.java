package com.example.kevin.fifastatistics.network;

import com.example.kevin.fifastatistics.BuildConfig;
import com.example.kevin.fifastatistics.managers.preferences.PrefsManager;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.Footballer;
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
    private static MatchUpdateApi updateApi;
    private static CurrentSeriesApi currentSeriesApi;
    private static FootballerApi footballerApi;

    public static UserApi getUserApi() {
        if (userApi == null) {
            userApi = initializeApi(UserApi.class);
        }
        return userApi;
    }

    public static MatchApi getMatchApi() {
        if (matchApi == null) {
            matchApi = initializeApi(MatchApi.class);
        }
        return matchApi;
    }

    public static SeriesApi getSeriesApi() {
        if (seriesApi == null) {
            seriesApi = initializeApi(SeriesApi.class);
        }
        return seriesApi;
    }

    public static LeagueApi getLeagueApi() {
        if (leagueApi == null) {
            leagueApi = initializeApi(LeagueApi.class);
        }
        return leagueApi;
    }

    public static MatchUpdateApi getUpdateApi() {
        if (updateApi == null) {
            updateApi = initializeApi(MatchUpdateApi.class);
        }
        return updateApi;
    }

    public static CurrentSeriesApi getCurrentSeriesApi() {
        if (currentSeriesApi == null) {
            currentSeriesApi = initializeApi(CurrentSeriesApi.class);
        }
        return currentSeriesApi;
    }

    public static FootballerApi getFootballerApi() {
        if (footballerApi == null) {
            footballerApi = initializeApi(FootballerApi.class);
        }
        return footballerApi;
    }

    private static <T> T initializeApi(Class<T> api) {
        HttpLoggingInterceptor loggingInterceptor = initializeLoggingInterceptor();
        OkHttpClient httpClient = initializeHttpClient(loggingInterceptor);
        Retrofit retrofit = initializeRetrofitObject(httpClient, FIFA_API_ENDPOINT);
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
        User user = PrefsManager.getUser();
        return chain -> {
            Request request = chain.request();
            request = request.newBuilder()
                    .addHeader("Authorization", user == null ? "" : user.getId())
                    .build();
            return chain.proceed(request);
        };
    }
}
