package com.example.kevin.fifastatistics.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.fragments.PlayerOverviewFragment;
import com.example.kevin.fifastatistics.fragments.SecondFragment;
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

public class PlayerActivty extends FifaActivity
        implements PlayerOverviewFragment.OnPlayerFragmentInteractionListener {

    /** The name of the user. */
    public static final String NAME_EXTRA = "name";

    /** The image URL of the user. */
    public static final String IMAGE_URL_EXTRA = "imageurl";

    /** The ID of the user. */
    public static final String ID_EXTRA = "id";

    /** The registration token of the user */
    public static final String REG_TOKEN_EXTRA = "registrationToken";

    /** Whether or not the user is a friend of the current user. */
    public static final String FRIEND_EXTRA = "isFriend";

    private View mParentLayout;
    private Toolbar mToolbar;
    private FloatingActionsMenu mFam;

    private String mPlayerId;
    private String mName;
    private String mImageUrl;
    private String mRegToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        setTitle(getIntent().getStringExtra(NAME_EXTRA));

        mPlayerId = getIntent().getStringExtra(ID_EXTRA);
        mName = getIntent().getStringExtra(NAME_EXTRA);
        mImageUrl = getIntent().getStringExtra(IMAGE_URL_EXTRA);
        mRegToken = getIntent().getStringExtra(REG_TOKEN_EXTRA);
        mParentLayout = findViewById(R.id.coordinator_layout);
        mFam = (FloatingActionsMenu) findViewById(R.id.fab_menu);

//        TextView title = (TextView) findViewById(R.id.title);
//        title.setText(NAME_EXTRA);

        initializeToolbar();
        initializeTabs();
        RetrievalManager.getCurrentUser().subscribe(this::initializeFab);
    }

    @SuppressWarnings("ConstantConditions")
    private void initializeToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        ImageView userImage = (ImageView) findViewById(R.id.avatar);
//        ImageLoader.getInstance().displayImage(getIntent().getStringExtra(IMAGE_URL_EXTRA), userImage);
    }

    @SuppressWarnings("ConstantConditions")
    private void initializeTabs() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        ViewPager vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(adapter);
        TabLayout tl = (TabLayout) findViewById(R.id.tabs);
        tl.setupWithViewPager(vp);

        adapter.addFragment(PlayerOverviewFragment.newInstance(mPlayerId), "Overview");
        adapter.addFragment(new SecondFragment(), "Head to head");
        adapter.notifyDataSetChanged();
    }

    @SuppressWarnings("ConstantConditions")
    private void initializeFab(User user) {
        FabFactory factory = FabFactory.newInstance(this);
        boolean isFriend = getIntent().getBooleanExtra(FRIEND_EXTRA, false);
        if (isFriend) {
            initializeFamForFriend(factory);
        } else {
            Friend friend = buildFriend();
            if (user.hasIncomingRequestWithId(mPlayerId)) {
                initializeFamForIncomingFriendRequest(friend, user, factory);
            } else if (user.hasOutgoingRequestWithId(mPlayerId)) {
                initializeFabForOutgoingFriendRequest(factory);
            } else {
                initializeFabForNonFriend(friend, user, factory);
            }
        }
    }

    private void initializeFamForFriend(FabFactory factory) {
        FloatingActionButton matchButton = factory.createPlayMatchFab();
        FloatingActionButton seriesButton = factory.createPlaySeriesFab();
        mFam.addButton(matchButton);
        mFam.addButton(seriesButton);
    }

    private Friend buildFriend() {
        return Friend.builder()
                .id(mPlayerId)
                .imageUrl(mImageUrl)
                .name(mName)
                .registrationToken(mRegToken)
                .build();
    }

    private void initializeFamForIncomingFriendRequest(Friend friend, User user, FabFactory factory) {
        FloatingActionButton acceptButton = factory.createAcceptRequestFab();
        acceptButton.setOnClickListener(l -> {
            mFam.collapse();
            handleAcceptFriendRequestClick(friend, user, acceptButton, factory);
        });
        FloatingActionButton declineButton = factory.createDeclineRequestFab();
        declineButton.setOnClickListener(l -> {
            mFam.collapse();
            handleDeclineFriendRequestClick(friend, user, declineButton, factory);
        });
        mFam.addButton(declineButton);
        mFam.addButton(acceptButton);
    }

    private void handleAcceptFriendRequestClick(Friend friend, User user,
                                                FloatingActionButton acceptButton, FabFactory factory) {
        NotificationSender.acceptFriendRequest(user, mRegToken).subscribe(response -> {
            if (response.isSuccessful()) {
                SnackbarUtils.getShortSnackbar(this, "Friend request accepted").show();
                user.acceptIncomingRequest(friend);
                SharedPreferencesManager.storeUser(user);
                mFam.removeButton(acceptButton);
                initializeFamForFriend(factory);
            } else {
                SnackbarUtils.getRetrySnackbar(this, "Failed to accept request",
                        v -> handleAcceptFriendRequestClick(friend, user, acceptButton, factory));
            }
        });
    }

    private void handleDeclineFriendRequestClick(Friend friend, User user,
                                                 FloatingActionButton declineButton, FabFactory factory) {
        NotificationSender.declineFriendRequest(user, mRegToken).subscribe(response -> {
           if (response.isSuccessful()) {
               SnackbarUtils.getShortSnackbar(this, "Friend request declined").show();
               user.declineIncomingRequest(friend);
               SharedPreferencesManager.storeUser(user);
               mFam.removeButton(declineButton);
               initializeFabForNonFriend(friend, user, factory);
           } else {
               SnackbarUtils.getRetrySnackbar(this, "Failed to decline request",
                       v -> handleDeclineFriendRequestClick(friend, user, declineButton, factory));
           }
        });
    }

    private void initializeFabForOutgoingFriendRequest(FabFactory factory) {
        mFam.addButton(factory.createFriendRequestPendingFab());
    }

    private void initializeFabForNonFriend(Friend friend, User user, FabFactory factory) {
        FloatingActionButton b = factory.createSendFriendRequestFab();
        b.setOnClickListener(l -> {
            mFam.collapse();
            handleSendFriendRequestClick(user, friend, b, factory);
        });
        mFam.addButton(b);
    }

    private void handleSendFriendRequestClick(User user, Friend friend, FloatingActionButton b,
                                              FabFactory factory) {
        NotificationSender.sendFriendRequest(user, friend.getRegistrationToken()).subscribe(response -> {
            if (response.isSuccessful()) {
                SnackbarUtils.getShortSnackbar(this, "Friend request sent").show();
                user.addOutgoingRequest(friend);
                SharedPreferencesManager.storeUser(user);
                mFam.removeButton(b);
                initializeFabForOutgoingFriendRequest(factory);
            } else {
                SnackbarUtils.getRetrySnackbar(this, "Failed to send friend request",
                        v -> handleSendFriendRequestClick(user, friend, b, factory)).show();
                Log.i("ERROR", response.toString());
            }
        });
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
    public void setNavigationLocked(boolean locked) {}

    @Override
    public View getParentLayout() {
        return mParentLayout;
    }
}
