package com.example.kevin.fifastatistics.models.notificationbundles;

/**
 * Model representation of bundles received from notifications.
 */
public interface NotificationBundle
{
    /**
     * Retrieve the imageUrl of the image to be used when displaying the notification.
     * @return  the imageUrl
     */
    String getImageUrl();
}
