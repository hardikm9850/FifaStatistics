package com.example.kevin.fifastatistics.viewmodels.item;

import android.view.View;

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

    }
}
