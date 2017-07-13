package com.example.kevin.fifastatistics.views.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.kevin.fifastatistics.activities.MatchActivity;
import com.example.kevin.fifastatistics.models.notifications.EventNotificationData;
import com.example.kevin.fifastatistics.models.notifications.NotificationData;

import java.util.Map;

public class UpdateHandledNotification extends FifaNotification {

    private PendingIntent mContentIntent;
    private EventNotificationData mData;

    public UpdateHandledNotification(Context context, Map<String, String> data) {
        super(context);
        mData = new EventNotificationData(data);
        initContentIntent(context);
    }

    private void initContentIntent(Context c) {
        Intent intent = MatchActivity.getLaunchIntent(c, mData.getId());
        mContentIntent = PendingIntent.getActivity(c, 0, intent, PendingIntent.FLAG_ONE_SHOT);
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
    protected void performPreSendActions() {}

    @Override
    protected int getNotificationId() {
        return mData.getId().hashCode();
    }
}
