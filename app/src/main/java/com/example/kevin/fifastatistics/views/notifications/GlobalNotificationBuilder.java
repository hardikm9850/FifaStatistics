package com.example.kevin.fifastatistics.views.notifications;

import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 * Provides a single instance of a notification builder.
 */
public class GlobalNotificationBuilder
{
    private static NotificationCompat.Builder builder;

    /**
     * Use a global notification builder for all notifications. This allows
     * for updating of notifications from seperate classes.
     */
    public static NotificationCompat.Builder getInstance(Context context)
    {
        if (builder == null) {
            builder = new NotificationCompat.Builder(context);
        }
        return builder;
    }
}
