package com.example.kevin.fifastatistics.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;

public abstract class FifaActivity extends AppCompatActivity {

    public abstract Toolbar getToolbar();
    public abstract void setNavigationLocked(boolean locked);
    public abstract View getParentLayout();
    public abstract void setProgressBarVisible(boolean visible);
}
