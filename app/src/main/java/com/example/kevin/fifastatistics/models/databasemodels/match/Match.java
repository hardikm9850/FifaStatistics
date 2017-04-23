package com.example.kevin.fifastatistics.models.databasemodels.match;

import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.models.databasemodels.user.Stats;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.SerializationUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = Match.MatchBuilder.class)
@Builder
@Getter
public class Match extends DatabaseModel {

    private final User.StatsPair stats;
    private final Penalties penalties;
    private final Date date;
    private final Friend winner;
    private final Friend loser;
    private String id;
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

    public boolean didWin(Player player) {
        return player.getId().equals(winner.getId());
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

    @Override
    public String toString() {
        return SerializationUtils.toJson(this);
    }
}
