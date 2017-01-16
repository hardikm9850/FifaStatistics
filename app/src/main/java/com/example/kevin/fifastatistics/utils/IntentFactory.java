package com.example.kevin.fifastatistics.utils;

import android.content.Context;
import android.content.Intent;

import com.example.kevin.fifastatistics.activities.BasePlayerActivity;
import com.example.kevin.fifastatistics.activities.CreateSeriesActivity;
import com.example.kevin.fifastatistics.activities.MainActivity;
import com.example.kevin.fifastatistics.activities.PlayerActivty;
import com.example.kevin.fifastatistics.fragments.FriendsFragment;
import com.example.kevin.fifastatistics.models.Constants;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;

public class IntentFactory {

    public static Intent createPlayerActivityIntent(Context c, Player player) {
        Intent intent = new Intent(c, PlayerActivty.class);
        intent.putExtra(PlayerActivty.NAME_EXTRA, player.getName());
        intent.putExtra(PlayerActivty.ID_EXTRA, player.getId());
        intent.putExtra(PlayerActivty.IMAGE_URL_EXTRA, player.getImageUrl());
        intent.putExtra(PlayerActivty.REG_TOKEN_EXTRA, player.getRegistrationToken());
        return intent;
    }

    public static Intent createNewSeriesActivityIntent(Context c, Player player) {
        Intent intent = new Intent(c, CreateSeriesActivity.class);
        intent.putExtra(BasePlayerActivity.NAME_EXTRA, player.getName());
        intent.putExtra(BasePlayerActivity.ID_EXTRA, player.getId());
        intent.putExtra(BasePlayerActivity.IMAGE_URL_EXTRA, player.getImageUrl());
        intent.putExtra(BasePlayerActivity.REG_TOKEN_EXTRA, player.getRegistrationToken());
        return intent;
    }

    public static Intent createFriendRequestsIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.FRAGMENT_EXTRA, Constants.FRIENDS_FRAGMENT);
        intent.putExtra(MainActivity.PAGE_EXTRA, FriendsFragment.REQUESTS_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public static Intent createMainActivityIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }
}
