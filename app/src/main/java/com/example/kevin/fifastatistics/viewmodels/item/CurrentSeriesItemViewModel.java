package com.example.kevin.fifastatistics.viewmodels.item;

import android.app.Activity;
import android.content.Intent;
import android.databinding.Bindable;
import android.support.annotation.ColorRes;

import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.match.CurrentSeries;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.utils.IntentFactory;

public class CurrentSeriesItemViewModel extends ItemViewModel<CurrentSeries> {

    private Player mCurrentUser;

    public CurrentSeriesItemViewModel(CurrentSeries item, ActivityLauncher launcher,
                                      @ColorRes int color, boolean isLastItem, Player currentUser) {
        super(item, launcher, color, isLastItem);
        mCurrentUser = currentUser;
    }

    @Bindable
    public String getSummary() {
        if (mItem != null) {
            return mItem.getScore(mCurrentUser) + " vs. " + mItem.getOpponent().getFirstName();
        } else {
            return null;
        }
    }

    public void onContinueClick() {
        if (mItem != null && mLauncher != null) {
            Intent newSeriesIntent = IntentFactory.createNewSeriesActivityIntent(
                    mLauncher.getContext(), mItem.getOpponent());
            mLauncher.launchActivity(newSeriesIntent, Activity.RESULT_OK, null);
        }
    }
}
