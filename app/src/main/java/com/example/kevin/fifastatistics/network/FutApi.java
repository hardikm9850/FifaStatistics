package com.example.kevin.fifastatistics.network;

import com.example.kevin.fifastatistics.models.FutApiResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public class FutApi {

    private static final String FUT_ID = "futId";
    private static final String PAGE = "pageNum";
    private static final String CLUB_SEARCH =
            "{\"club\":" + FUT_ID + ",\"page\":" + PAGE + ",\"quality\":\"bronze,silver," +
            "gold,rare_bronze,rare_silver,rare_gold\"}";

    FutApiInternal futApi;

    public Observable<FutApiResponse> getPlayersForClub(int teamFutId, int page) {
        String query = CLUB_SEARCH.replace(FUT_ID, String.valueOf(teamFutId));
        query = query.replace(PAGE, String.valueOf(page));
        return futApi.getPlayersForClub(query);
    }

    interface FutApiInternal {

        @GET("item")
        Observable<FutApiResponse> getPlayersForClub(@Query("jsonParamObject") String query);
    }
}
