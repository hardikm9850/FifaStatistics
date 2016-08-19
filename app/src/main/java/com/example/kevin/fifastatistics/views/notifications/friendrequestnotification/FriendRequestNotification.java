package com.example.kevin.fifastatistics.views.notifications.friendrequestnotification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.MainActivity;
import com.example.kevin.fifastatistics.fragments.FriendsFragment;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.Constants;
import com.example.kevin.fifastatistics.models.notifications.notificationbundles.FriendRequestBundle;
import com.example.kevin.fifastatistics.models.notifications.notificationbundles.NotificationBundle;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.views.notifications.FifaNotification;

public class FriendRequestNotification extends FifaNotification
{
    public static final int NOTIFICATION_ID = 0;

    private PendingIntent contentIntent;
    private PendingIntent acceptRequestPendingIntent;
    private PendingIntent declineRequestPendingIntent;
    private Context context;
    private FriendRequestBundle friendRequestBundle;
    private Resources resources;

    public FriendRequestNotification(Context c, Bundle notification)
    {
        super(c);

        friendRequestBundle = new FriendRequestBundle(notification);
        context = c;
        resources = context.getResources();

        initializeContentIntent();
        initializeAcceptRequestPendingIntent();
        initializeDeclineRequestPendingIntent();
    }

    private void initializeContentIntent()
    {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.FRAGMENT_EXTRA, Constants.FRIENDS_FRAGMENT);
        intent.putExtra(MainActivity.PAGE_EXTRA, FriendsFragment.requestsView);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        contentIntent = PendingIntent.getActivity(
                context, NOTIFICATION_ID, intent, PendingIntent.FLAG_ONE_SHOT);
    }

    private void initializeAcceptRequestPendingIntent()
    {
        Intent acceptRequestIntent = new Intent(
                context, FriendRequestAcceptService.class);

        acceptRequestPendingIntent = PendingIntent.getService(
                context, NOTIFICATION_ID, acceptRequestIntent,
                PendingIntent.FLAG_ONE_SHOT);
    }

    private void initializeDeclineRequestPendingIntent()
    {
        Intent declineRequestIntent = new Intent(
                context, FriendRequestDeclineService.class);

        declineRequestPendingIntent = PendingIntent.getService(
                context, NOTIFICATION_ID, declineRequestIntent,
                PendingIntent.FLAG_ONE_SHOT);
    }

    @Override
    protected NotificationBundle getNotificationBundle() {
        return friendRequestBundle;
    }

    @Override
    protected void setContentIntent() {
        mNotificationBuilder.setContentIntent(contentIntent);
    }

    @Override
    protected void addActions() {
        mNotificationBuilder.addAction(
                R.drawable.ic_check_black_24dp,
                resources.getString(R.string.notification_accept),
                acceptRequestPendingIntent);

        mNotificationBuilder.addAction(
                R.drawable.ic_close_black_24dp,
                resources.getString(R.string.notification_decline),
                declineRequestPendingIntent);
    }

    @Override
    protected int getNotificationId() {
        return NOTIFICATION_ID;
    }

    @Override
    public void performPreSendActions() {
        addFriendRequestToUser();
    }

    private void addFriendRequestToUser() {
        User user = SharedPreferencesManager.getUser();
        user.addIncomingRequest(friendRequestBundle.getFriend());
        SharedPreferencesManager.storeUserSync(user);
    }
}
