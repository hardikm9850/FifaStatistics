package com.example.kevin.fifastatistics.views.notifications.friendrequestnotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.kevin.fifastatistics.activities.MainActivity;
import com.example.kevin.fifastatistics.fragments.FriendsFragment;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.Constants;
import com.example.kevin.fifastatistics.models.user.User;

public class FriendRequestAcceptService extends Service
{
    private static final String TAG = "RequestAccept";

    public FriendRequestAcceptService()
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
        acceptFriendRequest();
        cancelNotification();
        startActivity();

        return START_NOT_STICKY;
    }

    private void acceptFriendRequest()
    {
        Log.i(TAG, "Accepting request");
        User user = SharedPreferencesManager.getUser();
        int size = user.getIncomingRequests().size();
        user.addFriend(user.getIncomingRequests().get(size - 1));
        user.getIncomingRequests().remove(size - 1);
        SharedPreferencesManager.storeUserSync(user);
    }

    private void cancelNotification()
    {
        NotificationManager nm = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);

        nm.cancel(FriendRequestNotification.NOTIFICATION_ID);
    }

    private void startActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.FRAGMENT_EXTRA, Constants.FRIENDS_FRAGMENT);
        intent.putExtra(MainActivity.PAGE_EXTRA, FriendsFragment.friendsView);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent activityIntent = PendingIntent.getActivity(
                this, 1, intent, PendingIntent.FLAG_ONE_SHOT);

        attemptToSendPendingIntent(activityIntent);
    }

    private void attemptToSendPendingIntent(PendingIntent pendingIntent)
    {
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }
}
