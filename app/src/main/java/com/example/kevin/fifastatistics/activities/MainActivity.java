package com.example.kevin.fifastatistics.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.fragments.FriendsFragment;
import com.example.kevin.fifastatistics.managers.FragmentInitializationManager;
import com.example.kevin.fifastatistics.managers.ImageLoaderManager;
import com.example.kevin.fifastatistics.models.user.Friend;
import com.example.kevin.fifastatistics.utils.factories.NavigationDrawerFactory;
import com.example.kevin.fifastatistics.views.adapters.ViewPagerAdapter;
import com.mikepenz.materialdrawer.Drawer;

public class MainActivity extends FifaActivity
        implements FriendsFragment.OnListFragmentInteractionListener
{
    public static final String PAGE_EXTRA = "page";
    public static final String FRAGMENT_EXTRA = "fragment";

    private static Toolbar mToolbar = null;
    private static Drawer mDrawer;
    private static ViewPagerAdapter mAdapter;
    private static TabLayout mTabLayout;
    private static ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageLoaderManager.initializeDefaultImageLoader(this);

        initializeToolbar();
        initializeViewPager();
        initializeDrawer();
        FragmentInitializationManager.initializeAppropriateFragment(this);
    }

    private void initializeToolbar()
    {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    private void initializeViewPager()
    {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initializeDrawer()
    {
        mDrawer = NavigationDrawerFactory.getDefaultDrawerInstance(this);
    }

    @Override
    public void onListFragmentInteraction(Friend friend) {
        System.out.println("Interacted");
    }

    @Override
    public void onBackPressed() {
        if (FriendsFragment.isSearchOpen()) {
            FriendsFragment.closeSearchView();
            return;
        }
        super.onBackPressed();
    }

    public Drawer getDrawer() {
        return mDrawer;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public ViewPagerAdapter getViewPagerAdapter() {
        return mAdapter;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public TabLayout getTabLayout() {
        return mTabLayout;
    }
}
