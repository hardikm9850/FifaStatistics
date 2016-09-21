package com.example.kevin.fifastatistics.views.notifications.friendrequestnotification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.models.notifications.notificationbundles.FriendRequestBundle;
import com.example.kevin.fifastatistics.models.notifications.notificationbundles.NotificationBundle;
import com.example.kevin.fifastatistics.utils.IntentFactory;
import com.example.kevin.fifastatistics.utils.UserUtils;
import com.example.kevin.fifastatistics.views.notifications.FifaNotification;

/**
 * Notification that is displayed when an outgoing friend request has been accepted.
 */
public class AcceptRequestNotification extends FifaNotification{

    public static final int NOTIFICATION_ID = 1;

    private Context mContext;
    private FriendRequestBundle mNotificationBundle;
    private PendingIntent mContentIntent;

    public AcceptRequestNotification(Context c, Bundle notification) {
        super(c);
        mContext = c;
        mNotificationBundle = new FriendRequestBundle(notification);

        initializeContentIntent(mNotificationBundle.getFriend());
    }

    private void initializeContentIntent(Friend friend) {
        Intent intent = IntentFactory.createPlayerActivityIntent(mContext, friend, true);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        mContentIntent = PendingIntent.getActivity(
                mContext, NOTIFICATION_ID, intent, PendingIntent.FLAG_ONE_SHOT);
    }

    @Override
    protected NotificationBundle getNotificationBundle() {
        return mNotificationBundle;
    }

    @Override
    protected void setContentIntent() {
        mNotificationBuilder.setContentIntent(mContentIntent);
    }

    @Override
    protected void performPreSendActions() {
        User currentUser = SharedPreferencesManager.getUser();
        currentUser.removeOutgoingRequest(mNotificationBundle.getFriend());
        currentUser.addFriend(mNotificationBundle.getFriend());
        UserUtils.updateUserSync(currentUser).subscribe();
    }

    @Override
    protected int getNotificationId() {
        return NOTIFICATION_ID;
    }
}
