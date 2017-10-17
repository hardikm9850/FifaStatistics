package com.example.kevin.fifastatistics.network;

import com.example.kevin.fifastatistics.models.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.Footballer;

import retrofit2.http.GET;
import rx.Observable;

public interface FootballerApi {

    int CACHE_SIZE = 500;

    @GET("players?size=" + CACHE_SIZE)
    Observable<ApiListResponse<Footballer>> getFootballers();
}
