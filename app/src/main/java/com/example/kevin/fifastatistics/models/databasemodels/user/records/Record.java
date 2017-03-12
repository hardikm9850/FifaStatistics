package com.example.kevin.fifastatistics.models.databasemodels.user.records;

import com.example.kevin.fifastatistics.models.databasemodels.match.Result;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;

/**
 * A win-loss pair record.
 */
@Getter
public class Record implements Serializable {

    private List<Result> recentResults;
    private int wins;
    private int losses;

    @Override
    public String toString() {
        return wins + "-" + losses;
    }
}
