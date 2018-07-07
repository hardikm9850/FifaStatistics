package com.example.kevin.fifastatistics.models.databasemodels.match;

import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.utils.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class CurrentSeries extends DatabaseModel {

    private String id;
    private String creatorId;
    private Friend opponent;
    private Team creatorTeam;
    private Team opponentTeam;
    private List<Match> matches;

    public CurrentSeries(String creatorId, Friend opponent, List<Match> matches, Team creatorTeam, Team opponentTeam) {
        this();
        this.creatorId = creatorId;
        this.opponent = opponent;
        this.matches = matches;
        this.creatorTeam = creatorTeam;
        this.opponentTeam = opponentTeam;
    }

    @JsonCreator
    public CurrentSeries() {
        matches = new ArrayList<>();
    }

    @JsonIgnore
    public String getOpponentId() {
        return opponent != null ? opponent.getId() : null;
    }

    @JsonIgnore
    public String getScore(Player currentUser) {
        if (!CollectionUtils.isEmpty(matches)) {
            int wins = 0;
            int losses = 0;
            for (Match match : matches) {
                if (match.wonBy(currentUser)) {
                    wins++;
                } else {
                    losses++;
                }
            }
            return wins + " - " + losses;
        } else {
            return "0 - 0";
        }
    }
}
