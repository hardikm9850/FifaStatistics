package com.example.kevin.fifastatistics.models.databasemodels.user.records;

import com.example.kevin.fifastatistics.models.databasemodels.match.Result;

import java.util.List;

import lombok.Getter;

/**
 * A win-loss pair record.
 */
@Getter
public class Record {

    private List<Result> recentResults;
    private int wins;
    private int losses;

    @Override
    public String toString() {
        return wins + "-" + losses;
    }
}
