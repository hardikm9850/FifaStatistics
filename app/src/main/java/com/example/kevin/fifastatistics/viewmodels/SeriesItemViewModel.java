package com.example.kevin.fifastatistics.viewmodels;

import com.example.kevin.fifastatistics.models.databasemodels.match.SeriesProjection;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;

public class SeriesItemViewModel extends EventViewModel<SeriesProjection> {

    public SeriesItemViewModel(SeriesProjection match, Player user) {
        super(match, user);
    }

    @Override
    protected int getScoreWinner() {
        return mEvent.getMatchesWinner();
    }

    @Override
    protected int getScoreLoser() {
        return mEvent.getMatchesLoser();
    }

    @Override
    public void openEventDetail() {

    }
}
