package com.example.kevin.fifastatistics.models.databasemodels.match;

import android.support.v4.util.Pair;

import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;
import com.example.kevin.fifastatistics.models.databasemodels.footballers.MatchEvents;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.models.databasemodels.user.Stats;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.SerializationUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import rx.functions.Func1;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = Match.MatchBuilder.class)
@Builder
@Getter
public class Match extends DatabaseModel implements TeamEvent, FifaEvent, PenaltyEvent {

    public static final int MAX_SECOND_HALF_MINUTE = 90;
    public static final int MAX_MINUTE = 120;

    private Date date;
    private Friend winner;
    private Friend loser;
    private String id;
    private String updateId;
    private String seriesId;
    private MatchScoreSummary summary;
    @Setter private MatchEvents events;
    @Setter private Penalties penalties;
    @Setter private User.StatsPair stats;
    @Setter private Team teamWinner;
    @Setter private Team teamLoser;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPOJOBuilder(withPrefix = "")
    public static final class MatchBuilder {
    }

    public static Match fromMatchWithId(Match match, String id) {
        match.id = id;
        return match;
    }

    public static Match swapStats(Match match) {
        User.StatsPair newStats = new User.StatsPair(match.getStats().getStatsAgainst(), match.getStats().getStatsFor());
        return Match.builder()
                .id(match.id)
                .stats(newStats)
                .penalties(match.penalties)
                .date(match.date)
                .winner(match.winner)
                .loser(match.loser)
                .build();
    }

    public static Match empty() {
        Match match = Match.builder().build();
        match.stats = new User.StatsPair();
        match.penalties = new Penalties();
        match.date = new Date();
        match.winner = Friend.builder().build();
        match.loser = Friend.builder().build();
        return match;
    }

    public boolean didWin(Player player) {
        return player.getId().equals(winner.getId());
    }

    public boolean didLose(Player player) {
        return player.getId().equals(loser.getId());
    }

    /**
     * Return the score of the match in the format '3 - 2', with the score of player specified first.
     */
    public String getMatchScore(String playerId) {
        if (playerId.equals(winner.getId())) {
            return Math.round(stats.getStatsFor().getGoals()) + " - " + Math.round(stats.getStatsAgainst().getGoals());
        } else {
            return Math.round(stats.getStatsAgainst().getGoals()) + " - " + Math.round(stats.getStatsFor().getGoals());
        }
    }

    public String getPenaltiesScore(String playerId) {
        if (penalties != null) {
            if (playerId.equals(winner.getId())) {
                return penalties.getWinner() + " - " + penalties.getLoser();
            } else {
                return penalties.getLoser() + " - " + penalties.getWinner();
            }
        } else {
            return "";
        }
    }

    public Stats getStatsFor() {
        return stats.getStatsFor();
    }

    public Stats getStatsAgainst() {
        return stats.getStatsAgainst();
    }

    @JsonIgnore
    @Override
    public String getLoserName() {
        return loser.getName();
    }

    @JsonIgnore
    @Override
    public String getWinnerName() {
        return winner.getName();
    }

    @JsonIgnore
    public String getLoserFirstName() {
        return loser.getName() != null ? loser.getName().split(" ")[0] : "";
    }

    @JsonIgnore
    public String getWinnerFirstName() {
        return winner.getName() != null ? winner.getName().split(" ")[0] : "";
    }

    @JsonIgnore
    @Override
    public String getTeamWinnerImageUrl() {
        return teamWinner.getCrestUrl();
    }

    @JsonIgnore
    @Override
    public String getTeamLoserImageUrl() {
        return teamLoser.getCrestUrl();
    }

    @JsonIgnore
    @Override
    public String getWinnerId() {
        return winner.getId();
    }

    @JsonIgnore
    @Override
    public String getLoserId() {
        return loser.getId();
    }

    @JsonIgnore
    @Override
    public int getScoreWinner() {
        return Math.round(getStatsFor().getGoals());
    }

    @JsonIgnore
    @Override
    public int getScoreLoser() {
        return Math.round(getStatsAgainst().getGoals());
    }

    @JsonIgnore
    public Team getTeamFor(Player player) {
        if (player != null) {
            if (didWin(player)) {
                return teamWinner;
            } else if (didLose(player)) {
                return teamLoser;
            }
        }
        return null;
    }

    @JsonIgnore
    public Pair<Integer, Integer> getGoalsPair() {
        return getPairFor(Stats::getGoals);
    }

    @JsonIgnore
    public Pair<Integer, Integer> getCardsPair() {
        return getPairFor(s -> s.getYellowCards() + s.getRedCards());
    }

    @JsonIgnore
    public Pair<Integer, Integer> getInjuriesPair() {
        return getPairFor(Stats::getInjuries);
    }

    @JsonIgnore
    private Pair<Integer, Integer> getPairFor(Func1<Stats, Float> func1) {
        if (stats != null && stats.getStatsFor() != null && stats.getStatsAgainst() != null) {
            Stats statsFor = stats.getStatsFor();
            Stats statsAgainst = stats.getStatsAgainst();
            return Pair.create(Math.round(func1.call(statsFor)), Math.round(func1.call(statsAgainst)));
        } else {
            return Pair.create(0, 0);
        }
    }

    @JsonIgnore
    public void swapWinnerAndLoser() {
        Team tempTeam = teamWinner;
        teamWinner = teamLoser;
        teamLoser = tempTeam;
        Friend tempFriend = winner;
        winner = loser;
        loser = tempFriend;
    }

    @JsonIgnore
    public void swapStats() {
        stats = new User.StatsPair(stats.getStatsAgainst(), stats.getStatsFor());
        if (penalties != null) {
            penalties.swap();
        }
    }

    @JsonIgnore
    public boolean hasPenalties() {
        return penalties != null;
    }

    @JsonIgnore
    public void setPenaltiesFor(int goals) {
        initPenalties();
        penalties.setWinner(goals);
    }

    @JsonIgnore
    public void setPenaltiesAgainst(int goals) {
        initPenalties();
        penalties.setLoser(goals);
    }

    private void initPenalties() {
        if (penalties == null) {
            penalties = new Penalties();
        }
    }

    public int getPenaltiesFor() {
        return penalties != null ? penalties.getWinner() : 0;
    }

    public int getPenaltiesAgainst() {
        return penalties != null ? penalties.getLoser() : 0;
    }

    @JsonIgnore
    public List<MatchEvents.GoalItem> getGoals() {
        return events != null ? events.getGoals() : null;
    }

    @JsonIgnore
    public List<MatchEvents.InjuryItem> getInjuries() {
        return events != null ? events.getInjuries() : null;
    }

    @JsonIgnore
    public List<MatchEvents.CardItem> getCards() {
        return events != null ? events.getCards() : null;
    }

    @Override
    public String toString() {
        return SerializationUtils.toJson(this);
    }
}
