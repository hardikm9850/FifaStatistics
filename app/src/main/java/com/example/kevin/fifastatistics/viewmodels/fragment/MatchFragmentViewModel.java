package com.example.kevin.fifastatistics.viewmodels.fragment;

import android.databinding.Bindable;
import android.view.View;

import com.example.kevin.fifastatistics.BR;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.listeners.SimpleObserver;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchProjection;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.EventResultHeaderViewModel;
import com.example.kevin.fifastatistics.viewmodels.card.MatchEventsCardViewModel;

import java.util.Collections;
import java.util.List;

import rx.Subscription;

import static com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents.CardItem;
import static com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents.GoalItem;
import static com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents.InjuryItem;

public class MatchFragmentViewModel extends ProgressFragmentViewModel {

    private OnMatchLoadedListener mListener;
    private MatchProjection mProjection;
    private EventResultHeaderViewModel mHeaderViewModel;
    private MatchEventsCardViewModel<GoalItem> mGoalsViewModel;
    private MatchEventsCardViewModel<CardItem> mCardsViewModel;
    private MatchEventsCardViewModel<InjuryItem> mInjuriesViewModel;
    private Match mMatch;
    private String mMatchId;
    private Player mPlayer;

    public MatchFragmentViewModel(OnMatchLoadedListener listener, MatchProjection projection,
                                  Player player, String matchId, ActivityLauncher launcher) {
        mListener = listener;
        mProjection = projection;
        mPlayer = player;
        mMatchId = matchId;
        mHeaderViewModel = new EventResultHeaderViewModel(projection, player);
        mGoalsViewModel = new MatchEventsCardViewModel<>(launcher);
        mInjuriesViewModel = new MatchEventsCardViewModel<>(launcher);
        mCardsViewModel = new MatchEventsCardViewModel<>(launcher);
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
        mGoalsViewModel.setMatch(mMatch, mMatch.getGoals());
        mCardsViewModel.setMatch(mMatch, mMatch.getCards());
        mInjuriesViewModel.setMatch(mMatch, mMatch.getInjuries());
        if (mProjection != null) {
            notifyPropertyChanged(BR.statsVisibility);
            notifyPropertyChanged(BR.stats);
            notifyPropertyChanged(BR.match);
            notifyPropertyChanged(BR.visibility);
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
        return mMatch != null ? Collections.singletonList(new User.StatsPair(mMatch.getStats())) : null;
    }

    @Bindable
    public MatchEventsCardViewModel<GoalItem> getGoals() {
        return mGoalsViewModel;
    }

    @Bindable
    public MatchEventsCardViewModel<CardItem> getCards() {
        return mCardsViewModel;
    }

    @Bindable
    public MatchEventsCardViewModel<InjuryItem> getInjuries() {
        return mInjuriesViewModel;
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
