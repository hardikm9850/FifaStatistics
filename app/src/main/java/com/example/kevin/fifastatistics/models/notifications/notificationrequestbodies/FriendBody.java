package com.example.kevin.fifastatistics.models.notifications.notificationrequestbodies;

import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.SerializationUtils;

import lombok.Getter;

/**
 * Abstract class defining notifications that have data containing properties of a friend.
 */
public abstract class FriendBody extends NotificationRequestBody {

    @Getter
    private final Data data;

    public FriendBody(User user, String registrationTokenOfReceiver, String bodyText,
                      String notificationTag) {
        super(new NotificationRequestBody.Notification(bodyText, ""), registrationTokenOfReceiver);
        data = Data.fromUserAndTag(user, notificationTag);
    }

    @Getter
    public static class Data implements NotificationRequestBody.NotificationData {
        private final String tag;
        private String name;
        private String id;
        private String imageUrl;
        private String registrationToken;
        private int level;

        public static Data fromUserAndTag(User user, String notificationTag) {
            Data data = new Data(notificationTag);
            data.name = user.getName();
            data.id = user.getId();
            data.imageUrl = user.getImageUrl();
            data.registrationToken = user.getRegistrationToken();
            data.level = user.getLevel();

            return data;
        }

        private Data(String notificationTag) {
            tag = notificationTag;
        }
    }

    @Override
    public String toString() {
        return SerializationUtils.toFormattedJson(this);
    }
}
