package com.example.kevin.fifastatistics.network;

import com.example.kevin.fifastatistics.models.apiresponses.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

public interface SeriesApi {

    @GET("series")
    Observable<ApiListResponse<Series>> getSeries();

    @POST("series")
    Observable<Response<Void>> createSeries(@Body Series series);

    @GET
    Observable<Series> lookupSeries(@Url String url);
}
