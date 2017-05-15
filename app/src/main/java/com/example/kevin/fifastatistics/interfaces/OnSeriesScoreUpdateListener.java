package com.example.kevin.fifastatistics.interfaces;

import com.example.kevin.fifastatistics.models.databasemodels.league.Team;

public interface OnSeriesScoreUpdateListener {

    void onUserScoreUpdate(int oldScore, int newScore);
    void onOpponentScoreUpdate(int oldScore, int newScore);
    void onUserTeamUpdated(Team team);
    void onOpponentTeamUpdated(Team team);
}
