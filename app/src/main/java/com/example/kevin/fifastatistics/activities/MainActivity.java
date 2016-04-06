package com.example.kevin.fifastatistics.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.fragments.friendsfragment.FriendsFragment;
import com.example.kevin.fifastatistics.fragments.OverviewFragment;
import com.example.kevin.fifastatistics.models.Constants;
import com.example.kevin.fifastatistics.models.user.Friend;
import com.example.kevin.fifastatistics.models.user.User;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.network.FifaApi;
import com.example.kevin.fifastatistics.network.FifaApiInterface;
import com.example.kevin.fifastatistics.views.FifaNavigationDrawer;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity
        extends AppCompatActivity
        implements FriendsFragment.OnListFragmentInteractionListener
{
    public static final String PAGE_EXTRA = "page";
    public static final String FRAGMENT_EXTRA = "fragment";

    private static Toolbar toolbar = null;

    // Properties
    private static final String TAG = "MainActivity";
    private static int incomingRequestsCount = 0;
    private static User mUser;
    private static Context mContext;

    private static FifaNavigationDrawer mDrawer;
    private static Adapter mAdapter;
    private static TabLayout mTabLayout;
    private static ViewPager mViewPager;

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

        // --------------------------------------------------------
        // ATTEMPT AT VIEW PAGER

        mAdapter = new Adapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        // --------------------------------------------------------

        initializeDrawer();

        createUser();

        String menuFragment = getIntent().getStringExtra(FRAGMENT_EXTRA);
        getIntent().removeExtra(FRAGMENT_EXTRA);
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

    private void createUser()
    {
        User user = new User("Kevin", "kevingrant@gmail.com", "12345", "url.com", "1233213");

        FifaApiInterface api = FifaApi.getService();

        api.createUser(user)
                .flatMap(response -> api.lookupUser(response.headers().get("Location")))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(e -> Log.i("SECOND ACTIVITY", "ERROR: " + e.getMessage()))
                .subscribe(this::logUser);
    }

    private void logUser(User user)
    {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        Log.i("SECOND ACTIVITY", "USER: \n" + json);
    }

    // --------------------------------------------------------------
    // ATTEMPT AT VIEW PAGER

    static class Adapter extends FragmentStatePagerAdapter
    {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }

        public int getItemPosition(Object item) {
            return POSITION_NONE;
        }

        public void clear() {
            mFragments.clear();
            mFragmentTitles.clear();
        }
    }

    // --------------------------------------------------------------

    /**
     * Initialize the App's main Navigation Drawer
     */
    private void initializeDrawer()
    {
        Log.i(TAG, "Initializing drawer");
        mDrawer = new FifaNavigationDrawer(toolbar, mUser, incomingRequestsCount, this);
        mDrawer.getDrawer().setOnDrawerItemClickListener((view, position, drawerItem) -> {

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
        toolbar.setTitle(Constants.OVERVIEW_FRAGMENT);
        mAdapter.clear();
        mAdapter.addFragment(new OverviewFragment(), Constants.OVERVIEW_FRAGMENT);
        mAdapter.notifyDataSetChanged();
        mTabLayout.setVisibility(View.GONE);
    }

    private void initializeSecondFragment() {
//        SecondFragment fragment = new SecondFragment();
//        toolbar.setTitle(Constants.STATISTICS_FRAGMENT);
//        FragmentTransaction fragmentTransaction =
//                getSupportFragmentManager().beginTransaction();
//
//        fragmentTransaction.replace(R.id.fragment_container, fragment);
//        fragmentTransaction.commit();
    }

    private void initializeFriendsFragment()
    {
        toolbar.setTitle(Constants.FRIENDS_FRAGMENT);

        mAdapter.clear();
        mAdapter.addFragment(
                FriendsFragment.newInstance(FriendsFragment.friendsView), Constants.FRIENDS_FRAGMENT);
        mAdapter.addFragment(
                FriendsFragment.newInstance(FriendsFragment.requestsView), "Requests");
        mAdapter.notifyDataSetChanged();

        mViewPager.setCurrentItem(getIntent().getIntExtra(PAGE_EXTRA, 0));
        getIntent().removeExtra(PAGE_EXTRA);
        mTabLayout.setVisibility(View.VISIBLE);
    }
}
