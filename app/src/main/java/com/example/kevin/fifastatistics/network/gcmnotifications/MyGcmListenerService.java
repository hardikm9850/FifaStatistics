package com.example.kevin.fifastatistics.network.gcmnotifications;

import android.os.Bundle;

import com.example.kevin.fifastatistics.views.notifications.FifaNotificationFactory;
import com.example.kevin.fifastatistics.views.notifications.FifaNotification;
import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle bundle)
    {
        createAndSendNotification(bundle);
    }

    private void createAndSendNotification(Bundle bundle)
    {
        FifaNotification n = FifaNotificationFactory.createNotification(this, bundle);
        n.performPreSendActions();
        n.finalizeAndSend();
    }

}