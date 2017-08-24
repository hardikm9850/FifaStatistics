package com.example.kevin.fifastatistics.viewmodels.card;

import android.support.annotation.NonNull;

import com.example.kevin.fifastatistics.adapters.MatchEventCardAdapter;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;

import java.util.List;

import static com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents.MatchEventItem;

public class MatchEventsCardViewModel<T extends MatchEventItem>
        extends RecyclerCardViewModel<T, MatchEventCardAdapter<T>> {

    private Match mMatch;

    public MatchEventsCardViewModel(ActivityLauncher launcher) {
        this(launcher, null, null);
    }

    public MatchEventsCardViewModel(ActivityLauncher launcher, List<T> items, Match match) {
        super(launcher, items, true);
        mMatch = match;
    }

    public void setMatch(Match match, List<T> items) {
        mMatch = match;
        mAdapter.setMatch(match);
        setItems(items);
    }

    @NonNull
    @Override
    protected MatchEventCardAdapter<T> createAdapter() {
        return new MatchEventCardAdapter<>(mItems, mLauncher, mMatch);
    }
}
