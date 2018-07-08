package com.example.kevin.fifastatistics.viewmodels.card;

import android.support.annotation.NonNull;

import com.example.kevin.fifastatistics.adapters.SeriesMatchAdapter;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

import java.util.List;

public class SeriesMatchesCardViewModel extends RecyclerCardViewModel<Match, SeriesMatchAdapter> {

    public SeriesMatchesCardViewModel(ActivityLauncher launcher, Series series, User currentUser) {
        super(launcher, series.getMatches(), true);
        mAdapter.setUser(currentUser);
        mAdapter.setSeries(series);
    }

    @NonNull
    @Override
    protected SeriesMatchAdapter createAdapter() {
        return new SeriesMatchAdapter(mItems, mLauncher);
    }
}
