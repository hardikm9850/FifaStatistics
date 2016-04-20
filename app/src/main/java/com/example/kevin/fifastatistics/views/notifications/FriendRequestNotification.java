package com.example.kevin.fifastatistics.views.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.MainActivity;
import com.example.kevin.fifastatistics.fragments.FriendsFragment;
import com.example.kevin.fifastatistics.models.Constants;
import com.example.kevin.fifastatistics.network.gcmnotifications.FriendRequestAcceptService;
import com.example.kevin.fifastatistics.network.gcmnotifications.FriendRequestDeclineService;

public class FriendRequestNotification extends FifaNotification
{
    private static final int NOTIFICATION_ID = 0;

    private NotificationCompat.Builder notificationBuilder;
    private PendingIntent contentIntent;
    private PendingIntent acceptRequestPendingIntent;
    private PendingIntent declineRequestPendingIntent;
    private Context context;

    public FriendRequestNotification(Context c, Bundle notification)
    {
        super(c, notification);
        notificationBuilder = super.getNotificationBuilder();

        context = c;
        initializeContentIntent();
        initializeDeclineRequestPendingIntent();
        initializeAcceptRequestPendingIntent();

        buildNotification(notification);
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

    private void buildNotification(Bundle notification)
    {
        setContentText(notification);
        setContentIntent();

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
        notificationBuilder.setContentIntent(contentIntent);
    }

    private void addAcceptRequestAction(Resources resources)
    {
        notificationBuilder.addAction(
                R.drawable.ic_menu_share,
                resources.getString(R.string.notification_accept),
                acceptRequestPendingIntent);
    }

    private void addDeclineRequestAction(Resources resources)
    {
        notificationBuilder.addAction(
                R.drawable.ic_action_navigation_close,
                resources.getString(R.string.notification_decline),
                declineRequestPendingIntent);
    }

    @Override
    public void send()
    {
        NotificationManager nm = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);

        nm.notify(NOTIFICATION_ID, notificationBuilder.build());
    }
}
