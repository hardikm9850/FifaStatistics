package com.example.kevin.fifastatistics.models.notifications.notificationrequestbodies;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.ResourceUtils;

/**
 * Model of an "Decline Friend Request" notification body
 */
public class DeclineFriendRequestBody extends FriendBody{

    public static final String NOTIFICATION_TAG = "DECLINE_FRIEND_REQUEST";

    public DeclineFriendRequestBody(User user, String registrationTokenOfReceiver) {
        super(user, registrationTokenOfReceiver, buildNotificationBody(user), NOTIFICATION_TAG);
    }

    private static String buildNotificationBody(User user) {
        return user.getName() + " " + ResourceUtils.getStringFromResourceId(R.string.declined_your_friend_request);
    }
}
