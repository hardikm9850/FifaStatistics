package com.example.kevin.fifastatistics.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.managers.NotificationSender;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.getbase.floatingactionbutton.FloatingActionButton;

public class FabFactory {

    public static FloatingActionButton createSendFriendRequestFab(
            FifaActivity activity, User currentUser, String regTokenOfReceiver) {
        FloatingActionButton b = createButtonWithAttributes(activity, R.string.send_friend_request,
                R.drawable.ic_person_add_white_24dp);

        b.setOnClickListener(l -> handleFriendRequestClick(activity, currentUser, regTokenOfReceiver));
        return b;
    }

    private static void handleFriendRequestClick(FifaActivity activity, User user, String regToken) {
        NotificationSender.sendFriendRequest(user, regToken).subscribe(response -> {
            if (response.isSuccessful()) {
                SnackbarUtils.getShortSnackbar(activity, "Friend request sent").show();
            } else {
                SnackbarUtils.getLongSnackbar(activity, "Failed to send friend request")
                        .setAction("RETRY", v -> handleFriendRequestClick(activity, user, regToken))
                        .setActionTextColor(Color.RED)
                        .show();
                Log.i("ERROR", response.toString());
            }
        });
    }

    private static void showDialogAndSendRequest(User currentUser, String regToken) {

    }

    public static FloatingActionButton createPlayMatchFab(Activity activity) {
        return createButtonWithAttributes(activity, R.string.play_match, R.drawable.soccer);
    }

    public static FloatingActionButton createPlaySeriesFab(Activity activity) {
        return createButtonWithAttributes(activity, R.string.play_series, R.drawable.trophy);
    }

    public static FloatingActionButton createAcceptRequestFab(Activity activity) {
        return createButtonWithAttributes(activity, R.string.accept_friend_request,
                R.drawable.ic_check_white_24dp);
    }

    public static FloatingActionButton createDeclineRequestFab(Activity activity) {
        return createButtonWithAttributes(activity, R.string.decline_friend_request,
                R.drawable.ic_not_interested_white_24dp);
    }

    private static FloatingActionButton createButtonWithAttributes(Activity activity, int titleId, int iconId) {
        FloatingActionButton button = new FloatingActionButton(activity);
        button.setColorNormalResId(R.color.colorAccent);
        button.setColorPressedResId(R.color.colorAccentDark);
        button.setSize(FloatingActionButton.SIZE_MINI);
        button.setIcon(iconId);
        button.setTitle(ResourceUtils.getStringFromResourceId(titleId));

        return button;
    }
}
