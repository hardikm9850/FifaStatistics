package com.example.kevin.fifastatistics.models.databasemodels.match;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchScoreSummary implements Serializable {

    private static final String ZERO = "ZERO";

    private PartSummary firstHalf;
    private PartSummary secondHalf;
    private PartSummary firstExtraTime;
    private PartSummary secondExtraTime;
    private PartSummary penalties;
    private PartSummary fullTime;
    
    public TeamSummary buildSummaryFor() {
        return new TeamSummary(getFor(firstHalf), getFor(secondHalf), getFor(firstExtraTime),
                getFor(secondExtraTime), getFor(penalties), getFor(fullTime));
    }
    
    public TeamSummary buildSummaryAgainst() {
        return new TeamSummary(getAgainst(firstHalf), getAgainst(secondHalf), getAgainst(firstExtraTime),
                getAgainst(secondExtraTime), getAgainst(penalties), getAgainst(fullTime));
    }
    
    private String getFor(PartSummary partSummary) {
        return partSummary != null ? String.valueOf(partSummary.goalsFor) : ZERO;
    }

    private String getAgainst(PartSummary partSummary) {
        return partSummary != null ? String.valueOf(partSummary.goalsAgainst) : ZERO;
    }

    @Getter
    @Setter
    public static class PartSummary implements Serializable {
        private int goalsFor;
        private int goalsAgainst;
    }

    @AllArgsConstructor
    public static class TeamSummary implements Serializable {

        public final String firstHalf;
        public final String secondHalf;
        public final String firstExtraTime;
        public final String secondExtraTime;
        public final String penalties;
        public final String fullTime;

        public TeamSummary() {
            this(ZERO, ZERO, ZERO, ZERO, ZERO, ZERO);
        }
    }
}
