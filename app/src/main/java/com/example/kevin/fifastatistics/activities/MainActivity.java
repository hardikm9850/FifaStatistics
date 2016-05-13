package com.example.kevin.fifastatistics.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.fragments.FifaFragment;
import com.example.kevin.fifastatistics.fragments.FriendsFragment;
import com.example.kevin.fifastatistics.managers.FragmentInitializationManager;
import com.example.kevin.fifastatistics.models.user.Friend;
import com.example.kevin.fifastatistics.utils.externalfactories.NavigationDrawerFactory;
import com.example.kevin.fifastatistics.views.adapters.ViewPagerAdapter;
import com.mikepenz.materialdrawer.Drawer;

public class MainActivity extends FifaActivity
        implements FriendsFragment.OnListFragmentInteractionListener
{
    public static final String PAGE_EXTRA = "page";
    public static final String FRAGMENT_EXTRA = "fragment";

    private Toolbar mToolbar;
    private Drawer mDrawer;
    private ViewPagerAdapter mAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FifaFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    @SuppressWarnings("ConstantConditions")
    private void initializeViewPager()
    {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.i("MAIN ACTIVITY", "VIEW CHANGED!");
                currentFragment = (FifaFragment) mAdapter.getItem(position);
            }
        });

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initializeDrawer() {
        mDrawer = NavigationDrawerFactory.getDefaultDrawerInstance(this);
    }

    @Override
    public void onListFragmentInteraction(Friend friend) {

    }

    @Override
    public void onBackPressed() {
        if (currentFragment.handledBackPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void setCurrentFragment(FifaFragment fragment) {
        currentFragment = fragment;
    }

    @Override
    public Drawer getDrawer() {
        return mDrawer;
    }

    @Override
    public ViewPagerAdapter getViewPagerAdapter() {
        return mAdapter;
    }

    @Override
    public ViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public TabLayout getTabLayout() {
        return mTabLayout;
    }
}
