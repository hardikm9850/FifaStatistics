package com.example.kevin.fifastatistics.utils;

import android.content.Context;
import android.support.annotation.ColorInt;

import com.example.kevin.fifastatistics.R;
import com.github.clans.fab.FloatingActionButton;

/**
 * Factory class for Floating Action Buttons that are a part of Floating Action Menus.
 * <br>
 * The create methods here do not define the onClickListeners, those should be handled by the
 * containing activity. This is more for appearance.
 */
public class FabFactory {

    private Context mContext;
    private int mColor;

    public static FabFactory newInstance(Context context, @ColorInt int buttonColor) {
        return new FabFactory(context, buttonColor);
    }

    private FabFactory(Context context, int color) {
        mContext = context;
        mColor = color;
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
        FloatingActionButton button = new FloatingActionButton(mContext);
        button.setColorNormal(mColor);
        button.setColorPressed(mColor);
        button.setButtonSize(FloatingActionButton.SIZE_MINI);
        button.setImageDrawable(ColorUtils.getTintedDrawable(iconId, mColor));
        button.setLabelText(ResourceUtils.getStringFromResourceId(titleId));

        return button;
    }
}
