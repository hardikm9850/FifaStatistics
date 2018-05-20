package com.example.kevin.fifastatistics.models.databasemodels.user.records;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Defines the record for a user (overall wins/losses, last ten wins/losses, current streak)
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE, onConstructor=@__(@JsonCreator))
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY)
public class UserRecords implements Serializable {

    @Getter private Record overallRecord;
    @Getter private Record lastTenRecord;

    private Streak currentStreak;

    @JsonIgnore
    public String getStreak() {
        return currentStreak.toString();
    }

    @JsonIgnore
    public int getEventCount() {
        if (overallRecord != null) {
            return overallRecord.getWins() + overallRecord.getLosses();
        } else {
            return 0;
        }
    }
}
