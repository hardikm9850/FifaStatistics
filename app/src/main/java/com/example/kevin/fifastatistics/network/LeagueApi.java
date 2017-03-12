package com.example.kevin.fifastatistics.network;

import com.example.kevin.fifastatistics.models.apiresponses.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.league.League;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

public interface LeagueApi {

    @GET("leagues")
    Observable<ApiListResponse<League>> getLeagues();

    @GET
    Observable<League> getLeague(@Url String url);

    @GET
    Observable<ApiListResponse<Team>> getTeams(@Url String url);
}
