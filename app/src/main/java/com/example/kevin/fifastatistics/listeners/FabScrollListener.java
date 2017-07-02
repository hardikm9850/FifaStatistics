package com.example.kevin.fifastatistics.listeners;

import android.view.View;

import com.github.clans.fab.FloatingActionMenu;

public class FabScrollListener implements View.OnScrollChangeListener {

    private final FloatingActionMenu mMenu;

    public FabScrollListener(FloatingActionMenu menu) {
        mMenu = menu;
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        final int downThreshold = 60;
        final int upThreshold = 30;
        if (scrollY > oldScrollY + downThreshold) {
            mMenu.hideMenu(true);
        } else if (scrollY < oldScrollY - upThreshold) {
            mMenu.showMenu(true);
        }
    }
}
