package com.example.kevin.fifastatistics.models.notifications;

/**
 * Model representation of data received from notifications.
 */
public interface NotificationData {
    String getBody();
    String getTitle();
    String getTag();
    String getImageUrl();
}
