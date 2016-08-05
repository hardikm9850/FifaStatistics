package com.example.kevin.fifastatistics.utils;

import android.content.Context;
import android.content.Intent;

import com.example.kevin.fifastatistics.activities.PlayerActivty;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

public class IntentFactory {

    /**
     * Create a new PlayerActivity intent, and indicate that this user is the current user's friend.
     */
    public static Intent createPlayerActivityIntent(Context c, Friend friend) {
        Intent intent = new Intent(c, PlayerActivty.class);
        intent.putExtra(PlayerActivty.NAME_EXTRA, friend.getName());
        intent.putExtra(PlayerActivty.ID_EXTRA, friend.getId());
        intent.putExtra(PlayerActivty.IMAGE_URL_EXTRA, friend.getImageUrl());
        intent.putExtra(PlayerActivty.REG_TOKEN_EXTRA, friend.getRegistrationToken());
        intent.putExtra(PlayerActivty.FRIEND_EXTRA, true);

        return intent;
    }

    /**
     * Create a new PlayerActivity intent, and indicate that this user is not a friend of the
     * current user.
     */
    public static Intent createPlayerActivityIntent(Context c, User user) {
        Intent intent = new Intent(c, PlayerActivty.class);
        intent.putExtra(PlayerActivty.NAME_EXTRA, user.getName());
        intent.putExtra(PlayerActivty.ID_EXTRA, user.getId());
        intent.putExtra(PlayerActivty.IMAGE_URL_EXTRA, user.getImageUrl());
        intent.putExtra(PlayerActivty.REG_TOKEN_EXTRA, user.getRegistrationToken());
        intent.putExtra(PlayerActivty.FRIEND_EXTRA, false);

        return intent;
    }
}
