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

    public static MatchScoreSummary inverseOf(MatchScoreSummary summary) {
        if (summary == null) {
            return null;
        } else {
            MatchScoreSummary invertedSummary = new MatchScoreSummary();
            invertedSummary.firstHalf = PartSummary.inverse(summary.firstHalf);
            invertedSummary.secondHalf = PartSummary.inverse(summary.secondHalf);
            invertedSummary.firstExtraTime = PartSummary.inverse(summary.firstExtraTime);
            invertedSummary.secondExtraTime = PartSummary.inverse(summary.secondExtraTime);
            invertedSummary.penalties = PartSummary.inverse(summary.penalties);
            invertedSummary.fullTime = PartSummary.inverse(summary.fullTime);
            return invertedSummary;
        }
    }

    public static MatchScoreSummary zeroed() {
        MatchScoreSummary s = new MatchScoreSummary();
        s.firstHalf = new PartSummary();
        s.secondHalf = new PartSummary();
        s.firstExtraTime = new PartSummary();
        s.secondExtraTime = new PartSummary();
        s.penalties = new PartSummary();
        s.fullTime = new PartSummary();
        return s;
    }
    
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

        static PartSummary inverse(PartSummary summary) {
            PartSummary s = new PartSummary();
            if (summary == null) {
                return s;
            } else {
                s.goalsFor = summary.goalsAgainst;
                s.goalsAgainst = summary.goalsFor;
                return s;
            }
        }

        public TextPartSummary stringify() {
            return new TextPartSummary(this);
        }

        private int goalsFor;
        private int goalsAgainst;
    }

    public static class TextPartSummary implements Serializable {
        public final String goalsFor;
        public final String goalsAgainst;

        TextPartSummary(PartSummary summary) {
            if (summary != null) {
                goalsFor = String.valueOf(summary.getGoalsFor());
                goalsAgainst = String.valueOf(summary.getGoalsAgainst());
            } else {
                goalsFor = ZERO;
                goalsAgainst = ZERO;
            }
        }
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
