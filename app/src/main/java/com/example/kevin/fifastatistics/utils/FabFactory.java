package com.example.kevin.fifastatistics.utils;

import android.app.Activity;

import com.example.kevin.fifastatistics.R;
import com.getbase.floatingactionbutton.FloatingActionButton;

public class FabFactory {

    public static FloatingActionButton createSendFriendRequestFab(Activity activity) {
        FloatingActionButton button = createButtonWithDefaultAttributes(activity);
        button.setIcon(R.drawable.ic_person_add_white_24dp);
        button.setTitle("Send Friend Request");

        return button;
    }

    public static FloatingActionButton createPlayMatchFab(Activity activity) {
        FloatingActionButton button = createButtonWithDefaultAttributes(activity);
        button.setIcon(R.drawable.soccer);
        button.setTitle("Play Match");

        return button;
    }

    public static FloatingActionButton createPlaySeriesFab(Activity activity) {
        FloatingActionButton button = createButtonWithDefaultAttributes(activity);
        button.setIcon(R.drawable.trophy);
        button.setTitle("Play Series");

        return button;
    }

    private static FloatingActionButton createButtonWithDefaultAttributes(Activity activity) {
        FloatingActionButton button = new FloatingActionButton(activity);
        button.setColorNormalResId(R.color.colorAccent);
        button.setColorPressedResId(R.color.colorAccentDark);
        button.setSize(FloatingActionButton.SIZE_MINI);
        return button;
    }
}
