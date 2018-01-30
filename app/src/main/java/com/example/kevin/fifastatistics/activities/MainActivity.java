package com.example.kevin.fifastatistics.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ActivityMainBinding;
import com.example.kevin.fifastatistics.fragments.EventStreamFragment;
import com.example.kevin.fifastatistics.fragments.FriendsFragment;
import com.example.kevin.fifastatistics.fragments.UserOverviewFragment;
import com.example.kevin.fifastatistics.interfaces.OnBackPressedHandler;
import com.example.kevin.fifastatistics.interfaces.OnMatchCreatedListener;
import com.example.kevin.fifastatistics.listeners.FabScrollListener;
import com.example.kevin.fifastatistics.managers.FifaEventManager;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.managers.preferences.PrefsManager;
import com.example.kevin.fifastatistics.managers.sync.FavoriteTeamSynchronizer;
import com.example.kevin.fifastatistics.managers.sync.FootballersSynchronizer;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.service.UpdateTokenService;
import com.example.kevin.fifastatistics.utils.BottomNavUtils;
import com.example.kevin.fifastatistics.utils.BuildUtils;
import com.example.kevin.fifastatistics.utils.ColorUtils;
import com.example.kevin.fifastatistics.utils.FabFactory;
import com.example.kevin.fifastatistics.views.FifaActionMenu;
import com.github.clans.fab.FloatingActionButton;
import com.lapism.searchview.SearchView;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.UpdateManager;

import rx.Subscription;

public class MainActivity extends FifaBaseActivity implements OnMatchCreatedListener, View.OnScrollChangeListener {

    private static final String SELECTED_NAV_ITEM_ID = "navPosition";

    private ActivityMainBinding mBinding;
    private FifaActionMenu mActionMenu;
    private FifaEventManager mEventManager;
    private FabScrollListener mFabScrollListener;
    private User mUser;
    private int mSelectedNavItemId = R.id.action_overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        restoreInstance(savedInstanceState);
        initToolbar();
        initializeFab();
        initBottomBar();
        initFragment(mSelectedNavItemId);
        syncRegistrationToken();
        syncFootballerCache();
        checkForHockeyAppUpdates();
    }

    private void restoreInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mSelectedNavItemId = savedInstanceState.getInt(SELECTED_NAV_ITEM_ID);
            mUser = (User) savedInstanceState.getSerializable(USER);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void initToolbar() {
        mBinding.searchView.setVersion(SearchView.VERSION_MENU_ITEM);
        setSupportActionBar(mBinding.toolbar);
    }

    private void initializeFab() {
        mActionMenu = mBinding.fabLayout.fabMenu;
        mActionMenu.setGradient(mBinding.fabGradient);
        mFabScrollListener = new FabScrollListener(mActionMenu);
        if (mUser == null) {
            RetrievalManager.getCurrentUser().subscribe(user -> {
                mUser = user;
                performPostUserRetrievalSetup();
            });
        } else {
            performPostUserRetrievalSetup();
        }
    }

    private void performPostUserRetrievalSetup() {
        initFabWithUser(mUser);
        setupFavoriteTeam(mUser);
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

    private void initBottomBar() {
        BottomNavUtils.disableShiftMode(mBinding.bottomNavigation);
        mBinding.bottomNavigation.setSelectedItemId(mSelectedNavItemId);
        mBinding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            mSelectedNavItemId = item.getItemId();
            initFragment(mSelectedNavItemId);
            return true;
        });
    }

    private void initFragment(int itemId) {
        switch (itemId) {
            case R.id.action_overview: {
                showFragment(new UserOverviewFragment(), R.string.overview, View.VISIBLE);
                break;
            }
            case R.id.action_matches: {
                Fragment f = EventStreamFragment.newMatchStreamInstance(mUser);
                showFragment(f, R.string.matches, View.GONE);
                break;
            }
            case R.id.action_series: {
                Fragment f = EventStreamFragment.newSeriesStreamInstance(mUser);
                showFragment(f, R.string.series, View.GONE);
                break;
            }
            case R.id.action_players:
                showFragment(FriendsFragment.newInstance(), R.string.players, View.GONE);
                break;
        }
    }

    private void showFragment(Fragment fragment, @StringRes int titleRes, int actionMenuVisibilty) {
        mActionMenu.setVisibility(actionMenuVisibilty);
        setTitle(getString(titleRes));
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
        setOnBackPressHandler((OnBackPressedHandler) fragment);
    }

    private void syncRegistrationToken() {
        if (!PrefsManager.didSendRegistrationToken()) {
            Intent intent = new Intent(this, UpdateTokenService.class);
            startService(intent);
        }
    }

    private void syncFootballerCache() {
        FootballersSynchronizer synchronizer = new FootballersSynchronizer(this);
        Subscription s = synchronizer.sync();
        addSubscription(s);
    }

    private void setupFavoriteTeam(final User user) {
        Subscription sub = FavoriteTeamSynchronizer.with(user).sync();
        addSubscription(sub);
    }

    private void checkForHockeyAppUpdates() {
        if (BuildUtils.isHockey()) {
            Log.d("HOCKEY", "checking for updates");
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
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_NAV_ITEM_ID, mSelectedNavItemId);
        outState.putSerializable(USER, mUser);
    }

    @Override
    public void onBackPressed() {
        if (mActionMenu.isOpened()) {
            mActionMenu.close(true);
        } else if (performHandlerBackPress()) {
            return;
        } else {
            superOnBackPressed();
        }
    }

    @Override
    protected void onColorUpdated() {
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
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        mFabScrollListener.onScrollChange(v, scrollX, scrollY, oldScrollX, oldScrollY);
    }
}
