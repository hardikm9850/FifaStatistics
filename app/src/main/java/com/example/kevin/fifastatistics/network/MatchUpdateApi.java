package com.example.kevin.fifastatistics.network;

import com.example.kevin.fifastatistics.models.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdateResponse;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface MatchUpdateApi {

    @GET("updates")
    Observable<ApiListResponse<MatchUpdate>> getUpdates();

    @GET("updates/search/findByReceiverId")
    Observable<ApiListResponse<MatchUpdate>> getUpdatesForUser(@Query("id") String userId);

    @GET("updates/{id}")
    Observable<MatchUpdate> getUpdate(@Path("id") String id);

    @POST("updates")
    Observable<Response<Void>> createUpdate(@Body MatchUpdate update);

    @POST("updates/{id}/accept")
    Observable<Response<Void>> acceptUpdate(@Path("id") String id, @Body MatchUpdateResponse response);

    @POST("updates/{id}/decline")
    Observable<Response<Void>> declineUpdate(@Path("id") String id, @Body MatchUpdateResponse response);
}
