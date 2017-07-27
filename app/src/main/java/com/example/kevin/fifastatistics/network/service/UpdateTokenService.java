package com.example.kevin.fifastatistics.network.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
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
            SharedPreferencesManager.setRegistrationFailed(true);
            SharedPreferencesManager.setDidSendRegistrationToken(false);
        }

        Intent registrationComplete = new Intent(REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void updateToken() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "GCM Registration Token: " + token);

        User user = SharedPreferencesManager.getUser();
        if (user != null) {
            user.setRegistrationToken(token);
            SharedPreferencesManager.storeUser(user);
            updateTokenOnServer(user, token);
        } else {
            SharedPreferencesManager.setDidSendRegistrationToken(false);
        }
        SharedPreferencesManager.setRegistrationToken(token);
    }

    private void updateTokenOnServer(User user, String newToken) {
        SharedPreferencesManager.setDidSendRegistrationToken(false);
        UserUtils.patchRegToken(user.getId(), newToken)
                .retryWhen(ObservableUtils.getExponentialBackoffRetryWhen())
                .subscribe(new ObservableUtils.OnNextObserver<User>() {
                    @Override
                    public void onNext(User user) {
                        SharedPreferencesManager.setDidSendRegistrationToken(true);
                    }
                });
    }
}
