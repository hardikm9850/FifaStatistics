package com.example.kevin.fifastatistics.network.gcmnotifications;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.user.User;

public class FriendRequestDeclineService extends Service
{
    private static final String TAG = "RequestDecline";

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
        return START_STICKY;
    }

    private void declineFriendRequest()
    {
        Log.i(TAG, "Declining request");
        SharedPreferencesManager preferencesManager = SharedPreferencesManager.getInstance(getApplicationContext());
        User user = preferencesManager.getUser();
        int size = user.getIncomingRequests().size();
        user.getIncomingRequests().remove(size - 1);
        preferencesManager.storeUser(user);
    }
}
