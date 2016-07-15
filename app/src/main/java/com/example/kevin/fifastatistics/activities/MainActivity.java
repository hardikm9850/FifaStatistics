package com.example.kevin.fifastatistics.activities;

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
import com.example.kevin.fifastatistics.views.wrappers.FifaNavigationDrawer;
import com.example.kevin.fifastatistics.views.adapters.ViewPagerAdapter;

/**
 * The application's main activity class that is loaded on launch, so long as the user is signed in.
 * If the user is not signed in, then {@link SignInActivity} will be launched.
 */
public class MainActivity extends FifaActivity
        implements FriendsFragment.OnListFragmentInteractionListener {

    public static final String PAGE_EXTRA = "page";
    public static final String FRAGMENT_EXTRA = "fragment";

    private Toolbar mToolbar;
    private FifaNavigationDrawer mDrawer;
    private ViewPagerAdapter mAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FifaFragment currentFragment;

    private int currentDrawerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeToolbar();
        initializeViewPager();
        initializeDrawer();
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

    private void initializeFragment() {
        FragmentInitializer initializer = FragmentInitializerFactory.createFragmentInitializer(this);
        prepareActivityForFragments(initializer);
    }

    private void prepareActivityForFragments(FragmentInitializer initializer) {
        initializer.setActivityTitle(this);
        initializer.changeAdapterDataSet(mAdapter);
        initializer.setTabLayoutVisibility(mTabLayout);

        int currentPage = getIntent().getIntExtra(PAGE_EXTRA, 0);
        mViewPager.setCurrentItem(currentPage);
        currentFragment = (FifaFragment) mAdapter.getItem(currentPage);
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
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public void setNavigationLocked(boolean locked) {
        mDrawer.setLocked(locked);
    }
}
