package com.example.kevin.fifastatistics.models.notifications.notificationrequestbodies;

import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.SerializationUtils;

import lombok.Getter;

/**
 * Model of a "Send Friend Rquest" notification request body.
 */
public class FriendRequestBody extends NotificationRequestBody {

    private static final String NOTIFICATION_TAG = "FRIEND_REQUEST";

    @Getter private final Data data;

    public FriendRequestBody(User user, String registrationTokenOfReceiver) {
        super(new Notification(buildNotificationBody(user), ""), registrationTokenOfReceiver);
        data = Data.fromUser(user);
    }

    private static String buildNotificationBody(User user) {
        return "Friend request from " + user.getName();
    }

    @Getter
    public static class Data implements NotificationData {
        private final String tag;
        private String name;
        private String id;
        private String imageUrl;
        private String registrationToken;
        private int level;

        public static Data fromUser(User user) {
            Data data = new Data();
            data.name = user.getName();
            data.id = user.getId();
            data.imageUrl = user.getImageUrl();
            data.registrationToken = user.getRegistrationToken();
            data.level = user.getLevel();

            return data;
        }

        private Data() {
            tag = NOTIFICATION_TAG;
        }
    }

    @Override
    public String toString() {
        return SerializationUtils.toFormattedJson(this);
    }
}
