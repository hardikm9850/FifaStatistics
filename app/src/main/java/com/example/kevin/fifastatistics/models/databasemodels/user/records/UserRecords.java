package com.example.kevin.fifastatistics.models.databasemodels.user.records;

import com.example.kevin.fifastatistics.models.databasemodels.match.Result;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    private static final String NO_STREAK = "-";

    @JsonDeserialize(as=OverallRecord.class)
    @Getter private Record overallRecord;

    @JsonDeserialize(as=CircularRecord.class)
    @Getter private Record lastTenRecord;

    private Streak currentStreak;

    public static UserRecords emptyRecords() {
        return new UserRecords(OverallRecord.emptyRecord(), CircularRecord.emptyRecord(10), null);
    }

    public void addResult(Result result) {
        overallRecord.addResult(result);
        lastTenRecord.addResult(result);
        updateStreak(result);
    }

    @JsonIgnore
    public String getStreak() {
        return (currentStreak == null) ? NO_STREAK : currentStreak.toString();
    }

    /**
     * Updates the Record's current streak, based on the outcome of the Match or Series.
     * @param result    the result of the Match or Series
     */
    private void updateStreak(Result result) {
        if (result == Result.WIN) {
            if (currentStreak == null || currentStreak == Streak.LOSING) {
                currentStreak = Streak.WINNING;
                currentStreak.initializeValue();
            } else {
                currentStreak.incrementValue();
            }
        } else {
            if (currentStreak == null || currentStreak == Streak.WINNING) {
                currentStreak = Streak.LOSING;
                currentStreak.initializeValue();
            } else {
                currentStreak.incrementValue();
            }
        }
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Getter
    public enum Streak {

        WINNING, LOSING;

        private int value;

        public void incrementValue() {
            value++;
        }

        public void initializeValue() {
            value = 1;
        }

        /**
         * Returns a string starting with 'L' or 'W' if this is a Winning or Losing streak, followed
         * by the number of consecutive wins or losses.
         * <br>
         * For example, a winning streak of 12 would return "W12".
         */
        @Override
        public String toString() {
            return name().substring(0,1) + value;
        }
    }
}
