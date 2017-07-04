package com.example.kevin.fifastatistics.models.databasemodels.match;

import com.example.kevin.fifastatistics.models.databasemodels.league.Team;

public interface TeamEvent {
    Team getTeamWinner();
    Team getTeamLoser();
}
