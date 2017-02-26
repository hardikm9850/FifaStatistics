package com.example.kevin.fifastatistics.utils;

import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;
import com.example.kevin.fifastatistics.network.ApiAdapter;
import com.example.kevin.fifastatistics.network.CreateFailedException;

import rx.Observable;
import rx.exceptions.Exceptions;

public class SeriesUtils {

    /**
     * Creates a series (sends a POST) and returns the created series observable.
     * @param series     The series to be created
     * @return  The series observable
     * @throws CreateFailedException if the POST fails.
     */
    public static Observable<Series> createSeries(Series series) throws CreateFailedException {
        return ApiAdapter.getFifaApi().createSeries(series)
                .compose(ObservableUtils.applySchedulers())
                .onErrorReturn(t -> {throw Exceptions.propagate(new CreateFailedException(t));})
                .map(response -> {
                    String matchId = NetworkUtils.getIdFromResponse(response);
                    return Series.fromSeriesWithId(series, matchId);
                });
    }
}