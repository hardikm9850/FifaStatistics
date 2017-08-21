package com.example.kevin.fifastatistics.network.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.interfaces.FragmentArguments;
import com.example.kevin.fifastatistics.listeners.SimpleObserver;
import com.example.kevin.fifastatistics.managers.RetrofitErrorManager;
import com.example.kevin.fifastatistics.managers.preferences.PrefsManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.CurrentSeries;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.FifaApi;
import com.example.kevin.fifastatistics.utils.NetworkUtils;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import java.util.ArrayList;

import retrofit2.Response;
import rx.Observable;

public class CurrentSeriesService extends IntentService implements FragmentArguments {

    private static final String NAME = "CURRENT_SERIES_SERVICE";
    private static final String EXTRA_TYPE = "TYPE";

    public enum Type {
        SAVE, DELETE
    }

    public static Intent getSaveIntent(User user, ArrayList<Match> matches, Friend opponent) {
        Intent intent = new Intent(FifaApplication.getContext(), CurrentSeriesService.class);
        intent.putExtra(USER, user);
        intent.putExtra(MATCH, matches);
        intent.putExtra(OPPONENT, opponent);
        intent.putExtra(EXTRA_TYPE, Type.SAVE);
        return intent;
    }

    public static Intent getDeleteIntent(User user, Series series) {
        Intent intent = new Intent(FifaApplication.getContext(), CurrentSeriesService.class);
        intent.putExtra(USER, user);
        intent.putExtra(SERIES, series);
        intent.putExtra(EXTRA_TYPE, Type.DELETE);
        return intent;
    }

    public CurrentSeriesService() {
        super(NAME);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Type t = (Type) intent.getSerializableExtra(EXTRA_TYPE);
        User user = (User) intent.getSerializableExtra(USER);
        if (t == Type.SAVE) {
            CurrentSeries currentSeries = getCurrentSeries(intent, user);
            save(currentSeries);
        } else {
            Series s = (Series) intent.getSerializableExtra(SERIES);
            remove(s, user);
        }
    }

    @SuppressWarnings("unchecked")
    private CurrentSeries getCurrentSeries(Intent intent, User user) {
        Friend opponent = (Friend) intent.getSerializableExtra(OPPONENT);
        CurrentSeries currentSeries = PrefsManager.getSeriesPrefs().getCurrentSeriesForOpponent(opponent.getId());
        ArrayList<Match> matches = (ArrayList<Match>) intent.getSerializableExtra(MATCH);
        if (currentSeries == null) {
            return new CurrentSeries(user.getId(), opponent, matches);
        } else {
            currentSeries.setMatches(matches);
            return currentSeries;
        }
    }

    private void save(CurrentSeries series) {
        if (series != null) {
            Observable<Response<Void>> observable = series.getId() == null ?
                    FifaApi.getCurrentSeriesApi().upload(series) :
                    FifaApi.getCurrentSeriesApi().update(series.getId(), series);
            observable
                    .retryWhen(ObservableUtils.getExponentialBackoffRetryWhen())
                    .map(response -> saveSeriesWithId(series, response))
                    .compose(ObservableUtils.applySchedulers())
                    .subscribe(new SimpleObserver<CurrentSeries>() {
                        @Override
                        public void onError(Throwable e) {
                            Log.e(NAME, "error saving", e);
                        }
                    });
        }
    }

    private CurrentSeries saveSeriesWithId(CurrentSeries series, Response<Void> response) {
        String id = NetworkUtils.getIdFromResponse(response);
        series.setId(id);
        PrefsManager.getSeriesPrefs().saveCurrentSeries(series);
        return series;
    }

    private void remove(Series series, User user) {
        String oppId = series.getPlayerOne().equals(user) ?
                series.getPlayerTwo().getId() : series.getPlayerOne().getId();
        CurrentSeries currentSeries = PrefsManager.getSeriesPrefs().getCurrentSeriesForOpponent(oppId);
        if (currentSeries != null) {
            FifaApi.getCurrentSeriesApi().delete(currentSeries.getId())
                    .subscribe(new SimpleObserver<Response<Void>>() {
                        @Override
                        public void onNext(Response<Void> response) {
                            PrefsManager.getSeriesPrefs().removeCurrentSeries(oppId);
                        }

                        @Override
                        public void onError(Throwable e) {
                            RetrofitErrorManager.ErrorCode c = RetrofitErrorManager.ErrorCode.fromThrowable(e);
                            if (c == RetrofitErrorManager.ErrorCode.NOT_FOUND) {
                                PrefsManager.getSeriesPrefs().removeCurrentSeries(oppId);
                            }
                        }
                    });
        }
    }
}
