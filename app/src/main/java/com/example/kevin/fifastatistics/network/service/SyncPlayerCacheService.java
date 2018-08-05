package com.example.kevin.fifastatistics.network.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.interfaces.FragmentArguments;
import com.example.kevin.fifastatistics.listeners.SimpleObserver;
import com.example.kevin.fifastatistics.managers.sync.FootballersSynchronizer;
import com.example.kevin.fifastatistics.models.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.Footballer;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.network.FifaApi;
import com.example.kevin.fifastatistics.utils.ObservableUtils;
import com.example.kevin.fifastatistics.utils.StorageUtils;

import java.util.List;

public class SyncPlayerCacheService extends IntentService implements FragmentArguments {

    private static final String NAME = "PLAYER_CACHE_SERVICE";

    public static Intent getSpecificTeamIntent(Team team) {
        Intent intent = new Intent(FifaApplication.getContext(), SyncPlayerCacheService.class);
        intent.putExtra(TEAM, team);
        return intent;
    }

    public static Intent getPlayersIntent() {
        return new Intent(FifaApplication.getContext(), SyncPlayerCacheService.class);
    }

    public SyncPlayerCacheService() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            Team team = (Team) intent.getSerializableExtra(TEAM);
            if (team != null) {
                syncTeam(team);
            } else {
                syncPlayers();
            }
        }
    }

    private void syncTeam(Team team) {

        // get players from team from fut api
        // save to disk
    }

    private void syncPlayers() {
        Log.d(NAME, "loading footballer cache");
        FifaApi.getFootballerApi().getFootballers()
                .retryWhen(ObservableUtils.getExponentialBackoffRetryWhen())
                .compose(ObservableUtils.applySchedulers())
                .subscribe(saveFootballersToDisk());
    }

    private SimpleObserver<ApiListResponse<Footballer>> saveFootballersToDisk() {
        return new SimpleObserver<ApiListResponse<Footballer>>() {
            @Override
            public void onError(Throwable e) {
                Log.e(NAME, "error retrieving footballers", e);
            }

            @Override
            public void onNext(ApiListResponse<Footballer> response) {
                List<Footballer> footballers = response.getItems();
                if (footballers != null) {
                    writeToDisk(footballers);
                }
            }
        };
    }

    private void writeToDisk(List<Footballer> footballers) {
        Context c = SyncPlayerCacheService.this;
        String file = FootballersSynchronizer.FOOTBALLERS_FILE;
        boolean success = StorageUtils.writeToInternalDiskStorage(file, footballers, c);
        if (success) {
            Log.d(NAME, "successfully saved footballers");
        } else {
            Log.e(NAME, "failed to save footballers");
        }
    }
}
