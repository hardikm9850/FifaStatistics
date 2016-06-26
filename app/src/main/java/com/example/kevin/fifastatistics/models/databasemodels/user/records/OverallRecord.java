package com.example.kevin.fifastatistics.models.databasemodels.user.records;

import com.example.kevin.fifastatistics.models.databasemodels.match.Result;
import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * An implementation of the Record interface, whose wins and losses always increment, with no limit.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE, onConstructor=@__(@JsonCreator))
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class OverallRecord implements Record {
    private int wins;
    private int losses;

    public static OverallRecord emptyRecord() {
        return new OverallRecord(0, 0);
    }

    @Override
    public void addResult(Result result) {
        if (result == Result.WIN) {
            wins++;
        } else {
            losses++;
        }
    }

    @Override
    public String toString() {
        return wins + "-" + losses;
    }
}
