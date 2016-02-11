package com.example.kevin.fifastatistics.gcm;


import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.overview.MainActivity;
import com.example.kevin.fifastatistics.user.User;
import com.example.kevin.fifastatistics.utils.PreferenceHandler;
import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param bundle Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle bundle) {
        String message = bundle.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        String tag = getTag(bundle);
        Log.i(TAG, "tag: " + tag);
        if (tag == null) {
            // Do nothing
        }
        else if (tag.equals(NotificationTypesEnum.FRIEND_REQUEST.name()))
        {
            addFriendRequestToUser(bundle);
            sendFriendRequestNotification(bundle);
        }
        else {
            // TODO OTHER NOTIFICATIONS
        }

    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param notification GCM message received.
     */
    private void sendFriendRequestNotification(Bundle notification)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fragment", "friends");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        // TODO HANDLE NPE
        String title = "";
        String body = "";
        final String NEW_REQUEST = "New Friend Request";
        try
        {
            title = notification.getString("gcm.notification.title");
            body = notification.getString("gcm.notification.body");
        }
        catch (NullPointerException e)
        {
            Log.e(TAG, "NPE!");
            title = NEW_REQUEST;
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    public void addFriendRequestToUser(Bundle data)
    {
        Context context = getApplicationContext();
        PreferenceHandler handler = PreferenceHandler.getInstance(context);
        User user = handler.getUser();
        user.addIncomingRequest(
                data.getString("name"), data.getString("id"), data.getString("imageUrl"), context);
        handler.storeUser(user);
    }

    public static String getTag(Bundle data)
    {
        try
        {
            return data.getString("tag");
        }
        catch (NullPointerException e)
        {
            return null;
        }
    }
}