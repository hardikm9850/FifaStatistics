package com.example.kevin.fifastatistics.models.notificationrequestbodies;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Abstract noification body
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class NotificationRequestBody {

    /** The notification body and title */
    @Getter private final Notification notification;

    /** The registration token of the user the notification is being sent to */
    @Getter private final String to;

    public abstract NotificationData getData();

    @RequiredArgsConstructor
    @Getter
    public static class Notification {
        private final String body;
        private final String title;
    }

    /**
     * subclasses should have a nested class that implements this interface, that represents
     * the 'data' section of notifications
     */
    public interface NotificationData {
        String getTag();
    }
}
