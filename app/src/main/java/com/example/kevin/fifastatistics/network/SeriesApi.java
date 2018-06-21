package com.example.kevin.fifastatistics.network;

import com.example.kevin.fifastatistics.models.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;
import com.example.kevin.fifastatistics.models.databasemodels.match.SeriesProjection;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

public interface SeriesApi {

    @GET("series/search/findAllByOrderByDateDesc")
    Observable<ApiListResponse<SeriesProjection>> getSeries();

    @GET("series/{id}")
    Observable<Series> findSeries(@Path("id") String id);

    @POST("series")
    Observable<Response<Void>> createSeries(@Body Series series);

    @GET("series/filter")
    Observable<ApiListResponse<SeriesProjection>> filterSeries(@QueryMap Map<String, String> filters);

    @GET
    Observable<Series> lookupSeries(@Url String url);

    @GET
    Observable<ApiListResponse<SeriesProjection>> getNextSeries(@Url String url);
}
