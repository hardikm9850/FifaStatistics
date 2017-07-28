package com.example.kevin.fifastatistics.views.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.MatchActivity;
import com.example.kevin.fifastatistics.models.notifications.EventNotificationData;
import com.example.kevin.fifastatistics.models.notifications.NotificationData;
import com.example.kevin.fifastatistics.utils.ResourceUtils;

import java.util.Map;

public class UpdateHandledNotification extends FifaNotification {

    private static final String VIEW_MATCH = ResourceUtils.getStringFromResourceId(R.string.view_match);

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
    protected void addActions() {
        mNotificationBuilder.addAction(new NotificationCompat.Action(0, VIEW_MATCH, mContentIntent));
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
