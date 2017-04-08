package com.example.kevin.fifastatistics.models.databasemodels.match;

import com.example.kevin.fifastatistics.models.databasemodels.user.Player;

import java.util.Date;

public interface FifaEvent {

    Date getDate();
    String getLoserName();
    String getWinnerName();
    String getTeamWinnerImageUrl();
    String getTeamLoserImageUrl();
    String getWinnerId();
    String getLoserId();
}
