package com.example.kevin.fifastatistics.overview;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.SecondFragment;
import com.example.kevin.fifastatistics.friendsfragment.FriendsFragment;
import com.example.kevin.fifastatistics.user.Friend;
import com.example.kevin.fifastatistics.user.User;
import com.example.kevin.fifastatistics.utils.PreferenceHandler;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class MainActivity
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                   FriendsFragment.OnListFragmentInteractionListener
{
    NavigationView navigationView = null;
    Toolbar toolbar = null;

    // Properties
    private static final String TAG = "MainActivity";
    private static String title = "";
    private static boolean doShowSearchItem = false;
    private static boolean doShowFriendRequestsItem = false;
    private static User currentUser;
    private static Context mContext;
    private static PreferenceHandler mHandler;
    private static MaterialSearchView mSearchView;

    // Fragment Names
    private static final String OVERVIEW = "overview";
    private static final String FRIENDS = "friends";
    private static final String STATISTICS = "statistics";
    private static final String STARRED = "starred";
    private static final String SETTINGS = "settings";

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

        String menuFragment = getIntent().getStringExtra("fragment");
        menuFragment = (menuFragment == null) ? OVERVIEW : menuFragment;
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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initializeDrawer();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        }
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

    private void initializeDrawer()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        ImageView profileImage = (ImageView) headerView.findViewById(R.id.profile_image);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(currentUser.getImageUrl(), profileImage);

        TextView name = (TextView) headerView.findViewById(R.id.username);
        name.setText(currentUser.getName());

        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void initializeSearchView()
    {
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
                Log.i(TAG, "Showing Search View!");
            }

            @Override
            public void onSearchViewClosed() {
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
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
    // NAVIGATION
    // ---------------------------------------------------------------------------------------------

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        String itemTitle = item.getTitle().toString();
        if (title.equals(itemTitle))
        {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        title = itemTitle;

        if (id == R.id.nav_overview) {
            initializeMainFragment();
        } else if (id == R.id.nav_statistics) {
            initializeSecondFragment();
        } else if (id == R.id.nav_friends) {
            initializeFriendsFragment();
        } else if (id == R.id.nav_starred) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeMainFragment()
    {
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
        toolbar.setTitle("Overview");
        title = "Overview";
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void initializeSecondFragment() {
        SecondFragment fragment = new SecondFragment();
        toolbar.setTitle("Statistics");
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void initializeFriendsFragment()
    {
        boolean invalidateOptions = false;

        if (!doShowSearchItem)
        {
            doShowSearchItem = true;
            invalidateOptions = true;
        }

        int incomingRequests = 0;
        if (currentUser.getIncomingRequests() != null) {
            incomingRequests = currentUser.getIncomingRequests().size();
        }

        if (incomingRequests > 0) {
            doShowFriendRequestsItem = true;
            invalidateOptions = true;
        }

        if (invalidateOptions) {
            this.invalidateOptionsMenu();
        }


        FriendsFragment fragment = new FriendsFragment();
        toolbar.setTitle("Friends");
        title = "Friends";

        Bundle bundle = new Bundle();
        bundle.putInt("view", 0);
        fragment.setArguments(bundle);

        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

}
