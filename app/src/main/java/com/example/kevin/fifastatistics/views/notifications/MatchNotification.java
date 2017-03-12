package com.example.kevin.fifastatistics.views.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.models.notifications.notificationbundles.NewMatchBundle;
import com.example.kevin.fifastatistics.models.notifications.notificationbundles.NotificationBundle;
import com.example.kevin.fifastatistics.utils.IntentFactory;

public class MatchNotification extends FifaNotification {

    public static final int NOTIFICATION_ID = 3;

    private PendingIntent mContentIntent;
    private NewMatchBundle mBundle;

    public MatchNotification(Context c, Bundle bundle) {
        super(c);
        this.mBundle = new NewMatchBundle(bundle);

        initializeContentIntent(c);
    }

    private void initializeContentIntent(Context c) {
        Intent intent = IntentFactory.createMainActivityIntent(c);
        mContentIntent = PendingIntent.getActivity(c, NOTIFICATION_ID, intent, PendingIntent.FLAG_ONE_SHOT);
        //TODO
    }

    @Override
    protected NotificationBundle getNotificationBundle() {
        return mBundle;
    }

    @Override
    protected void setContentIntent() {
        mNotificationBuilder.setContentIntent(mContentIntent);
    }

    @Override
    protected int getNotificationId() {
        return NOTIFICATION_ID;
    }

    @Override
    public void performPreSendActions() {
        RetrievalManager.syncCurrentUserWithServer();
    }

}
