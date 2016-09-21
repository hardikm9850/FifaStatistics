package com.example.kevin.fifastatistics.models.notifications.notificationbundles;

import android.os.Bundle;
import android.util.Log;

import com.example.kevin.fifastatistics.utils.SerializationUtils;

import lombok.Getter;

@Getter
public class NewMatchBundle implements NotificationBundle {

    private final String body;
    private final String tag;
    private final String matchId;
    private final String imageUrl;

    public NewMatchBundle(Bundle bundle) {

        Log.e("RETRIEVED BUNDLE:", bundle.toString());

        body = bundle.getString("gcm.notification.body");
        tag = bundle.getString("tag");
        matchId = bundle.getString("id");
        imageUrl = bundle.getString("imageUrl");
    }

    @Override
    public String toString() {
        return SerializationUtils.toFormattedJson(this);
    }

}
