package com.example.kevin.fifastatistics.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.fragments.FifaFragment;
import com.example.kevin.fifastatistics.fragments.FriendsFragment;
import com.example.kevin.fifastatistics.fragments.initializers.FragmentInitializer;
import com.example.kevin.fifastatistics.fragments.initializers.FragmentInitializerFactory;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.utils.FabFactory;
import com.example.kevin.fifastatistics.utils.IntentFactory;
import com.example.kevin.fifastatistics.views.wrappers.FifaNavigationDrawer;
import com.example.kevin.fifastatistics.views.adapters.ViewPagerAdapter;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

/**
 * The application's main activity class that is loaded on launch, so long as the user is signed in.
 * If the user is not signed in, then {@link SignInActivity} will be launched.
 */
public class MainActivity extends FifaActivity
        implements FriendsFragment.FriendsFragmentInteractionListener {

    public static final String PAGE_EXTRA = "page";
    public static final String FRAGMENT_EXTRA = "fragment";

    private Toolbar mToolbar;
    private FifaNavigationDrawer mDrawer;
    private ViewPagerAdapter mAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FloatingActionsMenu mActionMenu;
    private FifaFragment currentFragment;

    private int currentDrawerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeToolbar();
        initializeViewPager();
        initializeDrawer();
        initializeFab();
        initializeFragment();
    }

    private void initializeToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    @SuppressWarnings("ConstantConditions")
    private void initializeViewPager() {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentFragment = (FifaFragment) mAdapter.getItem(position);
            }
        });

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initializeDrawer() {
        mDrawer = FifaNavigationDrawer.getInstance(this);
        mDrawer.setOnDrawerItemClickListener((view, position, drawerItem) -> {
            if (position == currentDrawerPosition) {
                mDrawer.closeDrawer();
                return true;
            } else {
                currentDrawerPosition = position;
                FragmentInitializer initializer = FragmentInitializerFactory
                        .createFragmentInitializer(currentDrawerPosition);
                prepareActivityForFragments(initializer);
                return false;
            }
        });
    }

    private void initializeFab() {
        mActionMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        FloatingActionButton matchButton = FabFactory.createPlayMatchFab(this);
        FloatingActionButton seriesButton = FabFactory.createPlaySeriesFab(this);

        mActionMenu.addButton(matchButton);
        mActionMenu.addButton(seriesButton);
    }

    private void initializeFragment() {
        FragmentInitializer initializer = FragmentInitializerFactory.createFragmentInitializer(this);
        prepareActivityForFragments(initializer);
    }

    private void prepareActivityForFragments(FragmentInitializer initializer) {
        initializer.setActivityTitle(this);
        initializer.changeAdapterDataSet(mAdapter);
        initializer.setTabLayoutVisibility(mTabLayout);
        initializer.setFabVisibility(mActionMenu);

        int currentPage = getIntent().getIntExtra(PAGE_EXTRA, 0);
        mViewPager.setCurrentItem(currentPage);
        currentFragment = (FifaFragment) mAdapter.getItem(currentPage);
    }

    @Override
    public void onFriendsFragmentInteraction(Friend friend) {
        Intent intent = IntentFactory.createPlayerActivityIntent(this, friend);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (currentFragment.handleBackPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public void setNavigationLocked(boolean locked) {
        mDrawer.setLocked(locked);
    }
}
