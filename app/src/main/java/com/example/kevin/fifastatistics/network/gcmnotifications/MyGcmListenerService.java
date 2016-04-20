package com.example.kevin.fifastatistics.network.gcmnotifications;

import android.os.Bundle;
import android.util.Log;

import com.example.kevin.fifastatistics.managers.ImageLoaderManager;
import com.example.kevin.fifastatistics.models.Constants;
import com.example.kevin.fifastatistics.models.user.Friend;
import com.example.kevin.fifastatistics.models.user.User;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.views.notifications.FriendRequestNotification;
import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    private static final String IMAGE_URL = "imageUrl";
    private static final String DEFAULT_TAG = "default";

    @Override
    public void onMessageReceived(String from, Bundle bundle)
    {
        Log.d(TAG, "From: " + from);
        ImageLoaderManager.initializeNotificationsImageLoader(this);

        String tag = (getTag(bundle) == null) ? DEFAULT_TAG : getTag(bundle);
        switch (tag) {
            case Constants.FRIEND_REQUEST_TAG :
                addFriendRequestToUser(bundle);
                sendFriendRequestNotification(bundle);
                break;
            default :
                break;
        }

    }

    private void sendFriendRequestNotification(Bundle notification)
    {
        FriendRequestNotification frn = new FriendRequestNotification(this, notification);
        frn.send();
    }

    private void addFriendRequestToUser(Bundle data)
    {
        SharedPreferencesManager handler = SharedPreferencesManager.getInstance(this);
        User user = handler.getUser();

        int level = Integer.parseInt(data.getString("level"));
        Friend friend = new Friend.Builder()
                .withId(data.getString("id"))
                .withImageUrl(data.getString(IMAGE_URL))
                .withLevel(level)
                .withName(data.getString("name"))
                .withRegistrationToken(data.getString("registrationToken"))
                .build();

        user.addIncomingRequest(friend);
        handler.storeUser(user);
    }

    private static String getTag(Bundle data)
    {
        try {
            return data.getString("tag");
        }
        catch (NullPointerException e) {
            return DEFAULT_TAG;
        }
    }
}