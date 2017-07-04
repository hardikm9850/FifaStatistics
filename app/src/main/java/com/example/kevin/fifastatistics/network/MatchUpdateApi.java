package com.example.kevin.fifastatistics.network;

import com.example.kevin.fifastatistics.models.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdateResponse;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface MatchUpdateApi {

    @GET("updates")
    Observable<ApiListResponse<MatchUpdate>> getUpdates();

    @GET("updates/{id}")
    Observable<MatchUpdate> getUpdate(@Path("id") String id);

    @POST("updates")
    Observable<Response<?>> createUpdate(@Body MatchUpdate update);

    @POST("updates/accept")
    Observable<Response<?>> acceptUpdate(@Body MatchUpdateResponse response);

    @POST("updates/decline")
    Observable<Response<?>> declineUpdate(@Body MatchUpdateResponse response);
}
