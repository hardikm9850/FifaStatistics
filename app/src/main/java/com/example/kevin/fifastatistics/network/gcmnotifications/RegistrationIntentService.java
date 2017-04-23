package com.example.kevin.fifastatistics.network.gcmnotifications;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.ObservableUtils;
import com.example.kevin.fifastatistics.utils.UserUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class RegistrationIntentService extends FirebaseInstanceIdService {

    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    private static final String TAG = "RegIntentService";

    @Override
    public void onTokenRefresh() {
        try {
            String token = FirebaseInstanceId.getInstance().getToken();
            Log.i(TAG, "GCM Registration Token: " + token);

            User user = SharedPreferencesManager.getUser();
            if (user != null) {
                user.setRegistrationToken(token);
                SharedPreferencesManager.storeUser(user);
                updateTokenOnServerIfChanged(user, token);
            } else {
                SharedPreferencesManager.setDidSendRegistrationToken(true);
            }
            SharedPreferencesManager.setRegistrationToken(token);
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            SharedPreferencesManager.setRegistrationFailed(true);
            SharedPreferencesManager.setDidSendRegistrationToken(false);
        }

        Intent registrationComplete = new Intent(REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void updateTokenOnServerIfChanged(User user, String newToken) {
        String oldToken = SharedPreferencesManager.getRegistrationToken();
        if ((oldToken != null && !oldToken.equals(newToken)) || !SharedPreferencesManager.didSendRegistrationToken()) {
            SharedPreferencesManager.setDidSendRegistrationToken(false);
            UserUtils.patchRegToken(user.getId(), newToken).subscribe(new ObservableUtils.OnNextObserver<User>() {
                @Override
                public void onNext(User user) {
                    SharedPreferencesManager.setDidSendRegistrationToken(true);
                }
            });
        } else {
            SharedPreferencesManager.setDidSendRegistrationToken(true);
        }
    }
}
