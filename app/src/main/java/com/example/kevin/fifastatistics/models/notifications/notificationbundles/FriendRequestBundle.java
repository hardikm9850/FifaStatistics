package com.example.kevin.fifastatistics.models.notifications.notificationbundles;

import android.os.Bundle;

import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;

import lombok.Getter;

/**
 * Model of notification bundle that is received from Friend Requests.
 */
@Getter
public class FriendRequestBundle implements NotificationBundle
{
    private final String body;
    private final String tag;
    private final Friend friend;

    @SuppressWarnings("ConstantConditions")
    public FriendRequestBundle(Bundle bundle) {

        body = bundle.getString("gcm.notification.body");
        tag = bundle.getString("tag");

        friend = Friend.builder()
                .id(bundle.getString("id"))
                .imageUrl(bundle.getString("imageUrl"))
                .level(Integer.parseInt(bundle.getString("level")))
                .name(bundle.getString("name"))
                .registrationToken(bundle.getString("registrationToken"))
                .build();
    }

    @Override
    public String getImageUrl() {
        return friend.getImageUrl();
    }

}
