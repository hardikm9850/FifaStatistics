package com.example.kevin.fifastatistics.managers.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.kevin.fifastatistics.listeners.SimpleObserver;
import com.example.kevin.fifastatistics.network.service.SyncPlayerCacheService;
import com.example.kevin.fifastatistics.utils.StorageUtils;

import java.util.List;

import rx.Observable;
import rx.Subscription;

public class FootballersSynchronizer {

    public static final String FOOTBALLERS_FILE = "footballersFile";

    private final Context mContext;

    public FootballersSynchronizer(@NonNull Context context) {
        mContext = context;
    }

    public Subscription sync() {
        Observable<List> currentCache = StorageUtils.readFromDisk(FOOTBALLERS_FILE, List.class);
        return currentCache.subscribe(new SimpleObserver<List>() {
            @Override
            public void onError(Throwable e) {
                loadFootballersFromServer();
            }

            @Override
            public void onNext(List list) {
                if (list == null) {
                    loadFootballersFromServer();
                }
            }
        });
    }

    private void loadFootballersFromServer() {
        Intent footballerServiceIntent = SyncPlayerCacheService.getPlayersIntent();
        mContext.startService(footballerServiceIntent);
    }
}
