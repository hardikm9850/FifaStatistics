package com.example.kevin.fifastatistics.viewmodels;

import android.databinding.Bindable;
import android.view.View;

import com.example.kevin.fifastatistics.models.databasemodels.match.MatchProjection;
import com.example.kevin.fifastatistics.models.databasemodels.match.Penalties;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;

public class MatchesItemViewModel extends EventViewModel<MatchProjection> {

    private int mTopPenalties;
    private int mBottomPenalties;
    private int mWinnerPenalties;
    private int mLoserPenalties;

    public MatchesItemViewModel(MatchProjection match, Player user) {
        super(match, user);
    }

    @Override
    protected void onInitEventUserPlayedIn(boolean didWin) {
        Penalties penalties = mEvent.getPenalties();
        mWinnerPenalties = penalties != null ? penalties.getWinner() : 0;
        mLoserPenalties = penalties != null ? penalties.getLoser() : 0;
        if (didWin) {
            mTopPenalties = mWinnerPenalties;
            mBottomPenalties = mLoserPenalties;
        } else {
            mTopPenalties = mLoserPenalties;
            mBottomPenalties = mWinnerPenalties;
        }
    }

    @Override
    protected void onInitEventNotPlayedIn() {
        mTopPenalties = mWinnerPenalties;
        mBottomPenalties = mLoserPenalties;
    }

    @Override
    protected int getScoreWinner() {
        return mEvent.getGoalsWinner();
    }

    @Override
    protected int getScoreLoser() {
        return mEvent.getGoalsLoser();
    }

    @Bindable
    public String getTopPenalties() {
        return String.valueOf(mTopPenalties);
    }

    @Bindable
    public String getBottomPenalties() {
        return String.valueOf(mBottomPenalties);
    }

    @Bindable
    public int getPenaltiesVisibility() {
        return mEvent.getPenalties() != null ? View.VISIBLE : View.INVISIBLE;
    }

    @Override
    public void openEventDetail() {

    }
}
