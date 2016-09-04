package com.example.kevin.fifastatistics.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.fragments.FifaFragment;
import com.example.kevin.fifastatistics.fragments.FriendsFragment;
import com.example.kevin.fifastatistics.fragments.initializers.FragmentInitializer;
import com.example.kevin.fifastatistics.fragments.initializers.FragmentInitializerFactory;
import com.example.kevin.fifastatistics.managers.FifaEventManager;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.FabFactory;
import com.example.kevin.fifastatistics.utils.IntentFactory;
import com.example.kevin.fifastatistics.utils.ObservableUtils;
import com.example.kevin.fifastatistics.views.wrappers.FifaNavigationDrawer;
import com.example.kevin.fifastatistics.views.adapters.ViewPagerAdapter;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * The application's main activity class that is loaded on launch, so long as the user is signed in.
 * If the user is not signed in, then {@link SignInActivity} will be launched.
 */
public class MainActivity extends FifaActivity
        implements FriendsFragment.FriendsFragmentInteractionListener {

    public static final String PAGE_EXTRA = "page";
    public static final String FRAGMENT_EXTRA = "fragment";

    private View mCoordinatorLayout;
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

        mCoordinatorLayout = findViewById(R.id.coordinator_layout);
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
        mDrawer = FifaNavigationDrawer.newInstance(this);
        mDrawer.setOnDrawerItemClickListener((view, position, drawerItem) -> {
            if (position == currentDrawerPosition) {
                mDrawer.closeDrawer();
                return true;
            } else {
                handleDrawerClick(position);
                return false;
            }
        });
    }

    private void handleDrawerClick(int position) {
        Observable.just(position)
                .map(p -> currentDrawerPosition = p)
                .map(FragmentInitializerFactory::createFragmentInitializer)
                .compose(ObservableUtils.applySchedulers())
                .delaySubscription(450, TimeUnit.MILLISECONDS)
                .subscribe(this::prepareActivityForFragments);
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

    private void initializeFab() {
        mActionMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        RetrievalManager.getCurrentUser().subscribe(user -> {
            FabFactory factory = FabFactory.newInstance(this);
            FifaEventManager manager = FifaEventManager.newInstance(this, user);

            initializeAddMatchButton(factory, manager);
            initializeAddSeriesButton(factory, manager);
        });
    }

    private void initializeAddMatchButton(FabFactory factory, FifaEventManager manager) {
        FloatingActionButton matchButton = factory.createPlayMatchFab();
        matchButton.setOnClickListener(l -> {
            manager.setMatchFlow();
            manager.startNewFlow();
        });
        mActionMenu.addButton(matchButton);
    }

    private void initializeAddSeriesButton(FabFactory factory, FifaEventManager manager) {
        FloatingActionButton seriesButton = factory.createPlaySeriesFab();
        seriesButton.setOnClickListener(l -> {
            manager.setSeriesFlow();
            manager.startNewFlow();
        });
        mActionMenu.addButton(seriesButton);
    }

    @Override
    public void onFriendsFragmentInteraction(Friend friend) {
        RetrievalManager.getCurrentUser()
                .map(u -> u.hasFriendWithId(friend.getId()))
                .subscribe(isFriend -> {
                    Intent intent = IntentFactory.createPlayerActivityIntent(this, friend, isFriend);
                    startActivity(intent);
                });
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

    @Override
    public View getParentLayout() {
        return mCoordinatorLayout;
    }
}
