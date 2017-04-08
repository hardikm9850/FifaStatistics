package com.example.kevin.fifastatistics.fragments;

import android.support.v4.app.DialogFragment;

import com.example.kevin.fifastatistics.adapters.SeriesStreamAdapter;
import com.example.kevin.fifastatistics.models.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.match.SeriesProjection;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.network.FifaApi;

import java.util.Map;

import rx.Observable;

public class SeriesStreamFragment extends EventStreamFragment<SeriesProjection, SeriesStreamAdapter> {

    @Override
    public Observable<ApiListResponse<SeriesProjection>> getLoadMoreObservable(String nextUri) {
        return FifaApi.getSeriesApi().getNextSeries(nextUri);
    }

    @Override
    protected SeriesStreamAdapter getAdapter(Player user) {
        return new SeriesStreamAdapter(mUser);
    }

    @Override
    protected Observable<ApiListResponse<SeriesProjection>> getLoadEventsObservable() {
        return FifaApi.getSeriesApi().getSeries();
    }

    @Override
    protected DialogFragment getFilterDialogFragment() {
        return SeriesFilterDialogFragment.newInstance();
    }

    @Override
    protected Observable<ApiListResponse<SeriesProjection>> getFilterObservable(Map<String, String> queryFilter) {
        return FifaApi.getSeriesApi().filterSeries(queryFilter);
    }
}
