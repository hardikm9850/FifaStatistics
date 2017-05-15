package com.example.kevin.fifastatistics.interfaces;

import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;

public interface OnSeriesUpdatedListener {

    void onSeriesUpdated(Series series);
    void onUserTeamUpdated(Team team);
    void onOpponentTeamUpdated(Team team);
}
