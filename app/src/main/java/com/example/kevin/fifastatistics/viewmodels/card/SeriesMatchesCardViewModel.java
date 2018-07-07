package com.example.kevin.fifastatistics.viewmodels.card;

import android.support.annotation.NonNull;

import com.example.kevin.fifastatistics.adapters.SeriesMatchAdapter;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

import java.util.List;

public class SeriesMatchesCardViewModel extends RecyclerCardViewModel<Match, SeriesMatchAdapter> {

    public SeriesMatchesCardViewModel(ActivityLauncher launcher, List<Match> items,
                                      User currentUser, Player seriesWinner) {
        super(launcher, items, true);
        mAdapter.setUser(currentUser);
        mAdapter.setSeriesWinner(seriesWinner);
    }

    @NonNull
    @Override
    protected SeriesMatchAdapter createAdapter() {
        return new SeriesMatchAdapter(mItems, mLauncher);
    }
}
