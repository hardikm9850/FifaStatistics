package com.example.kevin.fifastatistics.network.gcmnotifications;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class RegistrationIntentService extends FirebaseInstanceIdService {

    private static final String TAG = "RegIntentService";

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";

    @Override
    public void onTokenRefresh()
    {
        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            // [START get_token]
            String token = FirebaseInstanceId.getInstance().getToken();
            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + token);

            // TODO: Implement this method to send any registration to your app's servers.
            SharedPreferencesManager.setRegistrationToken(token);
            User user = SharedPreferencesManager.getUser();
            if (user != null) {
                user.setRegistrationToken(token);
                SharedPreferencesManager.storeUser(user);
            }


            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            SharedPreferencesManager.setDidSendRegistrationToken(true);
            // [END register_for_gcm]
        }
        catch (Exception e)
        {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            SharedPreferencesManager.setRegistrationFailed(true);
            SharedPreferencesManager.setDidSendRegistrationToken(false);
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

}
