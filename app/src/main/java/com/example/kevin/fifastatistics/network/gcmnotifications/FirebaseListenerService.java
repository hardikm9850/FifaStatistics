package com.example.kevin.fifastatistics.network.gcmnotifications;

import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;

import com.example.kevin.fifastatistics.views.notifications.FifaNotification;
import com.example.kevin.fifastatistics.views.notifications.FifaNotificationFactory;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FirebaseListenerService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("LISTENER", "received remote message");
        Bundle notification = getBundleFromMessage(remoteMessage);
        FifaNotification n = FifaNotificationFactory.createNotification(this, notification);
        n.build();
    }

    private Bundle getBundleFromMessage(RemoteMessage m) {
        Parcel p = Parcel.obtain();
        try {
            Parcel.obtain();
            m.writeToParcel(p, 0);
            Bundle b = new Bundle();
            b.readFromParcel(p);
            return b;
        } finally {
            p.recycle();
        }
    }
}