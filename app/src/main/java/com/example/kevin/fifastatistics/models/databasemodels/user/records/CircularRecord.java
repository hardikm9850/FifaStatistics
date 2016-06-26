package com.example.kevin.fifastatistics.models.databasemodels.user.records;

import com.example.kevin.fifastatistics.models.databasemodels.match.Result;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayDeque;
import java.util.Deque;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * An implementation of Record that contains the wins and losses of the most recent results.
 * The number of results to keep track of is defined by <code>resultCount</code>.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE, onConstructor=@__(@JsonCreator))
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY)
public class CircularRecord implements Record {

    @JsonIgnore private static final int DEFAULT_RESULT_COUNT_SIZE = 10;

    private Deque<Result> recentResults;
    @JsonIgnore private int resultCount;
    @Getter private int wins;
    @Getter private int losses;

    public static CircularRecord emptyRecord() {
        return emptyRecord(DEFAULT_RESULT_COUNT_SIZE);
    }

    public static CircularRecord emptyRecord(int resultCount) {
        return new CircularRecord(new ArrayDeque<>(), resultCount, 0, 0);
    }

    @Override
    public void addResult(Result result) {
        recentResults.add(result);
        if (result == Result.WIN) {
            wins++;
        } else {
            losses++;
        }
        removeOverflowingResult();
    }

    private void removeOverflowingResult() {
        if (recentResults.size() > resultCount) {
            Result removedResult = recentResults.remove();
            if (removedResult == Result.WIN) {
                wins--;
            } else {
                losses--;
            }
        }
    }

    @Override
    public String toString() {
        return wins + "-" + losses;
    }
}
