package com.example.kevin.fifastatistics.network;

import com.example.kevin.fifastatistics.models.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchProjection;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

public interface MatchApi {

    @GET("matches/search/findAllByOrderByDateDesc")
    Observable<ApiListResponse<MatchProjection>> getMatches();

    @GET("matches/search/findAllByOrderByDateDesc")
    Observable<ApiListResponse<MatchProjection>> getMatchesOnPage(@Query("page") int page);

    @GET("matches/filter")
    Observable<ApiListResponse<MatchProjection>> filterMatches(@QueryMap Map<String, String> filters);

    @GET("matches/{id}?projection=inlineTeams")
    Observable<Match> getMatch(@Path("id") String id);

    @POST("matches")
    Observable<Response<Void>> createMatch(@Body Match match);

    @GET
    Observable<Match> lookupMatch(@Url String url);

    @GET
    Observable<ApiListResponse<MatchProjection>> getNextMatches(@Url String url);
}
