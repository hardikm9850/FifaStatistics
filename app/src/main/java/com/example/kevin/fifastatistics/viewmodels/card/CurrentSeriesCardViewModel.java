package com.example.kevin.fifastatistics.viewmodels.card;

import android.support.annotation.NonNull;

import com.example.kevin.fifastatistics.adapters.CurrentSeriesAdapter;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.match.CurrentSeries;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;

import java.util.List;

public class CurrentSeriesCardViewModel extends RecyclerCardViewModel<CurrentSeries, CurrentSeriesAdapter> {

    private Player mCurrentUser;

    public CurrentSeriesCardViewModel(ActivityLauncher launcher, List<CurrentSeries> series, boolean isCurrentUser, Player currentUser) {
        super(launcher, series, isCurrentUser);
        mCurrentUser = currentUser;
        mAdapter.setCurrentUser(currentUser);
    }

    @NonNull
    @Override
    protected CurrentSeriesAdapter createAdapter() {
        return new CurrentSeriesAdapter(mItems, mLauncher, mCurrentUser);
    }

    public void removeSeriesWithOpponentId(String opponentId) {
        if (mItems != null) {
            for (CurrentSeries s : mItems) {
                if (s.getOpponentId().equals(opponentId)) {
                    removeItem(s);
                }
            }
        }
    }
}
