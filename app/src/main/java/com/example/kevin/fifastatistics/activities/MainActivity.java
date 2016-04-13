package com.example.kevin.fifastatistics.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.fragments.FriendsFragment;
import com.example.kevin.fifastatistics.managers.FragmentManager;
import com.example.kevin.fifastatistics.models.Constants;
import com.example.kevin.fifastatistics.models.user.Friend;
import com.example.kevin.fifastatistics.models.user.User;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.utils.NavigationDrawerFactory;
import com.example.kevin.fifastatistics.views.adapters.ViewPagerAdapter;
import com.mikepenz.materialdrawer.Drawer;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class MainActivity
        extends FifaActivity
        implements FriendsFragment.OnListFragmentInteractionListener
{
    public static final String PAGE_EXTRA = "page";
    public static final String FRAGMENT_EXTRA = "fragment";

    private static final String TAG = "MainActivity";
    private static Toolbar mToolbar = null;
    private static User mUser;
    private static Context mContext;
    private static Drawer mDrawer;
    private static ViewPagerAdapter mAdapter;
    private static TabLayout mTabLayout;
    private static ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        getUser();
        initializeImageLoader();
        initializeToolbar();
        initializeViewPager();
        initializeDrawer();
        initializeAppropriateFragment();
    }

    private void getUser()
    {
        SharedPreferencesManager preferencesManager =
                SharedPreferencesManager.getInstance(mContext);
        mUser = preferencesManager.getUser();
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
        mDrawer = NavigationDrawerFactory.getDefaultDrawerInstance(
                this, mToolbar, mUser);
    }


    private void initializeAppropriateFragment()
    {
        String fragment = getFragmentExtra();
        switch (fragment) {
            case (Constants.FRIENDS_FRAGMENT):
                FragmentManager.initializeFriendsFragment(this);
                break;
            case (Constants.OVERVIEW_FRAGMENT):
                FragmentManager.initializeMainFragment(this);
                break;
            default:
                throw new IllegalStateException(fragment + " is not a valid" +
                        " fragment name!");
        }
    }

    private String getFragmentExtra()
    {
        String fragment = getIntent().getStringExtra(FRAGMENT_EXTRA);
        getIntent().removeExtra(FRAGMENT_EXTRA);
        if (fragment == null) {
            fragment = Constants.OVERVIEW_FRAGMENT;
        }

        return fragment;
    }

    private void initializeImageLoader()
    {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onListFragmentInteraction(Friend friend) {
        System.out.println("Interacted");
    }

    @Override
    public void onBackPressed() {
        FriendsFragment.closeSearchView();
        super.onBackPressed();
    }

    public static void lockNavigationDrawer() {
        mDrawer.getDrawerLayout().setDrawerLockMode(DrawerLayout
                .LOCK_MODE_LOCKED_CLOSED);
    }

    public static void unlockNavigationDrawer() {
        mDrawer.getDrawerLayout().setDrawerLockMode(DrawerLayout
                .LOCK_MODE_UNLOCKED);
    }

    public static void hideTabs() {
        mTabLayout.setVisibility(View.GONE);
    }

    public static void showTabs() {
        mTabLayout.setVisibility(View.VISIBLE);
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
