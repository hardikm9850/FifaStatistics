package com.example.kevin.fifastatistics.managers.sync;

import android.content.Context;
import android.content.Intent;

import com.example.kevin.fifastatistics.interfaces.Consumer;
import com.example.kevin.fifastatistics.listeners.SimpleObserver;
import com.example.kevin.fifastatistics.managers.preferences.CurrentSeriesPrefs;
import com.example.kevin.fifastatistics.managers.preferences.PrefsManager;
import com.example.kevin.fifastatistics.models.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.match.CurrentSeries;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.FifaApi;
import com.example.kevin.fifastatistics.network.service.CurrentSeriesService;
import com.example.kevin.fifastatistics.utils.NetworkUtils;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Builder;
import retrofit2.Response;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;

public class CurrentSeriesSynchronizer {

    private final Context context;
    private final User user;
    private final Consumer<Throwable> onSyncErrorHandler;
    private final Consumer<List<CurrentSeries>> onSyncSuccessHandler;
    private final Consumer<CurrentSeries> onSaveSuccessHandler;
    private final Action0 onSyncCompleteHandler;

    public static CurrentSeriesSynchronizer with(User user, Context context) {
        return CurrentSeriesSynchronizer.builder().user(user).context(context).build();
    }

    @Builder
    private CurrentSeriesSynchronizer(User user, Consumer<Throwable> onSyncErrorHandler,
                                      Consumer<List<CurrentSeries>> onSyncSuccessHandler,
                                      Consumer<CurrentSeries> onSaveSuccessHandler,
                                      Action0 onSyncCompleteHandler, Context context) {
        this.user = user;
        this.onSyncErrorHandler = onSyncErrorHandler;
        this.onSyncSuccessHandler = onSyncSuccessHandler;
        this.onSaveSuccessHandler = onSaveSuccessHandler;
        this.onSyncCompleteHandler = onSyncCompleteHandler;
        this.context = context;
    }

    public Subscription get() {
        int count = user.getCurrentSeriesCount();
        CurrentSeriesPrefs seriesPrefs = PrefsManager.getSeriesPrefs();
        if (count > 0) {
            return Observable.<Collection<CurrentSeries>>create(s -> s.onNext(seriesPrefs.getCurrentSeries()))
                    .flatMap(this::retrieveCurrentSeries)
                    .compose(ObservableUtils.applySchedulers())
                    .subscribe(new SimpleObserver<ApiListResponse<CurrentSeries>>() {
                        @Override
                        public void onError(Throwable e) {
                            fail(e);
                        }

                        @Override
                        public void onNext(ApiListResponse<CurrentSeries> response) {
                            if (response != null) {
                                List<CurrentSeries> series = response.getItems();
                                seriesPrefs.setCurrentSeries(series);
                                succeed(series);
                            }
                            complete();
                        }
                    });
        } else {
            complete();
            return null;
        }
    }

    private Observable<ApiListResponse<CurrentSeries>> retrieveCurrentSeries(Collection<?> savedSeries) {
        if (user.getCurrentSeriesCount() != savedSeries.size()) {
            return FifaApi.getCurrentSeriesApi().getCurrentSeries(user.getId());
        } else {
            return Observable.just(null);
        }
    }

    private void fail(Throwable e) {
        if (onSyncErrorHandler != null) {
            onSyncErrorHandler.accept(e);
        }
    }

    private void succeed(List<CurrentSeries> series) {
        if (onSyncSuccessHandler != null) {
            onSyncSuccessHandler.accept(series);
        }
    }

    private void complete() {
        if (onSyncCompleteHandler != null) {
            onSyncCompleteHandler.call();
        }
    }

    public void save(ArrayList<Match> matches, Friend opponent, Team userTeam, Team opponentTeam) {
        Intent intent = CurrentSeriesService.getSaveIntent(user, matches, opponent, userTeam, opponentTeam);
        context.startService(intent);
    }

    private CurrentSeries saveSeriesWithId(CurrentSeries series, Response<Void> response) {
        String id = NetworkUtils.getIdFromResponse(response);
        series.setId(id);
        PrefsManager.getSeriesPrefs().saveCurrentSeries(series);
        return series;
    }

    public void remove(Series series) {
        Intent intent = CurrentSeriesService.getDeleteIntent(user, series);
        context.startService(intent);
    }
}
