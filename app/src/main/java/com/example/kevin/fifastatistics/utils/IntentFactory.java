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
        return getPlayerActivityIntentForClass(activity, player, PlayerActivty.class);
    }

    public static Intent createNewSeriesActivityIntent(Activity activity, Player player) {
        return getPlayerActivityIntentForClass(activity, player, CreateSeriesActivity.class);
    }

    private static Intent getPlayerActivityIntentForClass(Activity activity, Player player,
                                                          Class<? extends BasePlayerActivity> activityClass) {
        Intent intent = new Intent(activity, activityClass);
        intent.putExtra(FifaBaseActivity.EXTRA_HASH_CODE, activity.hashCode());
        intent.putExtra(BasePlayerActivity.NAME_EXTRA, player.getName());
        intent.putExtra(BasePlayerActivity.ID_EXTRA, player.getId());
        intent.putExtra(BasePlayerActivity.IMAGE_URL_EXTRA, player.getImageUrl());
        intent.putExtra(BasePlayerActivity.REG_TOKEN_EXTRA, player.getRegistrationToken());
        intent.putExtra(BasePlayerActivity.FAVORITE_TEAM_ID_EXTRA, player.getFavoriteTeamId());
        return intent;
    }

    public static Intent createMainActivityIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }
}
