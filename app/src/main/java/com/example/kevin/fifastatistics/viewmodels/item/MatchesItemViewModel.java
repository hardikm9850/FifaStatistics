package com.example.kevin.fifastatistics.viewmodels.item;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.view.View;

import com.example.kevin.fifastatistics.activities.MatchActivity;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchProjection;
import com.example.kevin.fifastatistics.models.databasemodels.match.Penalties;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;

public class MatchesItemViewModel extends EventViewModel<MatchProjection> {

    private int mTopPenalties;
    private int mBottomPenalties;
    private int mWinnerPenalties;
    private int mLoserPenalties;
    private ActivityLauncher mLauncher;

    public MatchesItemViewModel(MatchProjection match, Player user, ActivityLauncher launcher) {
        super(match, user);
        mLauncher = launcher;
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
    public void openEventDetail(View view) {
        Context c = view.getContext();
        Intent intent = MatchActivity.getLaunchIntent(c, mEvent);
        mLauncher.launchActivity(intent, Activity.RESULT_OK, null);
    }
}
