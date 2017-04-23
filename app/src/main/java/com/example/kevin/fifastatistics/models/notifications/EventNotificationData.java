package com.example.kevin.fifastatistics.models.notifications;

import com.example.kevin.fifastatistics.utils.SerializationUtils;

import java.util.Map;

import lombok.Getter;

@Getter
public class EventNotificationData implements NotificationData {

    private final String body;
    private final String tag;
    private final String title;
    private final String id;
    private final String imageUrl;

    public EventNotificationData(Map<String, String> data) {
        body = data.get("body");
        tag = data.get("tag");
        title = data.get("title");
        id = data.get("id");
        imageUrl = data.get("imageUrl");
    }

    @Override
    public String toString() {
        return SerializationUtils.toFormattedJson(this);
    }

}
