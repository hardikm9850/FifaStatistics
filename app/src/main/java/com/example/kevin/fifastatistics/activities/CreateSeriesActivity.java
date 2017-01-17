package com.example.kevin.fifastatistics.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.fragments.CameraFragment;
import com.example.kevin.fifastatistics.fragments.CreateSeriesMatchListFragment;
import com.example.kevin.fifastatistics.interfaces.OnMatchCreatedListener;
import com.example.kevin.fifastatistics.managers.FifaEventManager;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;

import rx.Subscription;

public class CreateSeriesActivity extends BasePlayerActivity {

    private View mParentLayout;
    private Toolbar mToolbar;
    private FifaEventManager mEventManager;
    private CreateSeriesMatchListFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_series);
        initializeToolbar();
        initializeUsers();
    }

    @SuppressWarnings("ConstantConditions")
    private void initializeToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeUsers() {
        Subscription userSub = RetrievalManager.getCurrentUser().subscribe(user -> {
            initializeFragment(user);
            initializeEventManager(user);
        });
        addSubscription(userSub);
    }

    private void initializeFragment(User user) {
        mFragment = CreateSeriesMatchListFragment.newInstance(user, getFriend());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .commit();
    }

    private void initializeEventManager(User currentUser) {
        mEventManager = FifaEventManager.newInstance(this, currentUser);
        mEventManager.setMatchFlow(mFragment);
        setOnBackPressHandler(mEventManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_series, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add_match :
                setOnBackPressHandler(mEventManager);
                mEventManager.startNewFlow(getFriend());
                return true;
            case android.R.id.home :
                onBackPressed();
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mFragment != null && mFragment.isSeriesStarted()) {
            AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setMessage("Are you sure you want to discard this series?");
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "KEEP EDITING", (d, w) -> dialog.dismiss());
            dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DISCARD", (d, w) -> finish());
            dialog.show();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public View getParentLayout() {
        return mParentLayout;
    }
}
