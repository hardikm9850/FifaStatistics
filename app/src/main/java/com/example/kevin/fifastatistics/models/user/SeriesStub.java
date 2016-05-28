package com.example.kevin.fifastatistics.models.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * The SeriesStub class is meant to act as a 'stub' of a Series, and should exist
 * only within instances of a User object.
 * It is used as the series associated with a User object, and the related full Series object can
 * be accessed through the 'id' property.
 */
@JsonDeserialize(builder = SeriesStub.SeriesStubBuilder.class)
@Builder
@Getter
public class SeriesStub {

    private static final int MAX_GAMES_IN_SERIES = 7;

    private String id;
    private String opponentName;
    private String date;
    private ArrayList<MatchSummary> matches;
    private boolean didWin;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class SeriesStubBuilder {
    }

    /**
     * Adds a new Match Summary to the series.
     * @param goalsFor          The goals scored for the User in the match
     * @param goalsAgainst      The goals scored against the User in the match
     * @param didWin            Whether the user won or not
     */
    public void addMatchSummary(int goalsFor, int goalsAgainst, boolean didWin) {

        if (matches == null) {
            matches = new ArrayList<>();
        }
        else if (matches.size() == MAX_GAMES_IN_SERIES) {
            return;
        }

        matches.add(new MatchSummary(goalsFor, goalsAgainst, didWin));
    }

    /**
     * A short summary of each match in the series.
     */
    @AllArgsConstructor
    @Getter
    public static class MatchSummary {

        private int goalsFor;
        private int goalsAgainst;
        private boolean didWin;
    }


}
