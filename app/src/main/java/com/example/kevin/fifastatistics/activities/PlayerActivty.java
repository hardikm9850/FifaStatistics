package com.example.kevin.fifastatistics.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.adapters.FragmentAdapter;
import com.example.kevin.fifastatistics.databinding.ActivityPlayerBinding;
import com.example.kevin.fifastatistics.fragments.PlayerOverviewFragment;
import com.example.kevin.fifastatistics.fragments.SecondFragment;
import com.example.kevin.fifastatistics.managers.FifaEventManager;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.ColorUtils;
import com.example.kevin.fifastatistics.utils.FabFactory;
import com.example.kevin.fifastatistics.utils.TransitionUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class PlayerActivty extends BasePlayerActivity implements PlayerOverviewFragment.OnPlayerFragmentInteractionListener {

    public static final String EXTRA_ENTERED_FROM_SEARCH_BAR = "enteredFromSearch";

    private ActivityPlayerBinding mBinding;
    private Toolbar mToolbar;
    private FloatingActionMenu mFam;
    private User mCurrentUser;
    private boolean mDidEnterFromSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_player);
        supportPostponeEnterTransition();
        initializeMembers();
        initializeToolbar();
        initializeTabs();
        RetrievalManager.getCurrentUser().subscribe(user -> {
            mCurrentUser = user;
            initializeFab();
        });
    }

    private void initializeMembers() {
        mFam = mBinding.fabMenu;
        mFam.setMenuButtonColorNormal(mColor);
        mFam.setMenuButtonColorPressed(mColor);
        mFam.getMenuIconView().setImageDrawable(ColorUtils.getTintedDrawable(R.drawable.ic_add_white_24dp, mColor));
        mDidEnterFromSearch = getIntent().getExtras().getBoolean(EXTRA_ENTERED_FROM_SEARCH_BAR);
    }

    @SuppressWarnings("ConstantConditions")
    private void initializeToolbar() {
        mToolbar = mBinding.toolbarLayout.toolbar;
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mBinding.toolbarLayout.toolbarTitle.setText(getName());
        mBinding.toolbarLayout.setImageUrl(getImageUrl());
        mBinding.toolbarLayout.setImageCallback(TransitionUtils.getTransitionStartingImageCallback(this));
    }

    @SuppressWarnings("ConstantConditions")
    private void initializeTabs() {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        mBinding.viewpager.setAdapter(adapter);
        mBinding.tabs.setupWithViewPager(mBinding.viewpager);
        mBinding.tabs.setSelectedTabIndicatorColor(mColor);

        adapter.addFragment(PlayerOverviewFragment.newInstance(getPlayerId()), "Overview");
        adapter.addFragment(new SecondFragment(), "Head to head");
        adapter.notifyDataSetChanged();
    }

    @SuppressWarnings("ConstantConditions")
    private void initializeFab() {
        FabFactory factory = FabFactory.newInstance(this, mColor);
        Friend friend = getFriend();
        initializeFamForFriend(factory, friend);
    }

    private void initializeFamForFriend(FabFactory factory, Friend friend) {
        FifaEventManager manager = FifaEventManager.newInstance(this, mCurrentUser);
        initializeAddMatchButton(factory, manager, friend);
        initializeAddSeriesButton(factory, manager, friend);
    }

    private void initializeAddMatchButton(FabFactory factory, FifaEventManager manager, Friend friend) {
        FloatingActionButton matchButton = factory.createPlayMatchFab();
        matchButton.setOnClickListener(l -> {
            mFam.close(true);
            setOnBackPressHandler(manager);
            manager.setMatchFlow();
            manager.startNewFlow(friend);
        });
        mFam.addMenuButton(matchButton);
    }

    private void initializeAddSeriesButton(FabFactory factory, FifaEventManager manager, Friend friend) {
        FloatingActionButton seriesButton = factory.createPlaySeriesFab();
        seriesButton.setOnClickListener(l -> {
            mFam.close(true);
            manager.setSeriesFlow();
            manager.startNewFlow(friend);
        });
        mFam.addMenuButton(seriesButton);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mFam.isOpened()) {
            mFam.close(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDidEnterFromSearch) {
            finish();
        } else {
            supportFinishAfterTransition();
        }
    }

    @Override
    public void onPlayerFragmentInteraction(User user) {

    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public View getParentLayout() {
        return mBinding.coordinatorLayout;
    }
}
