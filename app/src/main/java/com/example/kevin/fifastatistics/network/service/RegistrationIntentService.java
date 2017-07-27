package com.example.kevin.fifastatistics.network.service;

import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceIdService;

public class RegistrationIntentService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, UpdateTokenService.class);
        startService(intent);
    }
}
