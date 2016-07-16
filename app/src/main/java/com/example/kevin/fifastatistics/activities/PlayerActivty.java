package com.example.kevin.fifastatistics.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.fragments.SecondFragment;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.views.adapters.ViewPagerAdapter;

public class PlayerActivty extends AppCompatActivity {

    public static final String NAME_EXTRA = "name";
    public static final String IMAGE_URL_EXTRA = "imageurl";
    public static final String ID_EXTRA = "id";

    private User mUser;
    private Toolbar mToolbar;
    private ViewPagerAdapter mAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        setTitle(getIntent().getStringExtra(NAME_EXTRA));

        initializeToolbar();
        initializeTabs();
    }

    private void initializeToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @SuppressWarnings("ConstantConditions")
    private void initializeTabs() {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mAdapter);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        mAdapter.addFragment(new SecondFragment(), "Overview");
        mAdapter.addFragment(new SecondFragment(), "Head to head");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
