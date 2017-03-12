package com.example.kevin.fifastatistics.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.FifaBaseActivity;
import com.github.clans.fab.FloatingActionButton;

/**
 * Factory class for Floating Action Buttons that are a part of Floating Action Menus.
 * <br>
 * The create methods here do not define the onClickListeners, those should be handled by the
 * containing activity. This is more for appearance.
 */
public class FabFactory {

    private FifaBaseActivity mActivity;
    private int mColor;

    public static FabFactory newInstance(FifaBaseActivity activity, @ColorInt int buttonColor) {
        return new FabFactory(activity, buttonColor);
    }

    private FabFactory(FifaBaseActivity activity, int color) {
        mActivity = activity;
        mColor = color;
    }

    public FloatingActionButton createSendFriendRequestFab() {
        return createButtonWithAttributes(R.string.send_friend_request, R.drawable.ic_person_add_white_24dp);
    }

    public FloatingActionButton createFriendRequestPendingFab() {
        FloatingActionButton b = createButtonWithAttributes(
                R.string.friend_request_pending, R.drawable.ic_hourglass_full_white_24dp);
        b.setClickable(false);
        b.setColorNormal(Color.LTGRAY);
        return b;
    }

    public FloatingActionButton createPlayMatchFab() {
        return createButtonWithAttributes(R.string.play_match, R.drawable.soccer);
    }

    public FloatingActionButton createPlaySeriesFab() {
        return createButtonWithAttributes(R.string.play_series, R.drawable.trophy);
    }

    public FloatingActionButton createAcceptRequestFab() {
        return createButtonWithAttributes(R.string.accept_friend_request, R.drawable.ic_check_white_24dp);
    }

    public FloatingActionButton createDeclineRequestFab() {
        return createButtonWithAttributes(R.string.decline_friend_request, R.drawable.ic_not_interested_white_24dp);
    }

    private FloatingActionButton createButtonWithAttributes(int titleId, int iconId) {
        FloatingActionButton button = new FloatingActionButton(mActivity);
        button.setColorNormal(mColor);
        button.setColorPressed(mColor);
        button.setButtonSize(FloatingActionButton.SIZE_MINI);
        button.setImageDrawable(ColorUtils.getTintedDrawable(iconId, mColor));
        button.setLabelText(ResourceUtils.getStringFromResourceId(titleId));

        return button;
    }
}
