package com.example.kevin.fifastatistics.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public abstract class FifaActivity extends AppCompatActivity {

    protected OnBackPressedHandler mBackPressHandler;

    public abstract Toolbar getToolbar();
    public abstract void setNavigationLocked(boolean locked);
    public abstract View getParentLayout();

    @Override
    public void onBackPressed() {
        if (mBackPressHandler != null && mBackPressHandler.handleBackPress()) {
            return;
        }
        super.onBackPressed();
    }

    public interface OnBackPressedHandler {

        /**
         * return true if the handler will handle the back press, or false if handling should
         * fall through to the activity.
         */
        boolean handleBackPress();
    }
}
