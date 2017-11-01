package com.example.kevin.fifastatistics.managers;

import android.util.Log;

import com.example.kevin.fifastatistics.models.FutApiResponse;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.Footballer;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.network.FifaApi;
import com.example.kevin.fifastatistics.network.FutApi;
import com.example.kevin.fifastatistics.utils.ObservableUtils;
import com.example.kevin.fifastatistics.utils.StorageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

import static com.example.kevin.fifastatistics.managers.sync.FootballersSynchronizer.FOOTBALLERS_FILE;

public class FootballerLoader {

    private static List<Footballer> footballers;
    private static Map<Team, List<Footballer>> teamFootballers = new HashMap<>();

    public Observable<List<Footballer>> loadFootballers() {
        if (footballers != null) {
            return Observable.just(footballers);
        } else {
            return loadFootballersFromDisk();
        }
    }

    private Observable<List<Footballer>> loadFootballersFromDisk() {
        Observable<List<Footballer>> readResult = StorageUtils.readListFromDisk(FOOTBALLERS_FILE);
        return readResult
                .onErrorResumeNext(throwable -> null)
                .doOnNext(list -> footballers = list)
                .compose(ObservableUtils.applySchedulers());
    }

    public Observable<List<Footballer>> loadTeamFootballers(Team team) {
        Log.d("KEVIN!!!!!!", "LOADING TEAM: " + team.toString());
        if (teamFootballers.containsKey(team)) {
            return Observable.just(teamFootballers.get(team));
        } else {
            return loadTeamFootballersFromDiskOrNetwork(team);
        }
    }

    private Observable<List<Footballer>> loadTeamFootballersFromDiskOrNetwork(final Team team) {
        if (team != null) {
            String fileName = getTeamFilename(team);
            Observable<List<Footballer>> readResult = StorageUtils.readListFromDisk(fileName);
            return readResult
                    .onErrorResumeNext(loadTeamFootballersFromNetwork(team, fileName))
                    .compose(ObservableUtils.applySchedulers());
        } else {
            return Observable.empty();
        }
    }

    private String getTeamFilename(Team team) {
        return FOOTBALLERS_FILE + team.getId();
    }

    private Observable<List<Footballer>> loadTeamFootballersFromNetwork(Team team, String fileName) {
        Observable<List<Footballer>> footballers = getConcatenatedFootballerLists(team.getFutId());
        return footballers.doOnNext(list -> {
            teamFootballers.put(team, list);
            StorageUtils.writeToDisk(list, fileName).subscribe();
        });
    }

    private Observable<List<Footballer>> getConcatenatedFootballerLists(int teamFutId) {
        final List<Footballer> footballers = new ArrayList<>();
        FutApi api = FifaApi.getFutApi();
        return Observable.range(1, Integer.MAX_VALUE)
                .concatMap(page -> api.getPlayersForClub(teamFutId, page))
                .takeUntil(result -> result.getPage() > result.getTotalPages())
                .onErrorReturn(t -> new FutApiResponse())
                .reduce(footballers, (footballers1, futApiResponse) -> {
                    if (futApiResponse.getItems() != null) {
                        footballers.addAll(futApiResponse.getItems());
                    }
                    return footballers;
                })
                .compose(ObservableUtils.applySchedulers());

    }
}
