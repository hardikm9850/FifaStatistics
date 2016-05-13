package com.example.kevin.fifastatistics.models.notificationbundles;

import android.os.Bundle;

import com.example.kevin.fifastatistics.models.user.Friend;

/**
 * Created by Kevin on 5/12/2016.
 */
public class FriendRequestBundle implements NotificationBundle
{
    private String body;
    private String tag;
    private Friend friend;

    public FriendRequestBundle(Bundle bundle) {

        body = bundle.getString("gcm.notification.body");
        tag = bundle.getString("tag");

        friend = new Friend.Builder()
                .withId(bundle.getString("id"))
                .withImageUrl(bundle.getString("imageUrl"))
                .withLevel(Integer.parseInt(bundle.getString("level")))
                .withName(bundle.getString("name"))
                .withRegistrationToken(bundle.getString("registrationToken"))
                .build();
    }

    public String getBody() {
        return body;
    }

    public String getTag() {
        return tag;
    }

    public Friend getFriend() {
        return friend;
    }

    @Override
    public String getImageUrl() {
        return friend.getImageUrl();
    }

}
