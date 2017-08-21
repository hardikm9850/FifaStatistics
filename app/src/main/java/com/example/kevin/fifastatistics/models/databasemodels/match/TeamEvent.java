package com.example.kevin.fifastatistics.models.databasemodels.match;

import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;

public interface TeamEvent {
    Team getTeamWinner();
    Team getTeamLoser();
    Friend getWinner();
    Friend getLoser();
}
