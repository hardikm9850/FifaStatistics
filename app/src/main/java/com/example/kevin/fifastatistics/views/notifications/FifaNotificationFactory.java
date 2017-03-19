package com.example.kevin.fifastatistics.views.notifications;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.kevin.fifastatistics.models.notifications.notificationrequestbodies.NewMatchBody;

public class FifaNotificationFactory
{
    private static final String DEFAULT_TAG = "default";

    /**
     * Creates a notification based on the 'Tag' field of the bundle.
     */
    public static FifaNotification createNotification(
            Context context, Bundle bundle)
    {
        String tag = getTag(bundle);
        Log.e("FACTORY", tag);
        switch (tag) {
            case NewMatchBody.NOTIFICATION_TAG :
                return new MatchNotification(context, bundle);
            default :
                return new NullNotification(context);
        }
    }

    private static String getTag(Bundle data)
    {
        try {
            return data.getString("tag");
        } catch (NullPointerException e) {
            return DEFAULT_TAG;
        }
    }
}
