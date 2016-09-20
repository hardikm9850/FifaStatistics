package com.example.kevin.fifastatistics.models.databasemodels.user.records;

import com.example.kevin.fifastatistics.models.databasemodels.match.Result;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Defines the record for a user (overall wins/losses, last ten wins/losses, current streak)
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE, onConstructor=@__(@JsonCreator))
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY)
public class UserRecords {

    @JsonDeserialize(as=OverallRecord.class)
    @Getter private Record overallRecord;

    @JsonDeserialize(as=CircularRecord.class)
    @Getter private Record lastTenRecord;

    private Streak currentStreak;

    public static UserRecords emptyRecords() {
        return new UserRecords(
                OverallRecord.emptyRecord(),
                CircularRecord.emptyRecord(10),
                new Streak());
    }

    @JsonIgnore
    public String getStreak() {
        return currentStreak.toString();
    }

    @JsonIgnore
    public int getTotalCount() {
        return overallRecord.getWins() + overallRecord.getLosses();
    }

    public void addResult(Result result) {
        overallRecord.addResult(result);
        lastTenRecord.addResult(result);
        currentStreak.update(result);
    }
}
