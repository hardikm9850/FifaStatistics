package com.example.kevin.fifastatistics.network.gcmnotifications;

import android.os.Bundle;
import android.util.Log;

import com.example.kevin.fifastatistics.views.notifications.FifaNotification;
import com.example.kevin.fifastatistics.views.notifications.FifaNotificationFactory;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseListenerService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("LISTENER", "received remote message");
//        createAndSendNotification();
    }

    private void createAndSendNotification(Bundle bundle) {
        FifaNotification n = FifaNotificationFactory.createNotification(this, bundle);
        n.build();
    }

}