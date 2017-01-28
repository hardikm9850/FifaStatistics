package com.example.kevin.fifastatistics.utils;

import android.view.MenuItem;

public class MenuUtils {

    private static final int DISABLED_ICON_ALPHA_LEVEL = 130;
    private static final int ENABLED_ICON_ALPHA_LEVEL = 255;

    public static void disableItem(MenuItem item) {
        item.setEnabled(false);
        item.getIcon().setAlpha(DISABLED_ICON_ALPHA_LEVEL);
    }

    public static void enableItem(MenuItem item) {
        item.setEnabled(true);
        item.getIcon().setAlpha(ENABLED_ICON_ALPHA_LEVEL);
    }
}
