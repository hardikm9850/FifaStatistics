package com.example.kevin.fifastatistics.utils;

public class MinuteFormatter {

    public static String format(int minute, int injuryTime) {
        String min = String.valueOf(minute) + "'";
        if (injuryTime > 0) {
            min += " + " + String.valueOf(injuryTime) + "'";
        }
        return min;
    }
}
