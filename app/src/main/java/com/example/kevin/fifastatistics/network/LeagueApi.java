package com.example.kevin.fifastatistics.network;

import com.example.kevin.fifastatistics.models.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.league.League;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

public interface LeagueApi {

    @GET("leagues")
    Observable<ApiListResponse<League>> getLeagues();

    @GET
    Observable<League> getLeague(@Url String url);

    @GET
    Observable<ApiListResponse<Team>> getTeams(@Url String url);

    @GET("teams/{id}")
    Observable<Team> getTeam(@Path("id") String id);
}
