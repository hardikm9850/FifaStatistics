package com.example.kevin.fifastatistics.views.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.models.notifications.EventNotificationData;
import com.example.kevin.fifastatistics.models.notifications.NotificationData;
import com.example.kevin.fifastatistics.utils.IntentFactory;

import java.util.Map;

public class SeriesNotification extends FifaNotification {

    private PendingIntent mContentIntent;
    private EventNotificationData mData;

    public SeriesNotification(Context c, Map<String, String> data) {
        super(c);
        this.mData = new EventNotificationData(data);
        initializeContentIntent(c);
    }

    private void initializeContentIntent(Context c) {
        Intent intent = IntentFactory.createMainActivityIntent(c);
        mContentIntent = PendingIntent.getActivity(c, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        //TODO
    }

    @Override
    protected NotificationData getNotificationBundle() {
        return mData;
    }

    @Override
    protected void setContentIntent() {
        mNotificationBuilder.setContentIntent(mContentIntent);
    }

    @Override
    protected int getNotificationId() {
        return mData.getId().hashCode();
    }

    @Override
    public void performPreSendActions() {
        RetrievalManager.syncCurrentUserWithServer();
    }
}
