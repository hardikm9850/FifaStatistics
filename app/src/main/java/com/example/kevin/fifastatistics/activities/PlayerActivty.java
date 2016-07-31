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

    private User mCurrentUser;
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

        mCurrentUser = SharedPreferencesManager.getUser();
        mPlayerId = getIntent().getStringExtra(ID_EXTRA);

//        TextView title = (TextView) findViewById(R.id.title);
//        title.setText(NAME_EXTRA);

        initializeToolbar();
        initializeTabs();
        initializeFab();
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
    private void initializeFab() {
        FloatingActionsMenu menu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        boolean isFriend = getIntent().getBooleanExtra(FRIEND_EXTRA, false);
        if (isFriend) {
            // TODO
        } else {
            if (mCurrentUser.hasIncomingRequestWithId(mPlayerId)) {
                // accept or decline request
            } else if (mCurrentUser.hasOutgoingRequestWithId(mPlayerId)) {
                // no button, request pending
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
