package com.example.kevin.fifastatistics.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.adapters.FragmentAdapter;
import com.example.kevin.fifastatistics.databinding.ActivityPlayerBinding;
import com.example.kevin.fifastatistics.fragments.PlayerOverviewFragment;
import com.example.kevin.fifastatistics.fragments.SecondFragment;
import com.example.kevin.fifastatistics.managers.FifaEventManager;
import com.example.kevin.fifastatistics.managers.NotificationSender;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.ColorUtils;
import com.example.kevin.fifastatistics.utils.FabFactory;
import com.example.kevin.fifastatistics.utils.SnackbarUtils;
import com.example.kevin.fifastatistics.utils.TransitionUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import rx.Subscription;

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
        Subscription userSubscription = RetrievalManager.getCurrentUser()
                .map(user -> mCurrentUser = user)
                .map(user -> user.hasFriendWithId(getPlayerId()))
                .subscribe(this::initializeFab);
        addSubscription(userSubscription);
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
    private void initializeFab(boolean isFriend) {
        FabFactory factory = FabFactory.newInstance(this, mColor);
        Friend friend = getFriend();
        if (isFriend) {
            initializeFamForFriend(factory, friend);
        } else {
            if (mCurrentUser.hasIncomingRequestWithId(getPlayerId())) {
                initializeFamForIncomingFriendRequest(friend, factory);
            } else if (mCurrentUser.hasOutgoingRequestWithId(getPlayerId())) {
                initializeFabForOutgoingFriendRequest(factory);
            } else {
                initializeFabForNonFriend(friend, factory);
            }
        }
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

    private void initializeFamForIncomingFriendRequest(Friend friend, FabFactory factory) {
        FloatingActionButton acceptButton = factory.createAcceptRequestFab();
        FloatingActionButton declineButton = factory.createDeclineRequestFab();
        acceptButton.setOnClickListener(l -> {
            mFam.close(true);
            handleAcceptFriendRequestClick(friend, acceptButton, declineButton, factory);
        });
        declineButton.setOnClickListener(l -> {
            mFam.close(true);
            handleDeclineFriendRequestClick(friend, acceptButton, declineButton, factory);
        });
        mFam.addMenuButton(declineButton);
        mFam.addMenuButton(acceptButton);
    }

    private void handleAcceptFriendRequestClick(Friend friend, FloatingActionButton acceptButton,
                                                FloatingActionButton declineButton, FabFactory factory) {
        Subscription acceptSub = NotificationSender.acceptFriendRequest(mCurrentUser, getRegToken()).subscribe(response -> {
            if (response.isSuccessful()) {
                SnackbarUtils.getShortSnackbar(this, "Friend request accepted").show();
                mCurrentUser.acceptIncomingRequest(friend);
                SharedPreferencesManager.storeUser(mCurrentUser);
                mFam.removeMenuButton(acceptButton);
                mFam.removeMenuButton(declineButton);
                initializeFamForFriend(factory, friend);
            } else {
                SnackbarUtils.getRetrySnackbar(this, "Failed to accept request",
                        v -> handleAcceptFriendRequestClick(friend, acceptButton, declineButton, factory));
            }
        });
        addSubscription(acceptSub);
    }

    private void handleDeclineFriendRequestClick(Friend friend, FloatingActionButton acceptButon,
                                                 FloatingActionButton declineButton, FabFactory factory) {
        Subscription declineSub = NotificationSender.declineFriendRequest(mCurrentUser, getRegToken()).subscribe(response -> {
           if (response.isSuccessful()) {
               SnackbarUtils.getShortSnackbar(this, "Friend request declined").show();
               mCurrentUser.declineIncomingRequest(friend);
               SharedPreferencesManager.storeUser(mCurrentUser);
               mFam.removeMenuButton(declineButton);
               mFam.removeMenuButton(acceptButon);
               initializeFabForNonFriend(friend, factory);
           } else {
               SnackbarUtils.getRetrySnackbar(this, "Failed to decline request",
                       v -> handleDeclineFriendRequestClick(friend, acceptButon, declineButton, factory));
           }
        });
        addSubscription(declineSub);
    }

    private void initializeFabForOutgoingFriendRequest(FabFactory factory) {
        mFam.addMenuButton(factory.createFriendRequestPendingFab());
    }

    private void initializeFabForNonFriend(Friend friend, FabFactory factory) {
        FloatingActionButton b = factory.createSendFriendRequestFab();
        b.setOnClickListener(l -> {
            mFam.close(true);
            handleSendFriendRequestClick(friend, b, factory);
        });
        mFam.addMenuButton(b);
    }

    private void handleSendFriendRequestClick(Friend friend, FloatingActionButton b,
                                              FabFactory factory) {
        Subscription sendSub = NotificationSender.sendFriendRequest(mCurrentUser, friend.getRegistrationToken()).subscribe(response -> {
            if (response.isSuccessful()) {
                SnackbarUtils.getShortSnackbar(this, "Friend request sent").show();
                mCurrentUser.addOutgoingRequest(friend);
                SharedPreferencesManager.storeUser(mCurrentUser);
                mFam.removeMenuButton(b);
                initializeFabForOutgoingFriendRequest(factory);
            } else {
                SnackbarUtils.getRetrySnackbar(this, "Failed to send friend request",
                        v -> handleSendFriendRequestClick(friend, b, factory)).show();
                Log.i("ERROR", response.toString());
            }
        });
        addSubscription(sendSub);
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
