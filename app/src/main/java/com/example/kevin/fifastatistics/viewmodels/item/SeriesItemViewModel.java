package com.example.kevin.fifastatistics.viewmodels.item;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.kevin.fifastatistics.activities.SeriesActivity;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.match.SeriesProjection;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;

public class SeriesItemViewModel extends EventViewModel<SeriesProjection> {

    private ActivityLauncher mLauncher;

    public SeriesItemViewModel(SeriesProjection match, Player user, ActivityLauncher launcher) {
        super(match, user);
        mLauncher = launcher;
    }

    @Override
    public void openEventDetail(View view) {
        Context c = view.getContext();
        Intent intent = SeriesActivity.getLaunchIntent(c, mEvent);
        mLauncher.launchActivity(intent, Activity.RESULT_OK, null);
    }
}
