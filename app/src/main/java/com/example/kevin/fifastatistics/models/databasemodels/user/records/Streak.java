package com.example.kevin.fifastatistics.models.databasemodels.user.records;

import java.io.Serializable;

import lombok.Getter;

@Getter
public class Streak implements Serializable {

    private static final String NO_VALUE_STREAK = "-";

    /**
     * The two types of streaks available.
     */
    public enum Type { WINNING, LOSING };

    private int value;
    private Type type;

    /**
     * Returns a string starting with 'L' or 'W' if this is a Winning or Losing streak, followed
     * by the number of consecutive wins or losses. If the current streak has no value, then '-' is
     * returned.
     * <br>
     * For example, a winning streak of 12 would return "W12".
     */
    @Override
    public String toString() {
        return value == 0 ? NO_VALUE_STREAK : type.name().substring(0, 1) + value;
    }
}
