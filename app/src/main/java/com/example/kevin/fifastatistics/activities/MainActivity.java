package com.example.kevin.fifastatistics.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.fragments.SecondFragment;
import com.example.kevin.fifastatistics.fragments.friendsfragment.FriendsFragment;
import com.example.kevin.fifastatistics.fragments.OverviewFragment;
import com.example.kevin.fifastatistics.models.Constants;
import com.example.kevin.fifastatistics.models.user.Friend;
import com.example.kevin.fifastatistics.models.user.User;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.views.FifaNavigationDrawer;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
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

    private static User mUser;
    private static Context mContext;
    private static SharedPreferencesManager mPreferencesManager;
    private static MaterialSearchView mSearchView;
    private static FifaNavigationDrawer mDrawer;

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
        mPreferencesManager = SharedPreferencesManager.getInstance(mContext);
        mUser = mPreferencesManager.getUser();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSearchView = (MaterialSearchView) findViewById(R.id.search_view);
        initializeSearchView();

        if (mUser.getIncomingRequests() != null) {
            incomingRequestsCount = mUser.getIncomingRequests().size();
        }

        initializeDrawer();

        String menuFragment = getIntent().getStringExtra("fragment");
        menuFragment = (menuFragment == null) ? Constants.OVERVIEW_FRAGMENT : menuFragment;
        Log.i(TAG, "Fragment: " + menuFragment);
        switch (menuFragment) {
            case (Constants.FRIENDS_FRAGMENT):
                initializeFriendsFragment();
                break;
            case (Constants.OVERVIEW_FRAGMENT):
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
        mDrawer = new FifaNavigationDrawer(toolbar, mUser, incomingRequestsCount, this);
        mDrawer.getDrawer().setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
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
        });
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
                mDrawer.getDrawer().getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                Log.i(TAG, "Showing Search View!");
            }

            @Override
            public void onSearchViewClosed() {
                mDrawer.getDrawer().getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
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
        toolbar.setTitle(Constants.OVERVIEW_FRAGMENT);
        FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void initializeSecondFragment() {
        SecondFragment fragment = new SecondFragment();
        toolbar.setTitle(Constants.STATISTICS_FRAGMENT);
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
        toolbar.setTitle(Constants.FRIENDS_FRAGMENT);

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
