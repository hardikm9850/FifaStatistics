package com.example.kevin.fifastatistics.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    private static int incomingRequestsCount = 0;
    private static User mUser;
    private static Context mContext;
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
        SharedPreferencesManager preferencesManager = SharedPreferencesManager.getInstance(mContext);
        mUser = preferencesManager.getUser();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (mUser.getIncomingRequests() != null) {
            incomingRequestsCount = mUser.getIncomingRequests().size();
        }

        initializeDrawer();

        String menuFragment = getIntent().getStringExtra("fragment");
        menuFragment = (menuFragment == null) ? Constants.OVERVIEW_FRAGMENT : menuFragment;
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

    /**
     * Initialize the App's main Navigation Drawer
     */
    private void initializeDrawer()
    {
        Log.i(TAG, "Initializing drawer");
        mDrawer = new FifaNavigationDrawer(toolbar, mUser, incomingRequestsCount, this);
        mDrawer.getDrawer().setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener()
        {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem)
            {
                if (mDrawer.getPreviousSelectedPosition() == position) {
                    mDrawer.setPreviousSelectedPosition(position);
                    mDrawer.getDrawer().closeDrawer();
                    return true;
                }

                mDrawer.setPreviousSelectedPosition(position);
                if (position == 1) {
                    initializeMainFragment();
                }
                else if (position == 2) {
                    initializeSecondFragment();
                }
                else if (position == 3) {
                    initializeFriendsFragment();
                }
                return false;
            }
        });
    }

    /**
     * Define the default Universal Image Loader Options
     */
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
        FriendsFragment.closeSearchView();
        super.onBackPressed();
    }

    public static void lockNavigationDrawer()
    {
        mDrawer.lock();
    }

    public static void unlockNavigationDrawer()
    {
        mDrawer.unlock();
    }

    // ---------------------------------------------------------------------------------------------
    // FRAGMENT INITIALIZATION
    // ---------------------------------------------------------------------------------------------

    private void initializeMainFragment()
    {
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

    private void initializeFriendsFragment()
    {
        Log.i(TAG, "initializing friends fragment######################");
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
