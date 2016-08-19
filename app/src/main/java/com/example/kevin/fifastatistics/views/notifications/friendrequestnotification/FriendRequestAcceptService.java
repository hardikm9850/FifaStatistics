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

public class FriendRequestAcceptService extends Service {

    private static final String TAG = "RequestAccept";
    private static final long DELAY_AFTER_ACCEPT_UNTIL_CANCEL_MS = 1300;

    public FriendRequestAcceptService() {}

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        acceptFriendRequest();
        return START_NOT_STICKY;
    }

    private void acceptFriendRequest() {
        Log.i(TAG, "Accepting request");
        User currentUser = SharedPreferencesManager.getUser();
        Friend friend = currentUser.getIncomingRequests().get(currentUser.getIncomingRequests().size() - 1);
        NotificationSender.acceptFriendRequest(currentUser, friend.getRegistrationToken())
                .subscribe(response -> {
                    if (response.isSuccessful()) {
                        currentUser.acceptIncomingRequest(friend);
                        SharedPreferencesManager.storeUser(currentUser);
                        updateAndClearNotification("Friend request accepted");
                    } else {
                        updateAndClearNotification("Failed to accept friend request");
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
            Thread.sleep(DELAY_AFTER_ACCEPT_UNTIL_CANCEL_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
