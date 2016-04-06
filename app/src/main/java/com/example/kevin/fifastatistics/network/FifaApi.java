package com.example.kevin.fifastatistics.network;

import com.example.kevin.fifastatistics.models.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by Kevin on 4/3/2016.
 */
public class FifaApi
{
    private static FifaApiInterface apiInterface;

    public static FifaApiInterface getService()
    {
        if (apiInterface == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.API_ENDPOINT)
                    .client(client)
                    .addConverterFactory(JacksonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            apiInterface = retrofit.create(FifaApiInterface.class);
        }

        return apiInterface;
    }

    private FifaApi() {

    }
}
