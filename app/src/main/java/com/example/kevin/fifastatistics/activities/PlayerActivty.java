package com.example.kevin.fifastatistics.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.fragments.PlayerOverviewFragment;
import com.example.kevin.fifastatistics.fragments.SecondFragment;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.FabFactory;
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

    /** Whether or not the user is a friend of the current user. */
    public static final String FRIEND_EXTRA = "isFriend";

    private String mPlayerId;
    private Toolbar mToolbar;
    private ViewPagerAdapter mAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        setTitle(getIntent().getStringExtra(NAME_EXTRA));

        mPlayerId = getIntent().getStringExtra(ID_EXTRA);

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
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        mAdapter.addFragment(PlayerOverviewFragment.newInstance(mPlayerId), "Overview");
        mAdapter.addFragment(new SecondFragment(), "Head to head");
        mAdapter.notifyDataSetChanged();
    }

    @SuppressWarnings("ConstantConditions")
    private void initializeFab(User user) {
        FloatingActionsMenu menu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        boolean isFriend = getIntent().getBooleanExtra(FRIEND_EXTRA, false);
        if (isFriend) {
            FloatingActionButton matchButton = FabFactory.createPlayMatchFab(this);
            FloatingActionButton seriesButton = FabFactory.createPlaySeriesFab(this);
            menu.addButton(matchButton);
            menu.addButton(seriesButton);
        } else {
            if (user.hasIncomingRequestWithId(mPlayerId)) {
                FloatingActionButton acceptButton = FabFactory.createAcceptRequestFab(this);
                FloatingActionButton declineButton = FabFactory.createDeclineRequestFab(this);
                menu.addButton(declineButton);
                menu.addButton(acceptButton);
            } else if (user.hasOutgoingRequestWithId(mPlayerId)) {
                // TODO display notice that friend request is pending
                menu.setVisibility(View.GONE);
            } else {
                FloatingActionButton sendRequestButton = FabFactory.createSendFriendRequestFab(this);
                menu.addButton(sendRequestButton);
            }
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
    public void setNavigationLocked(boolean locked) {

    }
}
