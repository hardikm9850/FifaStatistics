package com.example.kevin.fifastatistics.views.notifications.friendrequestnotification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
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
        return START_STICKY;
    }

    private void acceptFriendRequest()
    {
        Log.i(TAG, "Accepting request");
        SharedPreferencesManager preferencesManager = SharedPreferencesManager.getInstance(this);
        User user = preferencesManager.getUser();
        int size = user.getIncomingRequests().size();
        user.addFriend(user.getIncomingRequests().get(size - 1));
        user.getIncomingRequests().remove(size - 1);
        preferencesManager.storeUser(user);
    }
}
