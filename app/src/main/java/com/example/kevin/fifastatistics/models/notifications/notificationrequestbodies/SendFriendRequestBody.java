package com.example.kevin.fifastatistics.models.notifications.notificationrequestbodies;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.ResourceUtils;

/**
 * Model of a "Send Friend Request" notification request body.
 */
public class SendFriendRequestBody extends FriendBody {

    public static final String NOTIFICATION_TAG = "FRIEND_REQUEST";

    public SendFriendRequestBody(User user, String registrationTokenOfReceiver) {
        super(user, registrationTokenOfReceiver, buildNotificationBody(user), NOTIFICATION_TAG);
    }

    private static String buildNotificationBody(User user) {
        return ResourceUtils.getStringFromResourceId(R.string.friend_request_from) + " " + user.getName();
    }
}
