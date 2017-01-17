package com.example.kevin.fifastatistics.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.fragments.PlayerOverviewFragment;
import com.example.kevin.fifastatistics.fragments.SecondFragment;
import com.example.kevin.fifastatistics.managers.FifaEventManager;
import com.example.kevin.fifastatistics.managers.NotificationSender;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.FabFactory;
import com.example.kevin.fifastatistics.utils.SnackbarUtils;
import com.example.kevin.fifastatistics.views.adapters.ViewPagerAdapter;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.nostra13.universalimageloader.core.ImageLoader;

import rx.Subscription;

public class PlayerActivty extends BasePlayerActivity
        implements PlayerOverviewFragment.OnPlayerFragmentInteractionListener {

    private View mParentLayout;
    private Toolbar mToolbar;
    private FloatingActionsMenu mFam;
    private User mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        setTitle(getIntent().getStringExtra(NAME_EXTRA));

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
        mParentLayout = findViewById(R.id.coordinator_layout);
        mFam = (FloatingActionsMenu) findViewById(R.id.fab_menu);
    }

    @SuppressWarnings("ConstantConditions")
    private void initializeToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText(getName());

        ImageView userImage = (ImageView) findViewById(R.id.toolbar_profile_imageview);
        ImageLoader.getInstance().displayImage(getImageUrl(), userImage);
    }

    @SuppressWarnings("ConstantConditions")
    private void initializeTabs() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        ViewPager vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(adapter);
        TabLayout tl = (TabLayout) findViewById(R.id.tabs);
        tl.setupWithViewPager(vp);

        adapter.addFragment(PlayerOverviewFragment.newInstance(getPlayerId()), "Overview");
        adapter.addFragment(new SecondFragment(), "Head to head");
        adapter.notifyDataSetChanged();
    }

    @SuppressWarnings("ConstantConditions")
    private void initializeFab(boolean isFriend) {
        FabFactory factory = FabFactory.newInstance(this);
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
            mFam.collapse();
            setOnBackPressHandler(manager);
            manager.setMatchFlow();
            manager.startNewFlow(friend);
        });
        mFam.addButton(matchButton);
    }

    private void initializeAddSeriesButton(FabFactory factory, FifaEventManager manager, Friend friend) {
        FloatingActionButton seriesButton = factory.createPlaySeriesFab();
        seriesButton.setOnClickListener(l -> {
            mFam.collapse();
            manager.setSeriesFlow();
            manager.startNewFlow(friend);
        });
        mFam.addButton(seriesButton);
    }

    private void initializeFamForIncomingFriendRequest(Friend friend, FabFactory factory) {
        FloatingActionButton acceptButton = factory.createAcceptRequestFab();
        FloatingActionButton declineButton = factory.createDeclineRequestFab();
        acceptButton.setOnClickListener(l -> {
            mFam.collapse();
            handleAcceptFriendRequestClick(friend, acceptButton, declineButton, factory);
        });
        declineButton.setOnClickListener(l -> {
            mFam.collapse();
            handleDeclineFriendRequestClick(friend, acceptButton, declineButton, factory);
        });
        mFam.addButton(declineButton);
        mFam.addButton(acceptButton);
    }

    private void handleAcceptFriendRequestClick(Friend friend, FloatingActionButton acceptButton,
                                                FloatingActionButton declineButton, FabFactory factory) {
        Subscription acceptSub = NotificationSender.acceptFriendRequest(mCurrentUser, getRegToken()).subscribe(response -> {
            if (response.isSuccessful()) {
                SnackbarUtils.getShortSnackbar(this, "Friend request accepted").show();
                mCurrentUser.acceptIncomingRequest(friend);
                SharedPreferencesManager.storeUser(mCurrentUser);
                mFam.removeButton(acceptButton);
                mFam.removeButton(declineButton);
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
               mFam.removeButton(declineButton);
               mFam.removeButton(acceptButon);
               initializeFabForNonFriend(friend, factory);
           } else {
               SnackbarUtils.getRetrySnackbar(this, "Failed to decline request",
                       v -> handleDeclineFriendRequestClick(friend, acceptButon, declineButton, factory));
           }
        });
        addSubscription(declineSub);
    }

    private void initializeFabForOutgoingFriendRequest(FabFactory factory) {
        mFam.addButton(factory.createFriendRequestPendingFab());
    }

    private void initializeFabForNonFriend(Friend friend, FabFactory factory) {
        FloatingActionButton b = factory.createSendFriendRequestFab();
        b.setOnClickListener(l -> {
            mFam.collapse();
            handleSendFriendRequestClick(friend, b, factory);
        });
        mFam.addButton(b);
    }

    private void handleSendFriendRequestClick(Friend friend, FloatingActionButton b,
                                              FabFactory factory) {
        Subscription sendSub = NotificationSender.sendFriendRequest(mCurrentUser, friend.getRegistrationToken()).subscribe(response -> {
            if (response.isSuccessful()) {
                SnackbarUtils.getShortSnackbar(this, "Friend request sent").show();
                mCurrentUser.addOutgoingRequest(friend);
                SharedPreferencesManager.storeUser(mCurrentUser);
                mFam.removeButton(b);
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
        if (mFam.isExpanded()) {
            mFam.collapseImmediately();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
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
        return mParentLayout;
    }
}
