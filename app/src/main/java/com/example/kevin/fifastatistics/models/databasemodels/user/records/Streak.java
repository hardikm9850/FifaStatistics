package com.example.kevin.fifastatistics.models.databasemodels.user.records;

import com.example.kevin.fifastatistics.models.databasemodels.match.Result;

import lombok.Getter;

@Getter
public class Streak {

    private static final String NO_VALUE_STREAK = "-";

    /**
     * The two types of streaks available.
     */
    public enum Type { WINNING, LOSING };

    private int value;
    private Type type;

    public Streak() {
        type = Type.WINNING;
    }

    /**
     * Update the streak's value and type given the result.
     * @param result    the match or series result
     */
    public void update(Result result) {
        value++;
        if (result == Result.WIN && type == Type.LOSING) {
            type = Type.WINNING;
            value = 1;
        } else if (result == Result.LOSS && type == Type.WINNING){
            type = Type.LOSING;
            value = 1;
        }
    }

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
