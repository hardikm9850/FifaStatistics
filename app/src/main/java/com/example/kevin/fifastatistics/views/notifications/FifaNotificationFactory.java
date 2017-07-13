package com.example.kevin.fifastatistics.views.notifications;

import android.content.Context;

import java.util.Map;

public class FifaNotificationFactory {

    public static final String NEW_MATCH = "NEW_MATCH";
    public static final String NEW_SERIES = "NEW_SERIES";
    public static final String UPDATE_MATCH = "UPDATE_MATCH";
    public static final String ACCEPT_UPDATE = "ACCEPT_UPDATE";
    public static final String DECLINE_UPDATE = "DECLINE_UPDATE";

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
            case UPDATE_MATCH:
                return new UpdateCreatedNotification(context, data);
            case ACCEPT_UPDATE:
            case DECLINE_UPDATE:
                return new UpdateHandledNotification(context, data);
            default:
                return new NullNotification(context);
        }
    }
}
