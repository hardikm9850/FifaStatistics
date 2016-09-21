package com.example.kevin.fifastatistics.views.notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.models.notifications.notificationbundles.NewMatchBundle;
import com.example.kevin.fifastatistics.models.notifications.notificationbundles.NotificationBundle;
import com.example.kevin.fifastatistics.network.ApiAdapter;
import com.example.kevin.fifastatistics.utils.IntentFactory;
import com.example.kevin.fifastatistics.utils.ObservableUtils;
import com.example.kevin.fifastatistics.utils.UserUtils;

public class MatchNotification extends FifaNotification {

    public static final int NOTIFICATION_ID = 3;

    private PendingIntent mContentIntent;
    private NewMatchBundle mBundle;

    public MatchNotification(Context c, Bundle bundle) {
        super(c);
        this.mBundle = new NewMatchBundle(bundle);

        initializeContentIntent(c);
    }

    private void initializeContentIntent(Context c) {
        Intent intent = IntentFactory.createMainActivityIntent(c);
        mContentIntent = PendingIntent.getActivity(c, NOTIFICATION_ID, intent, PendingIntent.FLAG_ONE_SHOT);
        //TODO
    }

    @Override
    protected NotificationBundle getNotificationBundle() {
        return mBundle;
    }

    @Override
    protected void setContentIntent() {
        mNotificationBuilder.setContentIntent(mContentIntent);
    }

    @Override
    protected int getNotificationId() {
        return NOTIFICATION_ID;
    }

    @Override
    public void performPreSendActions() {
        Log.d("MATCH NOTIFICATION", "performing pre send actions");
        User user = SharedPreferencesManager.getUser();
        ApiAdapter.getFifaApi().getMatch(mBundle.getMatchId())
                .compose(ObservableUtils.applyImmediateSchedulers())
                .subscribe(match -> {
                    Log.d("MATCH NOTIFICATION", "adding match");
                    user.addMatch(match);
                    UserUtils.updateUserSync(user).subscribe();
                    Log.d("MATCH NOTIFICATION", "updated user");
                });
        Log.d("MATCH NOTIFICATION", "performed actions");
    }

}
