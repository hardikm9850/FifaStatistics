package com.example.kevin.fifastatistics.overview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.SecondFragment;
import com.example.kevin.fifastatistics.friendsfragment.FriendsFragment;
import com.example.kevin.fifastatistics.user.Friend;
import com.example.kevin.fifastatistics.user.User;
import com.example.kevin.fifastatistics.utils.PreferenceHandler;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class MainActivity
        extends AppCompatActivity
        implements FriendsFragment.OnListFragmentInteractionListener
{
    Toolbar toolbar = null;

    // Properties
    private static final String TAG = "MainActivity";
    private static boolean doShowSearchItem = false;
    private static boolean doShowFriendRequestsItem = false;
    private static int incomingRequestsCount = 0;
    private static User currentUser;
    private static Context mContext;
    private static PreferenceHandler mHandler;
    private static MaterialSearchView mSearchView;

    // Fragment Names
    private static final String OVERVIEW = "Overview";
    private static final String FRIENDS = "Friends";
    private static final String STATISTICS = "Statistics";
    private static final String STARRED = "Starred";
    private static final String SETTINGS = "Settings";

    private static Drawer drawer;

    // ---------------------------------------------------------------------------------------------
    // INITIALIZATION
    // ---------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        initializeImageLoader();
        mHandler = PreferenceHandler.getInstance(mContext);
        currentUser = mHandler.getUser();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSearchView = (MaterialSearchView) findViewById(R.id.search_view);
        initializeSearchView();

        if (currentUser.getIncomingRequests() != null) {
            incomingRequestsCount = currentUser.getIncomingRequests().size();
        }

        initializeDrawer();

        String menuFragment = getIntent().getStringExtra("fragment");
        menuFragment = (menuFragment == null) ? OVERVIEW : menuFragment;
        Log.i(TAG, "Fragment: " + menuFragment);
        switch (menuFragment) {
            case (FRIENDS):
                initializeFriendsFragment();
                break;
            case (OVERVIEW):
                initializeMainFragment();
                break;
            default:
                initializeMainFragment();
                break;
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        }
    }

    private void initializeDrawer()
    {
        BadgeStyle badgeStyle = new BadgeStyle().withColorRes(R.color.colorAccent).withCornersDp(20);

        PrimaryDrawerItem overviewItem = new PrimaryDrawerItem()
                .withName(R.string.overview)
                .withIcon(R.drawable.ic_home_black_24dp)
                .withIconTintingEnabled(true);

        PrimaryDrawerItem statisticsItem = new PrimaryDrawerItem()
                .withName(R.string.statistics)
                .withIcon(R.drawable.ic_assessment_black_24dp)
                .withIconTintingEnabled(true);

        PrimaryDrawerItem friendsItem = new PrimaryDrawerItem()
                .withName(R.string.friends)
                .withIcon(R.drawable.ic_group_black_24dp)
                .withBadge(String.valueOf(incomingRequestsCount))
                .withBadgeStyle(badgeStyle)
                .withIdentifier(1)
                .withIconTintingEnabled(true);

        PrimaryDrawerItem starredItem = new PrimaryDrawerItem()
                .withName(R.string.starred)
                .withIcon(R.drawable.ic_star_black_24dp)
                .withIconTintingEnabled(true);

        PrimaryDrawerItem settingsItem = new PrimaryDrawerItem()
                .withName(R.string.settings)
                .withIcon(R.drawable.ic_settings_black_24dp)
                .withIconTintingEnabled(true);

        //initialize and create the image loader logic
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(currentUser.getImageUrl(), imageView);
            }});

        // Create the account header
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName(currentUser.getName())
                                .withEmail("FIFA Legend")
                                .withIcon(currentUser.getImageUrl())
                )
                .build();

        // Create the drawer
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .withToolbar(toolbar)
                .addDrawerItems(
                        overviewItem,
                        statisticsItem,
                        friendsItem,
                        starredItem,
                        new DividerDrawerItem(),
                        settingsItem
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (position == 1) {
                            initializeMainFragment();
                        } else if (position == 2) {
                            initializeSecondFragment();
                        } else if (position == 3) {
                            initializeFriendsFragment();
                        }

                        return false;
                    }
                })
                .build();
    }

    private void initializeImageLoader()
    {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
    }

    private void initializeSearchView() {
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        Log.i(TAG, "Setting on search view listener####");
        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                Log.i(TAG, "Showing Search View!");
            }

            @Override
            public void onSearchViewClosed() {
                drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                Log.i(TAG, "Closing Search View!");
            }
        });

        mSearchView.setVoiceSearch(false);
        mSearchView.setHint("Search Users");
        mSearchView.setHintTextColor(Color.LTGRAY);
    }

    @Override
    public void onListFragmentInteraction(Friend friend) {
        System.out.println("Interacted");
    }

    // ---------------------------------------------------------------------------------------------
    // IMPLEMENTATION
    // ---------------------------------------------------------------------------------------------

    @Override
    public void onBackPressed()
    {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(doShowSearchItem);
        mSearchView.setMenuItem(searchItem);

        MenuItem requestsItem = menu.findItem(R.id.friend_requests);
        requestsItem.setVisible(doShowFriendRequestsItem);

        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.friend_requests:
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    // ---------------------------------------------------------------------------------------------
    // FRAGMENT INITIALIZATION
    // ---------------------------------------------------------------------------------------------

    private void initializeMainFragment() {
        boolean invalidateOptions = false;

        if (doShowSearchItem) {
            doShowSearchItem = false;
            invalidateOptions = true;
        }
        if (doShowFriendRequestsItem) {
            doShowFriendRequestsItem = false;
            invalidateOptions = true;
        }
        if (invalidateOptions) {
            this.invalidateOptionsMenu();
        }

        OverviewFragment fragment = new OverviewFragment();
        toolbar.setTitle(OVERVIEW);
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void initializeSecondFragment() {
        SecondFragment fragment = new SecondFragment();
        toolbar.setTitle(STATISTICS);
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void initializeFriendsFragment() {
        boolean invalidateOptions = false;

        if (!doShowSearchItem) {
            doShowSearchItem = true;
            invalidateOptions = true;
        }

        if (incomingRequestsCount > 0) {
            doShowFriendRequestsItem = true;
            invalidateOptions = true;
        }

        if (invalidateOptions) {
            this.invalidateOptionsMenu();
        }

        FriendsFragment fragment = new FriendsFragment();
        toolbar.setTitle(FRIENDS);

        int viewToShow = getIntent().getIntExtra("view", 0);
        getIntent().removeExtra("view");
        Bundle bundle = new Bundle();
        bundle.putInt("view", viewToShow);
        fragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
