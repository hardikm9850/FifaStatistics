package com.example.kevin.fifastatistics.models.databasemodels.match;

import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.SerializationUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import lombok.Getter;

@Getter
public class Series extends DatabaseModel {

    public static final int DEFAULT_MAX_SERIES_LENGTH = 7;

    private String id;
    private Friend winner;
    private Friend loser;
    private Date date;
    private User.StatsPair totalStats;
    private User.StatsPair averageStats;
    private List<Match> matches;
    private int matchesPlayed;
    private int bestOf;

    @JsonIgnore private Friend playerOne;
    @JsonIgnore private Friend playerTwo;
    @JsonIgnore private int playerOneWins;
    @JsonIgnore private int playerTwoWins;
    @JsonIgnore private boolean isSeriesOver;

    public Series(Friend playerOne, Friend playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        matches = new ArrayList<>(DEFAULT_MAX_SERIES_LENGTH);
        totalStats = new User.StatsPair();
        averageStats = new User.StatsPair();
        date = new Date();
        bestOf = DEFAULT_MAX_SERIES_LENGTH;
    }

    public static Series fromSeriesWithId(Series series, String id) {
        series.id = id;
        return series;
    }

    @JsonCreator private Series() {}

    public void addAll(Collection<Match> matches) {
        for (Match m : matches) {
            addMatch(m);
        }
    }

    public void addMatch(Match match) {
        if (matchesPlayed < bestOf) {
            doAddMatch(match);
        }
    }

    private void doAddMatch(Match match) {
        matches.add(match);
        matchesPlayed++;
        updateStats(match);
        maybeEndSeries();
    }

    private void updateStats(Match match) {
        if (match.didWin(playerOne)) {
            playerOneWins++;
        } else {
            playerTwoWins++;
        }
    }

    private void maybeEndSeries() {
        if (didPlayerWin(playerOneWins)) {
            winner = playerOne;
            loser = playerTwo;
            isSeriesOver = true;
        } else if (didPlayerWin(playerTwoWins)) {
            winner = playerTwo;
            loser = playerOne;
            totalStats.swap();
            isSeriesOver = true;
        }
    }

    private boolean didPlayerWin(int playerWins) {
        return playerWins > bestOf / 2;
    }

    @Override
    public String toString() {
        return SerializationUtils.toJson(this);
    }
}
