package com.example.kevin.fifastatistics.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.fragments.CreateSeriesMatchListFragment;
import com.example.kevin.fifastatistics.interfaces.OnSeriesCompletedListener;
import com.example.kevin.fifastatistics.managers.FifaEventManager;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.CreateFailedException;
import com.example.kevin.fifastatistics.utils.SeriesUtils;
import com.example.kevin.fifastatistics.utils.ToastUtils;

import rx.Subscription;

public class CreateSeriesActivity extends BasePlayerActivity implements OnSeriesCompletedListener {

    private View mParentLayout;
    private Toolbar mToolbar;
    private FifaEventManager mEventManager;
    private CreateSeriesMatchListFragment mFragment;
    private Series mSeries;
    private boolean mIsSeriesCompleted;

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
        mFragment = CreateSeriesMatchListFragment.newInstance(user, getFriend(), this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, mFragment)
                .commit();
    }

    private void initializeEventManager(User currentUser) {
        mEventManager = FifaEventManager.newInstance(this, currentUser);
        mEventManager.setMatchFlow(mFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_series, menu);
        setMenuItemVisibility(menu);
        return true;
    }

    private void setMenuItemVisibility(Menu menu) {
        MenuItem addMatchItem = menu.findItem(R.id.menu_item_add_match);
        MenuItem uploadSeriesItem = menu.findItem(R.id.menu_item_complete_series);
        if (mIsSeriesCompleted) {
            addMatchItem.setVisible(false);
            uploadSeriesItem.setVisible(true);
        } else {
            addMatchItem.setVisible(true);
            uploadSeriesItem.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_add_match :
                setOnBackPressHandler(mEventManager);
                mEventManager.startNewFlow(getFriend());
                return true;
            case R.id.menu_item_complete_series :
                uploadSeries();
                return true;
            case android.R.id.home :
                onBackPressed();
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    private void uploadSeries() {
        ProgressDialog d = new ProgressDialog(this);
        d.setTitle(R.string.uploading_series);
        d.setMessage(getString(R.string.please_wait));
        d.setCancelable(false);
        d.show();
        try {
            SeriesUtils.createSeries(mSeries).subscribe(series -> {
                SharedPreferencesManager.removeCurrentSeries();
                d.cancel();
                RetrievalManager.syncCurrentUserWithServer();
                ToastUtils.showShortToast(this, getString(R.string.create_series_success));
                finish();
            });
        } catch (CreateFailedException e) {
            d.cancel();
            ToastUtils.showLongToast(this, getString(R.string.create_series_failed) + ": " + e.getErrorCode().getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        boolean onBackPressHandled = performHandlerBackPress();
        if (onBackPressHandled) {
            return;
        } else if (mFragment != null && mFragment.isSeriesStarted()) {
            showConfirmationDialog();
        } else {
            super.onBackPressed();
        }
    }

    private void showConfirmationDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setMessage(getString(R.string.create_series_exit_confirmation));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dialog_keep_editing), (d, w) -> dialog.dismiss());
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.dialog_discard), (d, w) -> finish());
        dialog.show();
    }

    @Override
    public void onSeriesCompleted(Series series) {
        invalidateMenuForSeriesCompletion(true);
        mSeries = series;
    }

    @Override
    public void onSeriesContinued() {
        invalidateMenuForSeriesCompletion(false);
    }

    private void invalidateMenuForSeriesCompletion(boolean isCompleted) {
        mIsSeriesCompleted = isCompleted;
        invalidateOptionsMenu();
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
