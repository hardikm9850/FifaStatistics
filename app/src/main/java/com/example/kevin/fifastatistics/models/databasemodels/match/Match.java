package com.example.kevin.fifastatistics.models.databasemodels.match;

import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.SerializationUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Date;

import lombok.Builder;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = Match.MatchBuilder.class)
@Builder
@Getter
public class Match extends DatabaseModel {

    private String id;
    private final User.StatsPair stats;
    private final Penalties penalties;
    private final Date date;

    private final Friend winner;
    private final Friend loser;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPOJOBuilder(withPrefix = "")
    public static final class MatchBuilder {
    }

    public static Match fromMatchWithId(Match match, String id) {
        match.id = id;
        return match;
    }

    public boolean didWin(Player player) {
        return player.getId().equals(winner.getId());
    }

    /**
     * Return the score of the match in the format '3 - 2', with the score of player specified first.
     */
    public String getMatchScore(String playerId) {
        if (playerId.equals(winner.getId())) {
            return stats.getStatsFor().getGoals() + " - " + stats.getStatsAgainst().getGoals();
        } else {
            return stats.getStatsAgainst().getGoals() + " - " + stats.getStatsFor().getGoals();
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

    @Override
    public String toString() {
        return SerializationUtils.toJson(this);
    }
}
