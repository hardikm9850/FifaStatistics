package com.example.kevin.fifastatistics.views.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.MatchUpdateActivity;
import com.example.kevin.fifastatistics.models.notifications.EventNotificationData;
import com.example.kevin.fifastatistics.models.notifications.NotificationData;
import com.example.kevin.fifastatistics.utils.ResourceUtils;

import java.util.Map;
import java.util.Random;

public class UpdateCreatedNotification extends FifaNotification {

    private static final String VIEW = ResourceUtils.getStringFromResourceId(R.string.view);

    private PendingIntent mContentIntent;
    private EventNotificationData mData;

    public UpdateCreatedNotification(Context context, Map<String, String> data) {
        super(context);
        mData = new EventNotificationData(data);
        initContentIntent(context);
    }

    private void initContentIntent(Context context) {
        Intent intent = getMatchIntent();
        mContentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
    }

    private Intent getMatchIntent() {
        int id = Math.abs(getNotificationId());
        Intent intent = MatchUpdateActivity.getNotificationIntent(context, mData.getId());
        intent.setAction(String.valueOf(id + new Random().nextInt(id)));
        return intent;
    }

    @Override
    protected NotificationData getNotificationBundle() {
        return mData;
    }

    @Override
    protected void addActions() {
        PendingIntent cancelingIntent = NotificationCancelingReceiver.getIntent(context, mContentIntent, getNotificationId());
        mNotificationBuilder.addAction(new NotificationCompat.Action(0, VIEW, cancelingIntent));
    }

    @Override
    protected void setContentIntent() {
        mNotificationBuilder.setContentIntent(mContentIntent);
    }

    @Override
    protected void performPreSendActions() {

    }

    @Override
    protected int getNotificationId() {
        return mData.getId().hashCode();
    }
}
