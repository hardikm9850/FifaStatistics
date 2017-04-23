package com.example.kevin.fifastatistics.views.notifications;

import android.content.Context;

import com.example.kevin.fifastatistics.models.notifications.NotificationData;

/**
 * Null notification object used when no valid notification can be initialized.
 * In theory, this class should never actually end up being used. It is essentially
 * a failsafe.
 */
public class NullNotification extends FifaNotification
{
    private static final int NOTIFICATION_ID = -1;

    public NullNotification(Context c) {
        super(c);
    }

    @Override
    protected NotificationData getNotificationBundle() { return null; }

    @Override
    protected void setDefaultNotificationSettings(NotificationData bundle) {}

    @Override
    protected void setContentIntent() {}

    @Override
    protected void performPreSendActions() {}

    @Override
    protected int getNotificationId() { return NOTIFICATION_ID; }

    @Override
    protected void send(int notificationId) {}
}
