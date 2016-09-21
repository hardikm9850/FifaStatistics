package com.example.kevin.fifastatistics.views.notifications;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.kevin.fifastatistics.models.notifications.notificationrequestbodies.AcceptFriendRequestBody;
import com.example.kevin.fifastatistics.models.notifications.notificationrequestbodies.DeclineFriendRequestBody;
import com.example.kevin.fifastatistics.models.notifications.notificationrequestbodies.NewMatchBody;
import com.example.kevin.fifastatistics.models.notifications.notificationrequestbodies.SendFriendRequestBody;
import com.example.kevin.fifastatistics.views.notifications.friendrequestnotification.AcceptRequestNotification;
import com.example.kevin.fifastatistics.views.notifications.friendrequestnotification.DeclineRequestNotification;
import com.example.kevin.fifastatistics.views.notifications.friendrequestnotification.FriendRequestNotification;

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
            case SendFriendRequestBody.NOTIFICATION_TAG :
                return new FriendRequestNotification(context, bundle);
            case AcceptFriendRequestBody.NOTIFICATION_TAG :
                return new AcceptRequestNotification(context, bundle);
            case DeclineFriendRequestBody.NOTIFICATION_TAG :
                return new DeclineRequestNotification(context, bundle);
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
