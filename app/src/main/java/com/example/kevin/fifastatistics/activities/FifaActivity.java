package com.example.kevin.fifastatistics.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.kevin.fifastatistics.fragments.FifaFragment;
import com.example.kevin.fifastatistics.views.adapters.ViewPagerAdapter;
import com.mikepenz.materialdrawer.Drawer;

public abstract class FifaActivity extends AppCompatActivity
{
    public abstract Toolbar getToolbar();
    public abstract ViewPagerAdapter getViewPagerAdapter();
    public abstract ViewPager getViewPager();
    public abstract TabLayout getTabLayout();
    public abstract Drawer getDrawer();
    public abstract void setCurrentFragment(FifaFragment fragment);
}
