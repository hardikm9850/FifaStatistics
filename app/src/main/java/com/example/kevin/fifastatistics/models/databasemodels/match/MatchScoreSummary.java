package com.example.kevin.fifastatistics.models.databasemodels.match;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchScoreSummary implements Serializable {

    private PartSummary firstHalf;
    private PartSummary secondHalf;
    private PartSummary firstExtraTime;
    private PartSummary secondExtraTime;
    private PartSummary penalties;
    private PartSummary fullTime;

    @Getter
    @Setter
    public static class PartSummary implements Serializable {
        private int goalsFor;
        private int goalsAgainst;
    }
}
