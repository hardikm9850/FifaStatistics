package com.example.kevin.fifastatistics.models.databasemodels.user;

import com.example.kevin.fifastatistics.models.databasemodels.DatabaseModel;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;

@Getter
public class SeriesStub extends DatabaseModel {

    private String id;
    private String opponentName;
    private String date;
    private List<MatchSummary> matches;
    private boolean didWin;

    @Getter
    public static class MatchSummary implements Serializable {

        private int goalsUser;
        private int goalsOpponent;
        private boolean didWin;
    }

}
