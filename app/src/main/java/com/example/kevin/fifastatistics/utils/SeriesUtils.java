package com.example.kevin.fifastatistics.utils;

import com.example.kevin.fifastatistics.managers.preferences.CurrentSeriesPrefs;
import com.example.kevin.fifastatistics.managers.preferences.PrefsManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.CurrentSeries;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.CreateFailedException;
import com.example.kevin.fifastatistics.network.FifaApi;

import rx.Observable;
import rx.exceptions.Exceptions;

public class SeriesUtils {

    /**
     * Creates a series (sends a POST) and returns the created series observable.
     * @param series     The series to be created
     * @return  The series observable
     * @throws CreateFailedException if the POST fails.
     */
    public static Observable<Series> createSeries(Series series, User user) throws CreateFailedException {
        String opponentId = series.getOpponentId(user);
        CurrentSeriesPrefs prefs = PrefsManager.getSeriesPrefs();
        return Observable.<CurrentSeries>create(s -> s.onNext(prefs.getCurrentSeriesForOpponent(opponentId)))
                .map(s -> {
                    series.setCurrentSeriesId(s.getId());
                    return series;
                })
                .flatMap(s -> FifaApi.getSeriesApi().createSeries(s))
                .compose(ObservableUtils.applySchedulers())
                .onErrorReturn(t -> {throw Exceptions.propagate(new CreateFailedException(t));})
                .map(response -> {
                    String matchId = NetworkUtils.getIdFromResponse(response);
                    return Series.fromSeriesWithId(series, matchId);
                });
    }
}
