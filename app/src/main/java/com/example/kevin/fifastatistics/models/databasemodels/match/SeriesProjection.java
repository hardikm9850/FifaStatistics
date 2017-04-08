package com.example.kevin.fifastatistics.models.databasemodels.match;

import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeriesProjection extends DatabaseModel implements FifaEvent {

    private String id;
    private String winnerId;
    private String winnerName;
    private String teamWinnerImageUrl;
    private String teamLoserImageUrl;
    private String loserId;
    private String loserName;
    private Date date;
    private int matchesPlayed;
    private int bestOf;
    private int matchesWinner;
    private int matchesLoser;
}
