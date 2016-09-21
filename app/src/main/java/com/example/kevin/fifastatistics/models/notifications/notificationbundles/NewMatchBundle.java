package com.example.kevin.fifastatistics.models.notifications.notificationbundles;

import android.os.Bundle;

import lombok.Getter;

@Getter
public class NewMatchBundle implements NotificationBundle {

    private final String body;
    private final String tag;
    private final String matchId;
    private final String imageUrl;

    public NewMatchBundle(Bundle bundle) {
        body = bundle.getString("gcm.notification.body");
        tag = bundle.getString("tag");
        matchId = bundle.getString("id");
        imageUrl = bundle.getString("imageUrl");
    }

}
