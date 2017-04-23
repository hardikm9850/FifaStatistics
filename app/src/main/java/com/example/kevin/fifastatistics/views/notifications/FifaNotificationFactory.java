package com.example.kevin.fifastatistics.views.notifications;

import android.content.Context;

import java.util.Map;

public class FifaNotificationFactory {

    private static final String NEW_MATCH = "NEW_MATCH";
    private static final String NEW_SERIES = "NEW_SERIES";

    public static FifaNotification createNotification(Context context, Map<String, String> data) {
        if (data != null) {
            String tag = data.get("tag");
            if (tag != null) {
                return getNotificationForTag(tag, context, data);
            }
        }
        return new NullNotification(context);
    }

    private static FifaNotification getNotificationForTag(String tag, Context context, Map<String, String> data) {
        switch (tag) {
            case NEW_MATCH:
                return new MatchNotification(context, data);
            case NEW_SERIES:
                return new SeriesNotification(context, data);
            default:
                return new NullNotification(context);
        }
    }
}
