package com.example.kevin.fifastatistics.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.adapters.FragmentAdapter;
import com.example.kevin.fifastatistics.fragments.initializers.FragmentInitializer;
import com.example.kevin.fifastatistics.fragments.initializers.FragmentInitializerFactory;
import com.example.kevin.fifastatistics.interfaces.OnBackPressedHandler;
import com.example.kevin.fifastatistics.interfaces.OnMatchCreatedListener;
import com.example.kevin.fifastatistics.listeners.FabScrollListener;
import com.example.kevin.fifastatistics.managers.FifaEventManager;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.FifaApi;
import com.example.kevin.fifastatistics.utils.BuildUtils;
import com.example.kevin.fifastatistics.utils.ColorUtils;
import com.example.kevin.fifastatistics.utils.FabFactory;
import com.example.kevin.fifastatistics.utils.ObservableUtils;
import com.example.kevin.fifastatistics.views.FifaActionMenu;
import com.example.kevin.fifastatistics.views.FifaNavigationDrawer;
import com.github.clans.fab.FloatingActionButton;
import com.lapism.searchview.SearchView;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;

public class MainActivity extends FifaBaseActivity implements OnMatchCreatedListener, View.OnScrollChangeListener {

    public static final String PAGE_EXTRA = "page";
    private static final String INITIALIZER = "initializer";
    private static final String DRAWER_POSITION = "drawerPosition";

    private Toolbar mToolbar;
    private FifaNavigationDrawer mDrawer;
    private FragmentAdapter mAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FifaActionMenu mActionMenu;
    private FifaEventManager mEventManager;
    private FragmentInitializer mInitializer;
    private FabScrollListener mFabScrollListener;
    private User mUser;
    private int currentDrawerPosition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        restoreInstance(savedInstanceState);
        initializeToolbar();
        initializeViewPager();
        initializeDrawer(savedInstanceState);
        initializeFab();
        initializeFragment();
        checkForHockeyAppUpdates();
        Log.d("token", SharedPreferencesManager.getRegistrationToken());
    }

    private void restoreInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mInitializer = (FragmentInitializer) savedInstanceState.getSerializable(INITIALIZER);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void initializeToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        SearchView sv = (SearchView) findViewById(R.id.searchView);
        sv.setVersion(SearchView.VERSION_MENU_ITEM);
        setSupportActionBar(mToolbar);
    }

    @SuppressWarnings("ConstantConditions")
    private void initializeViewPager() {
        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setOnBackPressHandler((OnBackPressedHandler) mAdapter.getItem(position));
            }
        });

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initializeDrawer(Bundle savedInstanceState) {
        mDrawer = FifaNavigationDrawer.newInstance(this, mToolbar, mColor, savedInstanceState);
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
        if (position == 7) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            mDrawer.setPosition(currentDrawerPosition);
            return;
        }
        Subscription drawerSubscription = Observable.just(position)
                .map(p -> currentDrawerPosition = p)
                .map(p -> FragmentInitializerFactory.createFragmentInitializer(p, mUser))
                .compose(ObservableUtils.applySchedulers())
                .delaySubscription(370, TimeUnit.MILLISECONDS)
                .subscribe(this::prepareActivityForFragments);
        addSubscription(drawerSubscription);
    }

    private void initializeFragment() {
        if (mInitializer == null) {
            mInitializer = FragmentInitializerFactory.createFragmentInitializer(this);
        }
        prepareActivityForFragments(mInitializer);
    }

    private void prepareActivityForFragments(FragmentInitializer initializer) {
        mInitializer = initializer;
        initializer.setActivityTitle(this);
        initializer.changeAdapterDataSet(mAdapter);
        initializer.setTabLayoutVisibility(mTabLayout);
        initializer.setFabVisibility(mActionMenu);
        mTabLayout.setSelectedTabIndicatorColor(mColor);
        int currentPage = getIntent().getIntExtra(PAGE_EXTRA, 0);
        mViewPager.setCurrentItem(currentPage);
        setOnBackPressHandler((OnBackPressedHandler) mAdapter.getItem(currentPage));
    }

    private void initializeFab() {
        mActionMenu = (FifaActionMenu) findViewById(R.id.fab_menu);
        mActionMenu.setGradient(findViewById(R.id.gradient));
        mFabScrollListener = new FabScrollListener(mActionMenu);
        RetrievalManager.getCurrentUser().subscribe(user -> {
            mUser = user;
            initFabWithUser(mUser);
            setupFavoriteTeam(mUser);
        });
    }

    private void initFabWithUser(User user) {
        FabFactory factory = FabFactory.newInstance(this, mColor);
        mEventManager = FifaEventManager.newInstance(this, user);
        initializeAddMatchButton(factory, mEventManager);
        initializeAddSeriesButton(factory, mEventManager);
        setFabColor();
    }

    private void initializeAddMatchButton(FabFactory factory, FifaEventManager manager) {
        FloatingActionButton matchButton = factory.createPlayMatchFab();
        matchButton.setOnClickListener(l -> {
            mActionMenu.close(true);
            setOnBackPressHandler(manager);
            manager.setMatchFlow(false);
            manager.startNewFlow();
        });
        mActionMenu.addMenuButton(matchButton);
    }

    private void initializeAddSeriesButton(FabFactory factory, FifaEventManager manager) {
        FloatingActionButton seriesButton = factory.createPlaySeriesFab();
        seriesButton.setOnClickListener(l -> {
            mActionMenu.close(true);
            manager.setSeriesFlow();
            manager.startNewFlow();
        });
        mActionMenu.addMenuButton(seriesButton);
    }

    private void setupFavoriteTeam(final User user) {
        Subscription sub = Observable.<String>create(s -> {
            Team favTeam = SharedPreferencesManager.getFavoriteTeam();
            if (user.getFavoriteTeamId() != null && favTeam == null) {
                s.onNext(user.getFavoriteTeamId());
            } else {
                s.onNext(null);
            }
        }).flatMap(id -> {
            if (id != null) {
                return FifaApi.getLeagueApi().getTeam(id);
            } else{
                return Observable.empty();
            }
        }).compose(ObservableUtils.applyBackground()).subscribe(new ObservableUtils.OnNextObserver<Team>() {
            @Override
            public void onNext(Team team) {
                if (team != null) {
                    SharedPreferencesManager.setFavoriteTeam(team);
                }
            }
        });
        addSubscription(sub);
    }

    private void checkForHockeyAppUpdates() {
        if (BuildUtils.isHockey()) {
            UpdateManager.register(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CrashManager.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterHockeyAppUpdater();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterHockeyAppUpdater();
    }

    private void unregisterHockeyAppUpdater() {
        if (BuildUtils.isHockey()) {
            UpdateManager.unregister();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = mDrawer.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
        outState.putInt(DRAWER_POSITION, currentDrawerPosition);
        outState.putSerializable(INITIALIZER, mInitializer);
    }

    @Override
    public void onBackPressed() {
        if (mActionMenu.isOpened()) {
            mActionMenu.close(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onColorUpdated() {
        mTabLayout.setSelectedTabIndicatorColor(mColor);
        mDrawer.updateColors(mColor);
        mActionMenu.removeAllMenuButtons();
        initFabWithUser(mUser);
    }

    private void setFabColor() {
        mActionMenu.setMenuButtonColorNormal(mColor);
        mActionMenu.setMenuButtonColorPressed(mColor);
        mActionMenu.getMenuIconView().setImageDrawable(ColorUtils.getTintedDrawable(R.drawable.ic_add_white_24dp, mColor));
    }

    @Override
    public void onMatchCreated(Match match) {
        if (mEventManager != null) {
            mEventManager.onMatchCreated(match);
        }
    }

    @Override
    public void setNavigationLocked(boolean locked) {
        mDrawer.setLocked(locked);
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        mFabScrollListener.onScrollChange(v, scrollX, scrollY, oldScrollX, oldScrollY);
    }
}
