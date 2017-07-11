package com.example.kevin.fifastatistics.models.databasemodels.match;

import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchProjection extends DatabaseModel implements FifaEvent, PenaltyEvent {

    private String id;
    private String winnerId;
    private String winnerName;
    private String teamWinnerImageUrl;
    private String teamLoserImageUrl;
    private String loserId;
    private String loserName;
    private Penalties penalties;
    private Date date;
    private int goalsWinner;
    private int goalsLoser;

    public boolean didPlayerWin(Player player) {
        return winnerId.equals(player.getId());
    }

    public boolean didPlayerLose(Player player) {
        return loserId.equals(player.getId());
    }

    @JsonIgnore
    @Override
    public int getScoreWinner() {
        return goalsWinner;
    }

    @JsonIgnore
    @Override
    public int getScoreLoser() {
        return goalsLoser;
    }

    @JsonIgnore
    public String getWinnerFirstName() {
        return winnerName.split(" ")[0];
    }
}
