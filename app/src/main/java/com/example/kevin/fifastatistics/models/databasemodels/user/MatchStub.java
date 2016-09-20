package com.example.kevin.fifastatistics.models.databasemodels.user;

import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.Penalties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Getter;

/**
 * The MatchStub class is meant to act as a 'stub' of a Match, and should exist
 * only within instances of a User or Series object.
 * It is used as the matches
 * associated with a user or Series object, and the related full Match object can be
 * accessed through the 'matchId' property.
 */
@JsonDeserialize(builder = MatchStub.MatchStubBuilder.class)
@Builder
@Getter
public class MatchStub {

    private String id;
    private String winnerId;
    private String date;
    private int goalsWinner;
    private int goalsLoser;
    private Penalties penalties;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class MatchStubBuilder {
    }

    public static MatchStub fromMatch(Match match) {
        return MatchStub.builder()
                .id(match.getId())
                .winnerId(match.getWinner().getId())
                .goalsWinner(match.getStats().getStatsFor().getGoals())
                .goalsLoser(match.getStats().getStatsAgainst().getGoals())
                .penalties(match.getPenalties())
                .build();
    }
}
