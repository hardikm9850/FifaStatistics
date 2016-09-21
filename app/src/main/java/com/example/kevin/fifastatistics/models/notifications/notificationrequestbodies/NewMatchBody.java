package com.example.kevin.fifastatistics.models.notifications.notificationrequestbodies;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.ResourceUtils;

import lombok.Getter;

public class NewMatchBody extends NotificationRequestBody {

    public static final String NOTIFICATION_TAG = "NEW_MATCH";

    @Getter private final Data data;

    public NewMatchBody(User user, String regToken, Match match) {
        super(new NotificationRequestBody.Notification(buildNotificationBody(user), ""), regToken);
        data = new Data(user, match);
    }

    private static String buildNotificationBody(User user) {
        return user.getName() + " " + ResourceUtils.getStringFromResourceId(R.string.added_a_new_match);
    }

    public static class Data implements NotificationRequestBody.NotificationData {

        @Getter private final String id;
        @Getter private final String imageUrl;

        public Data(User user, Match match) {
            id = match.getId();
            imageUrl = user.getImageUrl();
        }

        public String getTag() {
            return NOTIFICATION_TAG;
        }
    }
}
