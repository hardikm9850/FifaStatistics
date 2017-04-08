package com.example.kevin.fifastatistics.fragments;

import android.support.v4.app.DialogFragment;

import com.example.kevin.fifastatistics.adapters.MatchStreamRecyclerViewAdapter;
import com.example.kevin.fifastatistics.models.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchProjection;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.network.FifaApi;

import java.util.Map;

import rx.Observable;

public class MatchStreamFragment extends EventStreamFragment<MatchProjection, MatchStreamRecyclerViewAdapter> {

    @Override
    public Observable<ApiListResponse<MatchProjection>> getLoadMoreObservable(String nextUri) {
        return FifaApi.getMatchApi().getNextMatches(nextUri);
    }

    @Override
    protected MatchStreamRecyclerViewAdapter getAdapter(Player user) {
        return new MatchStreamRecyclerViewAdapter(mUser);
    }

    @Override
    protected Observable<ApiListResponse<MatchProjection>> getLoadEventsObservable() {
        return FifaApi.getMatchApi().getMatches();
    }

    @Override
    protected DialogFragment getFilterDialogFragment() {
        return MatchesFilterDialogFragment.newInstance();
    }

    @Override
    protected Observable<ApiListResponse<MatchProjection>> getFilterObservable(Map<String, String> queryFilter) {
        return FifaApi.getMatchApi().filterMatches(queryFilter);
    }
}
