package com.example.kevin.fifastatistics.viewmodels.item;

import android.app.Activity;
import android.content.Intent;
import android.databinding.Bindable;
import android.support.annotation.ColorRes;

import com.example.kevin.fifastatistics.activities.MatchUpdateActivity;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;

public class PendingUpdateItemViewModel extends ItemViewModel<MatchUpdate> {

    public PendingUpdateItemViewModel(MatchUpdate update, ActivityLauncher launcher, @ColorRes int color, boolean isLastItem) {
        super(update, launcher, color, isLastItem);
    }

    @Bindable
    public String getSummary() {
        return mItem != null ? mItem.getSummary() : null;
    }

    public void onReviewButtonClick() {
        if (mItem != null && mLauncher != null) {
            Intent intent = MatchUpdateActivity.getLaunchIntent(mLauncher.getContext(),
                    MatchUpdateActivity.MatchEditType.REVIEW, mItem, null);
            mLauncher.launchActivity(intent, Activity.RESULT_OK, null);
        }
    }
}
