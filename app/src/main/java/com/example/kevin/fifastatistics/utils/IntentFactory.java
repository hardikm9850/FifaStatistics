package com.example.kevin.fifastatistics.utils;

import android.content.Context;
import android.content.Intent;

import com.example.kevin.fifastatistics.activities.PlayerActivty;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;

public class IntentFactory {

    public static Intent createPlayerActivityIntent(Context c, Friend friend) {
        Intent intent = new Intent(c, PlayerActivty.class);
        intent.putExtra(PlayerActivty.NAME_EXTRA, friend.getName());
        intent.putExtra(PlayerActivty.ID_EXTRA, friend.getId());
        intent.putExtra(PlayerActivty.IMAGE_URL_EXTRA, friend.getImageUrl());

        return intent;
    }
}
