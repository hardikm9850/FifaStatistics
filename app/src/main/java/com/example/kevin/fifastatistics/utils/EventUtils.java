package com.example.kevin.fifastatistics.utils;

import com.example.kevin.fifastatistics.models.databasemodels.match.FifaEvent;

import java.util.Calendar;

public class EventUtils {

    private static final Calendar CAL1 = Calendar.getInstance();
    private static final Calendar CAL2 = Calendar.getInstance();

    public static boolean isEventsOnSameDay(FifaEvent event1, FifaEvent event2) {
        if (event1 == null || event2 == null) {
            return false;
        } else {
            CAL1.setTime(event1.getDate());
            CAL2.setTime(event2.getDate());
            return CAL1.get(Calendar.YEAR) == CAL2.get(Calendar.YEAR) &&
                    CAL1.get(Calendar.DAY_OF_YEAR) == CAL2.get(Calendar.DAY_OF_YEAR);
        }
    }
}
