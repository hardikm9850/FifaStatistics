package com.example.kevin.fifastatistics.models.databasemodels.user.records;

import com.example.kevin.fifastatistics.models.databasemodels.match.Result;

/**
 * A win-loss pair record.
 */
public interface Record {

    /**
     * Get the number of wins present in the record.
     * @return the number of wins
     */
    int getWins();

    /**
     * Get the number of losses present in the record.
     * @return the number of losses
     */
    int getLosses();

    /**
     * Add a result to the record
     * @param result the result
     */
    void addResult(Result result);
}
