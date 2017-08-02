package com.example.kevin.fifastatistics.network;

import com.example.kevin.fifastatistics.models.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.match.CurrentSeries;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kevin on 2017-08-01.
 */

public interface CurrentSeriesApi {

    @GET("currentSeries/search/findByCreatorId")
    Observable<ApiListResponse<CurrentSeries>> getCurrentSeries(@Query("creatorId") String id);

    @POST("currentSeries")
    Observable<Response<Void>> upload(@Body CurrentSeries series);

    @PUT("currentSeries/{id}")
    Observable<Response<Void>> update(@Path("id") String id, @Body CurrentSeries series);

    @DELETE("currentSeries/{id}")
    Observable<Response<Void>> delete(@Path("id") String id);
}
