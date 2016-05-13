package com.example.kevin.fifastatistics.views.notifications.friendrequestnotification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.MainActivity;
import com.example.kevin.fifastatistics.fragments.FriendsFragment;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.Constants;
import com.example.kevin.fifastatistics.models.user.Friend;
import com.example.kevin.fifastatistics.models.user.User;


import com.example.kevin.fifastatistics.views.notifications.FifaNotification;

public class FriendRequestNotification extends FifaNotification
{
    public static final int NOTIFICATION_ID = 0;
    private PendingIntent contentIntent;
    private PendingIntent acceptRequestPendingIntent;
    private PendingIntent declineRequestPendingIntent;
    private Context context;
    private Bundle notificationData;

    public FriendRequestNotification(Context c, Bundle notification)
    {
        super(c, notification);
        notificationId = NOTIFICATION_ID;
        notificationData = notification;
        context = c;

        initializeAcceptRequestPendingIntent();
        initializeDeclineRequestPendingIntent();

        buildNotification();
    }

    private void initializeAcceptRequestPendingIntent()
    {
        Intent acceptRequestIntent = new Intent(
                context, FriendRequestAcceptService.class);

        acceptRequestPendingIntent = PendingIntent.getService(
                context, notificationId, acceptRequestIntent,
                PendingIntent.FLAG_ONE_SHOT);
    }

    private void initializeDeclineRequestPendingIntent()
    {
        Intent declineRequestIntent = new Intent(
                context, FriendRequestDeclineService.class);

        declineRequestPendingIntent = PendingIntent.getService(
                context, notificationId, declineRequestIntent,
                PendingIntent.FLAG_ONE_SHOT);
    }

    private void buildNotification()
    {
        Resources res = context.getResources();
        addAcceptRequestAction(res);
        addDeclineRequestAction(res);
    }

    @Override
    protected void setContentText(Bundle notification)
    {
        String notificationBody = getNotificationBody(notification);
        notificationBuilder.setContentText(notificationBody);
    }

    private String getNotificationBody(Bundle notification)
    {
        Resources res = context.getResources();
        String body = res.getString(R.string.new_friend_request);
        try {
            body += " " + notification.getString("gcm.notification.body");
        }
        catch (NullPointerException e) {
            body = res.getString(R.string.new_friend_request_npe);
        }

        return body;
    }

    @Override
    protected void setContentIntent()
    {
        initializeContentIntent();
        notificationBuilder.setContentIntent(contentIntent);
    }

    private void initializeContentIntent()
    {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.FRAGMENT_EXTRA, Constants
                .FRIENDS_FRAGMENT);
        intent.putExtra(MainActivity.PAGE_EXTRA, FriendsFragment.requestsView);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        contentIntent = PendingIntent.getActivity(
                context, notificationId, intent, PendingIntent.FLAG_ONE_SHOT);
    }

    private void addAcceptRequestAction(Resources resources)
    {
        notificationBuilder.addAction(
                R.drawable.ic_check_black_24dp,
                resources.getString(R.string.notification_accept),
                acceptRequestPendingIntent);
    }

    private void addDeclineRequestAction(Resources resources)
    {
        notificationBuilder.addAction(
                R.drawable.ic_close_black_24dp,
                resources.getString(R.string.notification_decline),
                declineRequestPendingIntent);
    }

    @Override
    public void performPreSendActions()
    {
        addFriendRequestToUser();
    }

    private void addFriendRequestToUser()
    {
        User user = SharedPreferencesManager.getUser();

        int level = Integer.parseInt(notificationData.getString("level"));
        Friend friend = new Friend.Builder()
                .withId(notificationData.getString("id"))
                .withImageUrl(notificationData.getString("imageUrl"))
                .withLevel(level)
                .withName(notificationData.getString("name"))
                .withRegistrationToken(notificationData.getString("registrationToken"))
                .build();

        user.addIncomingRequest(friend);
        SharedPreferencesManager.storeUser(user);
    }
}
