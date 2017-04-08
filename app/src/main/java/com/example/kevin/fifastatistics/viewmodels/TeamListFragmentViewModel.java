package com.example.kevin.fifastatistics.viewmodels;

import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.models.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.league.League;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import java.util.List;

import rx.Subscription;

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
            Subscription s = RetrievalManager.getTeamsForLeague(mLeague).subscribe(getTeamObserver());
            addSubscription(s);
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
