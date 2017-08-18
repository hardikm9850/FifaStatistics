package com.example.kevin.fifastatistics.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.event.EventBus;
import com.example.kevin.fifastatistics.event.SeriesRemovedEvent;
import com.example.kevin.fifastatistics.fragments.CreateSeriesMatchListFragment;
import com.example.kevin.fifastatistics.interfaces.OnMatchCreatedListener;
import com.example.kevin.fifastatistics.interfaces.OnSeriesCompletedListener;
import com.example.kevin.fifastatistics.interfaces.OnSeriesUpdatedListener;
import com.example.kevin.fifastatistics.managers.CurrentSeriesSynchronizer;
import com.example.kevin.fifastatistics.managers.FifaEventManager;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.managers.preferences.PrefsManager;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.match.CurrentSeries;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.network.CreateFailedException;
import com.example.kevin.fifastatistics.utils.SeriesUtils;
import com.example.kevin.fifastatistics.utils.ToastUtils;

import java.util.ArrayList;

import rx.Subscription;

public class CreateSeriesActivity extends BasePlayerActivity implements
        OnSeriesCompletedListener, OnMatchCreatedListener, OnSeriesUpdatedListener {

    private static final String SERIES_KEY = "series";
    private static final String SERIES_ENDED = "ended";

    private FifaEventManager mEventManager;
    private CreateSeriesMatchListFragment mFragment;
    private Series mSeries;
    private User mUser;
    private boolean mIsSeriesCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_series);
        restoreInstance(savedInstanceState);
        initializeToolbar();
        initializeUsers();
    }

    private void restoreInstance(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mSeries = (Series) savedInstanceState.getSerializable(SERIES_KEY);
            mIsSeriesCompleted = savedInstanceState.getBoolean(SERIES_ENDED);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeUsers() {
        Subscription userSub = RetrievalManager.getCurrentUser().subscribe(user -> {
            mUser = user;
            initializeFragment(user);
            initializeEventManager(user);
        });
        addSubscription(userSub);
    }

    private void initializeFragment(User user) {
        Friend opponent = getFriend();
        CurrentSeries savedMatches = PrefsManager.getSeriesPrefs().getCurrentSeriesForOpponent(opponent.getId());
        mSeries = savedMatches != null ? Series.with(savedMatches.getMatches(), Friend.fromPlayer(user), opponent) : null;
        Log.d("SERIES", mSeries != null ? mSeries.toString() : "null");
        mFragment = CreateSeriesMatchListFragment.newInstance(user, getFriend(), mSeries);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, mFragment).commit();
    }

    private void initializeEventManager(User currentUser) {
        mEventManager = FifaEventManager.newInstance(this, currentUser);
        mEventManager.setMatchFlow(this, true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SERIES_KEY, mSeries);
        outState.putBoolean(SERIES_ENDED, mIsSeriesCompleted);
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
                mFragment.notifyCreatingNewMatch();
                mEventManager.startNewFlow(getFriend());
                return true;
            case R.id.menu_item_complete_series :
                if (!isSeriesTeamsSet()) {
                    ToastUtils.showShortToast(this, R.string.error_select_teams);
                    return true;
                }
                uploadSeries();
                return true;
            case android.R.id.home :
                onBackPressed();
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isSeriesTeamsSet() {
        return (mSeries != null && mSeries.getTeamWinner() != null && mSeries.getTeamLoser() != null);
    }

    private void uploadSeries() {
        ProgressDialog d = new ProgressDialog(this);
        d.setTitle(R.string.uploading_series);
        d.setMessage(getString(R.string.please_wait));
        d.setCancelable(false);
        d.show();
        try {
            SeriesUtils.createSeries(mSeries, mUser).subscribe(series -> {
                PrefsManager.getSeriesPrefs().removeCurrentSeries(getPlayerId());
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
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.dialog_discard), (d, w) -> {
            CurrentSeriesSynchronizer.builder().context(this).user(mUser).build().remove(mSeries);
            finish();
        });
        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.dialog_save), (d, w) -> {
            ToastUtils.showShortToast(this, R.string.series_saved);
            finish();
        });
        dialog.show();
    }

    @Override
    public void onMatchCreated(Match match) {
        if (mFragment != null) {
            mFragment.onMatchCreated(match);
        }
    }

    @Override
    public void onSeriesCompleted(Series series) {
        invalidateMenuForSeriesCompletion(true);
        mSeries = series;
    }

    @Override
    public void onSeriesUpdated(Series series) {
        mSeries = series;
    }

    @Override
    public void onUserTeamUpdated(Team team) {
        updateSeriesOnTeamUpdate(team, mUser);
    }

    @Override
    public void onOpponentTeamUpdated(Team team) {
        updateSeriesOnTeamUpdate(team, getFriend());
    }

    private void updateSeriesOnTeamUpdate(Team team, Player player) {
        if (mSeries != null) {
            mSeries.updateTeamForPlayer(team, player);
            saveSeries();
        }
    }

    private void saveSeries() {
        CurrentSeriesSynchronizer s = CurrentSeriesSynchronizer.with(mUser, this);
        s.save(new ArrayList<>(mSeries.getMatches()), getFriend());
    }

    @Override
    public void onSeriesContinued() {
        invalidateMenuForSeriesCompletion(false);
    }

    private void invalidateMenuForSeriesCompletion(boolean isCompleted) {
        mIsSeriesCompleted = isCompleted;
        invalidateOptionsMenu();
    }
}
