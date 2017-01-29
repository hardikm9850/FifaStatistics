package com.example.kevin.fifastatistics.models.databasemodels.user;

import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SeriesStub extends DatabaseModel {

    private String id;
    private String opponentName;
    private String date;
    private List<MatchSummary> matches;
    private boolean didWin;

    @AllArgsConstructor
    @Getter
    public static class MatchSummary {

        private int goalsFor;
        private int goalsAgainst;
        private boolean didWin;
    }

}
