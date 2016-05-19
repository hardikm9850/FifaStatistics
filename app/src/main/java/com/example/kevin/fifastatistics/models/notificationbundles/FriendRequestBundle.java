package com.example.kevin.fifastatistics.models.notificationbundles;

import android.os.Bundle;

import com.example.kevin.fifastatistics.models.user.Friend;

import lombok.Getter;

/**
 * Created by Kevin on 5/12/2016.
 */
@Getter
public class FriendRequestBundle implements NotificationBundle
{
    private String body;
    private String tag;
    private Friend friend;

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
