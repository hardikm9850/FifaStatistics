package com.example.kevin.fifastatistics.models.notificationbundles;

/**
 * Model representation of bundles received from notifications.
 */
public interface NotificationBundle
{
    /**
     * Retrieve the text that is to be displayed as the notification body.
     * @return  the body
     */
    String getBody();

    /**
     * Retrieve the imageUrl of the image to be used when displaying the notification.
     * @return  the imageUrl
     */
    String getImageUrl();
}
