package com.example.kevin.fifastatistics.viewmodels;

import android.util.Log;

import com.example.kevin.fifastatistics.models.apiresponses.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.league.League;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.network.FifaApi;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import java.util.List;

public class TeamListFragmentViewModel extends ProgressFragmentViewModel {

    private OnTeamsLoadedListener mListener;
    private League mLeague;

    public TeamListFragmentViewModel(OnTeamsLoadedListener listener, League league) {
        mListener = listener;
        mLeague = league;
    }

    public void loadTeams() {
        showProgressBar();
        if (mLeague != null && mLeague.getTeamUrl() != null) {
            FifaApi.getLeagueApi().getTeams(mLeague.getTeamUrl())
                    .compose(ObservableUtils.applySchedulers())
                    .subscribe(getTeamObserver());
        } else {
            notifyError();
        }
    }

    private void notifyError() {
        hideProgressBar();
        if (mListener != null) {
            mListener.onTeamsLoadFailure();
        }
    }

    private ObservableUtils.EmptyOnCompleteObserver<ApiListResponse<Team>> getTeamObserver() {
        return new ObservableUtils.EmptyOnCompleteObserver<ApiListResponse<Team>>() {
            @Override
            public void onError(Throwable e) {
                Log.d("VM", "error: " + e.getMessage());
                notifyError();
            }

            @Override
            public void onNext(ApiListResponse<Team> response) {
                hideProgressBar();
                if (mListener != null) {
                    mListener.onTeamsLoaded(response.getItems());
                }
            }
        };
    }

    public interface OnTeamsLoadedListener {
        void onTeamsLoaded(List<Team> teams);
        void onTeamsLoadFailure();
    }
}
