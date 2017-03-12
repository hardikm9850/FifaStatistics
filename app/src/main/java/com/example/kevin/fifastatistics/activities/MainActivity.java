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
import com.example.kevin.fifastatistics.managers.FifaEventManager;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.utils.ColorUtils;
import com.example.kevin.fifastatistics.utils.FabFactory;
import com.example.kevin.fifastatistics.utils.ObservableUtils;
import com.example.kevin.fifastatistics.views.FifaNavigationDrawer;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.lapism.searchview.SearchView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;

public class MainActivity extends FifaBaseActivity {

    public static final String PAGE_EXTRA = "page";
    public static final String FRAGMENT_EXTRA = "fragment";

    private View mCoordinatorLayout;
    private Toolbar mToolbar;
    private FifaNavigationDrawer mDrawer;
    private FragmentAdapter mAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private FloatingActionMenu mActionMenu;
    private FloatingActionButton mMatchButton;
    private FloatingActionButton mSeriesButton;
    private FifaEventManager mEventManager;
    private Player mUser;
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
        RetrievalManager.getCurrentUser().subscribe(user -> mUser = user);
        Log.d("token", SharedPreferencesManager.getRegistrationToken());
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

    private void initializeDrawer() {
        mDrawer = FifaNavigationDrawer.newInstance(this, mColor);
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
            return;
        }
        Subscription drawerSubscription = Observable.just(position)
                .map(p -> currentDrawerPosition = p)
                .map(p -> FragmentInitializerFactory.createFragmentInitializer(p, mUser))
                .compose(ObservableUtils.applySchedulers())
                .delaySubscription(450, TimeUnit.MILLISECONDS)
                .subscribe(this::prepareActivityForFragments);
        addSubscription(drawerSubscription);
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
        mTabLayout.setSelectedTabIndicatorColor(mColor);
        int currentPage = getIntent().getIntExtra(PAGE_EXTRA, 0);
        mViewPager.setCurrentItem(currentPage);
        setOnBackPressHandler((OnBackPressedHandler) mAdapter.getItem(currentPage));
    }

    private void initializeFab() {
        mActionMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        Subscription fabSubscription = RetrievalManager.getCurrentUser().subscribe(user -> {
            FabFactory factory = FabFactory.newInstance(this, mColor);
            mEventManager = FifaEventManager.newInstance(this, user);
            initializeAddMatchButton(factory, mEventManager);
            initializeAddSeriesButton(factory, mEventManager);
            setFabColor();
        });
        addSubscription(fabSubscription);
    }

    private void initializeAddMatchButton(FabFactory factory, FifaEventManager manager) {
        mMatchButton = factory.createPlayMatchFab();
        mMatchButton.setOnClickListener(l -> {
            mActionMenu.close(true);
            setOnBackPressHandler(manager);
            manager.setMatchFlow();
            manager.startNewFlow();
        });
        mActionMenu.addMenuButton(mMatchButton);
    }

    private void initializeAddSeriesButton(FabFactory factory, FifaEventManager manager) {
        mSeriesButton = factory.createPlaySeriesFab();
        mSeriesButton.setOnClickListener(l -> {
            mActionMenu.close(true);
            manager.setSeriesFlow();
            manager.startNewFlow();
        });
        mActionMenu.addMenuButton(mSeriesButton);
    }

    @Override
    protected void onColorUpdated() {
        setFabColor();
        mActionMenu.removeAllMenuButtons();
        FabFactory fabFactory = FabFactory.newInstance(this, mColor);
        initializeAddMatchButton(fabFactory, mEventManager);
        initializeAddSeriesButton(fabFactory, mEventManager);
        mTabLayout.setSelectedTabIndicatorColor(mColor);
        mDrawer.updateColors(mColor);
    }

    private void setFabColor() {
        mActionMenu.setMenuButtonColorNormal(mColor);
        mActionMenu.setMenuButtonColorPressed(mColor);
        mActionMenu.getMenuIconView().setImageDrawable(ColorUtils.getTintedDrawable(R.drawable.ic_add_white_24dp, mColor));
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
