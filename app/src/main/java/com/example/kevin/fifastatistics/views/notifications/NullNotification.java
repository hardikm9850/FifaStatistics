package com.example.kevin.fifastatistics.views.notifications;

import android.content.Context;

import com.example.kevin.fifastatistics.models.notificationbundles.NotificationBundle;

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
    protected NotificationBundle getNotificationBundle() { return null; }

    @Override
    protected void setDefaultNotificationSettings(NotificationBundle bundle) {}

    @Override
    protected void setContentText() {}

    @Override
    protected void setContentIntent() {}

    @Override
    protected void performPreSendActions() {}

    @Override
    protected int getNotificationId() { return NOTIFICATION_ID; }

    @Override
    protected void send(int notificationId) {}
}
