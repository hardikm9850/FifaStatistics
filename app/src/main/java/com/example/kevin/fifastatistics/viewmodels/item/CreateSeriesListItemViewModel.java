package com.example.kevin.fifastatistics.viewmodels.item;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.CreateMatchActivity;
import com.example.kevin.fifastatistics.fragments.CreateSeriesMatchListFragment;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.interfaces.OnMatchUpdatedListener;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.ResourceUtils;

import java.util.List;

public class CreateSeriesListItemViewModel extends BaseObservable {

    private Match mMatch;
    private User mUser;
    private Friend mOpponent;
    private ActivityLauncher mLauncher;
    private List<OnMatchUpdatedListener> mOnMatchUpdatedListeners;
    private int mMatchNumber;

    public CreateSeriesListItemViewModel(ActivityLauncher launcher, Match match, User currentUser, Friend opponent,
                                         int matchNumber, List<OnMatchUpdatedListener> listeners) {
        mMatch = match;
        mLauncher = launcher;
        mUser = currentUser;
        mOpponent = opponent;
        mMatchNumber = matchNumber;
        mOnMatchUpdatedListeners = listeners;
    }

    public void onMatchUpdated(Match match) {
        Match oldMatch = mMatch;
        setMatch(match);
        notifyMatchUpdatedListeners(oldMatch, match);
    }

    private void notifyMatchUpdatedListeners(Match oldMatch, Match newMatch) {
        for (OnMatchUpdatedListener listener : mOnMatchUpdatedListeners) {
            listener.onMatchUpdated(oldMatch, newMatch);
        }
    }

    public void onItemClicked() {
        notifyUpdating();
        Intent intent = CreateMatchActivity.getPartOfSeriesIntent(mLauncher.getContext(),
                mOpponent, mMatch, mMatch.getTeamLoser(), mMatch.getTeamWinner());
        mLauncher.launchActivity(intent, CreateSeriesMatchListFragment.CREATE_SERIES_REQUEST_CODE, null);
    }

    private void notifyUpdating() {
        for (OnMatchUpdatedListener listener : mOnMatchUpdatedListeners) {
            listener.setMatchIndex(mMatchNumber - 1);
        }
    }

    public void setMatch(Match match) {
        mMatch = match;
        notifyChange();
    }

    public Match getMatch() {
        return mMatch;
    }

    @Bindable
    public String getMatchNumber() {
        return "0" + mMatchNumber;
    }

    @Bindable
    public int getIsPenaltiesVisible() {
        return (mMatch.getPenalties() != null) ? View.VISIBLE : View.GONE;
    }

    @Bindable
    public String getPenaltiesText() {
        return "(" + mMatch.getPenaltiesScore(mUser.getId()) + ")";
    }

    @Bindable
    public String getMatchScore() {
        return mMatch.getMatchScore(mUser.getId());
    }

    @Bindable
    public String getMatchResult() {
        return ((mMatch.didWin(mUser)) ? getResultPrefix(R.string.win) : getResultPrefix(R.string.loss));
    }

    private String getResultPrefix(int id) {
        return ResourceUtils.getStringFromResourceId(id).substring(0, 1);
    }
}
