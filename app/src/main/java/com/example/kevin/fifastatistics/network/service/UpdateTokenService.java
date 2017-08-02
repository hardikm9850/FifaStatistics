package com.example.kevin.fifastatistics.network.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.kevin.fifastatistics.managers.preferences.PrefsManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.ObservableUtils;
import com.example.kevin.fifastatistics.utils.UserUtils;
import com.google.firebase.iid.FirebaseInstanceId;

public class UpdateTokenService extends IntentService {

    private static final String REGISTRATION_COMPLETE = "registrationComplete";
    private static final String TAG = "updateTokenService";

    public UpdateTokenService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            updateToken();
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            PrefsManager.setRegistrationFailed(true);
            PrefsManager.setDidSendRegistrationToken(false);
        }

        Intent registrationComplete = new Intent(REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void updateToken() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "GCM Registration Token: " + token);

        User user = PrefsManager.getUser();
        if (user != null) {
            user.setRegistrationToken(token);
            PrefsManager.storeUser(user);
            updateTokenOnServer(user, token);
        } else {
            PrefsManager.setDidSendRegistrationToken(false);
        }
        PrefsManager.setRegistrationToken(token);
    }

    private void updateTokenOnServer(User user, String newToken) {
        PrefsManager.setDidSendRegistrationToken(false);
        UserUtils.patchRegToken(user.getId(), newToken)
                .retryWhen(ObservableUtils.getExponentialBackoffRetryWhen())
                .subscribe(new ObservableUtils.OnNextObserver<User>() {
                    @Override
                    public void onNext(User user) {
                        PrefsManager.setDidSendRegistrationToken(true);
                    }
                });
    }
}
