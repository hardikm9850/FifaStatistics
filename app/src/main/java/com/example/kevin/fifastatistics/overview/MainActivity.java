package com.example.kevin.fifastatistics.overview;

import android.content.Context;
import android.os.Bundle;
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
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FriendsFragment.OnListFragmentInteractionListener {

    NavigationView navigationView = null;
    Toolbar toolbar = null;

    private static final String TAG = "MainActivity";
    private static String title = "";
    private static boolean doShowSearchItem = false;
    private static User currentUser;
    private static Context mContext;
    private static PreferenceHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        initializeImageLoader();
        mHandler = PreferenceHandler.getInstance(mContext);
        currentUser = mHandler.getUser();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String menuFragment = getIntent().getStringExtra("fragment");
        if (menuFragment == null) {
            initializeMainFragment();
        }
        else if (menuFragment.equals("friends")) {
            initializeFriendsFragment();
        }
        else {
            initializeMainFragment();
        }

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initializeDrawer();
    }

    private void initializeImageLoader()
    {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

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
        imageLoader.displayImage(currentUser.imageUrl, profileImage);

        TextView name = (TextView) headerView.findViewById(R.id.username);
        name.setText(currentUser.name);

        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onListFragmentInteraction(Friend friend) {
        System.out.println("Interacted");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(doShowSearchItem);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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
        if (doShowSearchItem)
        {
            doShowSearchItem = false;
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
        if (!doShowSearchItem)
        {
            doShowSearchItem = true;
            this.invalidateOptionsMenu();
        }

        FriendsFragment fragment = new FriendsFragment();
        toolbar.setTitle("Friends");
        title = "Friends";
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
