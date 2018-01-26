package com.example.kevin.fifastatistics.models.databasemodels.match;

import com.fasterxml.jackson.annotation.JsonCreator;

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

    public MatchScoreSummary(MatchScoreSummary s) {
        firstHalf = new PartSummary(s.firstHalf);
        secondHalf = new PartSummary(s.secondHalf);
        firstExtraTime = new PartSummary(s.firstExtraTime);
        secondExtraTime = new PartSummary(s.secondExtraTime);
        penalties = new PartSummary(s.penalties);
        fullTime = new PartSummary(s.fullTime);
    }

    @JsonCreator
    public MatchScoreSummary() {}

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

        private int goalsFor;
        private int goalsAgainst;

        public PartSummary(PartSummary p) {
            if (p != null) {
                goalsFor = p.goalsFor;
                goalsAgainst = p.goalsAgainst;
            }
        }

        @JsonCreator
        public PartSummary() {}

        public TextPartSummary stringify() {
            return new TextPartSummary(this);
        }
    }

    public static class TextPartSummary implements Serializable {
        private static final String ZERO = "0";

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
}
