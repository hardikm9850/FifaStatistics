package com.example.kevin.fifastatistics.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.support.annotation.ColorRes;
import android.view.View;

import com.example.kevin.fifastatistics.activities.MatchUpdateActivity;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;

public class PendingUpdateItemViewModel extends FifaBaseViewModel {

    private MatchUpdate mUpdate;
    private ActivityLauncher mLauncher;
    private int mColor;
    private boolean mIsLastItem;

    public PendingUpdateItemViewModel(MatchUpdate update, ActivityLauncher launcher,
                                      @ColorRes int color, boolean isLastItem) {
        mUpdate = update;
        mLauncher = launcher;
        mColor = color;
        mIsLastItem = isLastItem;
    }

    public void setUpdate(MatchUpdate update) {
        mUpdate = update;
        notifyChange();
    }

    @Bindable
    public String getSummary() {
        return mUpdate != null ? mUpdate.getSummary() : null;
    }

    @Bindable
    public int getColor() {
        return mColor;
    }

    @Bindable
    public int getDividerVisibility() {
        return mIsLastItem ? View.GONE : View.VISIBLE;
    }

    @Override
    public void destroy() {
        super.destroy();
        mLauncher = null;
    }

    public void onReviewButtonClick() {
        if (mLauncher != null && mUpdate != null) {
            Intent intent = MatchUpdateActivity.getLaunchIntent(mLauncher.getContext(),
                    MatchUpdateActivity.MatchEditType.REVIEW, mUpdate, null);
            mLauncher.launchActivity(intent, Activity.RESULT_OK, null);
        }
    }
}
