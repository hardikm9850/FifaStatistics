package com.example.kevin.fifastatistics.views.notifications.friendrequestnotification;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.views.notifications.FifaNotification;

public class FriendRequestDeclineService extends Service
{
    private static final String TAG = "RequestDecline";
    private static final long DELAY_AFTER_DECLINE_UNTIL_CANCEL_MS = 1300;

    public FriendRequestDeclineService()
    {
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        declineFriendRequest();
        updateAndClearNotification();

        return START_NOT_STICKY;
    }

    private void declineFriendRequest()
    {
        Log.i(TAG, "Declining request");
        User user = SharedPreferencesManager.getUser();
        int size = user.getIncomingRequests().size();
        user.getIncomingRequests().remove(size - 1);
        SharedPreferencesManager.storeUserSync(user);
    }

    private void updateAndClearNotification()
    {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        updateNotification(nm);
        waitForCancelDelay();
        nm.cancel(FriendRequestNotification.NOTIFICATION_ID);
    }

    private void updateNotification(NotificationManager nm)
    {
        NotificationCompat.Builder nb = FifaNotification.getNotifcationBuilder()
                .setContentText("Friend request declined");

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
