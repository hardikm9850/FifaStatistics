package com.example.kevin.fifastatistics.viewmodels.fragment;

import android.databinding.Bindable;
import android.view.View;

import com.example.kevin.fifastatistics.BR;
import com.example.kevin.fifastatistics.listeners.SimpleObserver;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchProjection;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.EventResultHeaderViewModel;

import java.util.Collections;
import java.util.List;

import rx.Subscription;

public class MatchFragmentViewModel extends ProgressFragmentViewModel {

    private OnMatchLoadedListener mListener;
    private MatchProjection mProjection;
    private EventResultHeaderViewModel mHeaderViewModel;
    private Match mMatch;
    private String mMatchId;
    private Player mPlayer;

    public MatchFragmentViewModel(OnMatchLoadedListener listener, MatchProjection projection,
                                  Player player, String matchId) {
        mListener = listener;
        mProjection = projection;
        mPlayer = player;
        mMatchId = matchId;
        mHeaderViewModel = new EventResultHeaderViewModel(projection, player);
    }

    public void loadMatch() {
        if (mMatch != null) {
            return;
        }
        showProgressBar();
        String id = mProjection != null ? mProjection.getId() : mMatchId;
        Subscription s = RetrievalManager.getMatch(id).subscribe(new SimpleObserver<Match>() {
            @Override
            public void onError(Throwable e) {
                hideProgressBar();
                showRetryButton();
                if (mListener != null) {
                    mListener.onMatchLoadFailed(e);
                }
            }

            @Override
            public void onNext(Match match) {
                hideProgressBar();
                mMatch = match;
                if (mListener != null) {
                    mListener.onMatchLoaded(match);
                }
                notifyMatchLoaded();
            }
        });
        addSubscription(s);
    }

    private void notifyMatchLoaded() {
        if (mProjection != null) {
            notifyPropertyChanged(BR.statsVisibility);
            notifyPropertyChanged(BR.stats);
            notifyPropertyChanged(BR.match);
        } else {
            mHeaderViewModel.setEvent(mMatch);
            notifyChange();
        }
    }

    @Override
    public void onRetryButtonClick() {
        loadMatch();
    }

    @Override
    public void destroy() {
        super.destroy();
        mListener = null;
    }

    @Bindable
    public int getHeaderVisibility() {
        return mProjection == null && mMatch == null ? View.GONE : View.VISIBLE;
    }

    @Bindable
    public int getStatsVisibility() {
        return mMatch != null ? View.VISIBLE : View.GONE;
    }

    @Bindable
    public List<User.StatsPair> getStats() {
        return mMatch != null ? Collections.singletonList(mMatch.getStats()) : null;
    }

    @Bindable
    public Match getMatch() {
        return mMatch;
    }

    public void setMatch(Match match) {
        mMatch = match;
        if (match != null && mListener != null) {
            mListener.onMatchLoaded(match);
        }
    }

    public String getUsername() {
        return mPlayer.getName();
    }

    public EventResultHeaderViewModel getHeaderViewModel() {
        return mHeaderViewModel;
    }

    public interface OnMatchLoadedListener {
        void onMatchLoaded(Match match);
        void onMatchLoadFailed(Throwable t);
    }
}
