package com.example.kevin.fifastatistics.views.notifications.friendrequestnotification;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.kevin.fifastatistics.managers.NotificationSender;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.views.notifications.FifaNotification;

public class FriendRequestDeclineService extends Service
{
    private static final String TAG = "RequestDecline";
    private static final long DELAY_AFTER_DECLINE_UNTIL_CANCEL_MS = 1300;

    public FriendRequestDeclineService() {}

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        declineFriendRequest();
        return START_NOT_STICKY;
    }

    private void declineFriendRequest()
    {
        Log.i(TAG, "Declining request");
        User user = SharedPreferencesManager.getUser();
        Friend friend = user.getIncomingRequests().get(user.getIncomingRequests().size() - 1);
        NotificationSender.declineFriendRequest(user, friend.getRegistrationToken())
                .subscribe(response -> {
                    if (response.isSuccessful()) {
                        user.declineIncomingRequest(friend);
                        SharedPreferencesManager.storeUser(user);
                        updateAndClearNotification("Friend request declined");
                    } else {
                        updateAndClearNotification("Failed to decline friend request");
                    }
                });
    }

    private void updateAndClearNotification(String message)
    {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        updateNotification(nm, message);
        waitForCancelDelay();
        nm.cancel(FriendRequestNotification.NOTIFICATION_ID);
    }

    private void updateNotification(NotificationManager nm, String message)
    {
        NotificationCompat.Builder nb = FifaNotification.getNotifcationBuilder().setContentText(message);
        nb.mActions.clear();
        nm.notify(FriendRequestNotification.NOTIFICATION_ID, nb.build());
    }

    private void waitForCancelDelay()
    {
        try {
            Thread.sleep(DELAY_AFTER_DECLINE_UNTIL_CANCEL_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
