package com.example.kevin.fifastatistics.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.kevin.fifastatistics.activities.BasePlayerActivity;
import com.example.kevin.fifastatistics.activities.CreateSeriesActivity;
import com.example.kevin.fifastatistics.activities.FifaBaseActivity;
import com.example.kevin.fifastatistics.activities.MainActivity;
import com.example.kevin.fifastatistics.activities.PlayerActivty;
import com.example.kevin.fifastatistics.fragments.FriendsFragment;
import com.example.kevin.fifastatistics.models.Constants;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;

public class IntentFactory {

    public static Intent createPlayerActivityIntent(Activity activity, Player player) {
        Intent intent = new Intent(activity, PlayerActivty.class);
        intent.putExtra(FifaBaseActivity.EXTRA_HASH_CODE, activity.hashCode());
        intent.putExtra(PlayerActivty.NAME_EXTRA, player.getName());
        intent.putExtra(PlayerActivty.ID_EXTRA, player.getId());
        intent.putExtra(PlayerActivty.IMAGE_URL_EXTRA, player.getImageUrl());
        intent.putExtra(PlayerActivty.REG_TOKEN_EXTRA, player.getRegistrationToken());
        return intent;
    }

    public static Intent createNewSeriesActivityIntent(Activity activity, Player player) {
        Intent intent = new Intent(activity, CreateSeriesActivity.class);
        intent.putExtra(FifaBaseActivity.EXTRA_HASH_CODE, activity.hashCode());
        intent.putExtra(BasePlayerActivity.NAME_EXTRA, player.getName());
        intent.putExtra(BasePlayerActivity.ID_EXTRA, player.getId());
        intent.putExtra(BasePlayerActivity.IMAGE_URL_EXTRA, player.getImageUrl());
        intent.putExtra(BasePlayerActivity.REG_TOKEN_EXTRA, player.getRegistrationToken());
        return intent;
    }

    public static Intent createMainActivityIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }
}
