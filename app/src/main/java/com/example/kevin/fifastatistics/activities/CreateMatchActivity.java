package com.example.kevin.fifastatistics.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ActivityCreateMatchBinding;
import com.example.kevin.fifastatistics.fragments.CreateMatchFragment;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;

public class CreateMatchActivity extends FifaBaseActivity implements CreateMatchFragment.OnMatchUpdatedListener {

    private static final String CHANGE_MADE = "changeMade";

    private Match mMatch;
    private Player mOpponent;
    private boolean mIsPartOfSeries;
    private boolean mIsNewChangeMade;

    public static Intent getPartOfSeriesIntent(Context context, Player opponent, @Nullable Match match) {
        return getLaunchIntent(context, opponent, true, match);
    }

    public static Intent getIndividualMatchIntent(Context context, Player opponent, @Nullable Match match) {
        return getLaunchIntent(context, opponent, false, match);
    }

    private static Intent getLaunchIntent(Context context, Player opponent, boolean isPartOfSeries, Match match) {
        Intent intent = new Intent(context, CreateMatchActivity.class);
        intent.putExtra(OPPONENT, opponent);
        intent.putExtra(SERIES, isPartOfSeries);
        intent.putExtra(MATCH, match);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restoreInstance(savedInstanceState);
        ActivityCreateMatchBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_create_match);
        initToolbar(binding);
        initFragment();
    }

    private void restoreInstance(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            savedInstanceState = getIntent().getExtras();
        }
        mIsPartOfSeries = savedInstanceState.getBoolean(SERIES);
        mOpponent = (Player) savedInstanceState.getSerializable(OPPONENT);
        mMatch = (Match) savedInstanceState.getSerializable(MATCH);
    }

    @SuppressWarnings("ConstantConditions")
    private void initToolbar(ActivityCreateMatchBinding binding) {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initFragment() {
        RetrievalManager.getCurrentUser().subscribe(user -> {
            CreateMatchFragment f = CreateMatchFragment.newInstance(user, mOpponent, mMatch, mIsPartOfSeries);
            getSupportFragmentManager().beginTransaction().replace(R.id.content, f).commit();
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SERIES, mIsPartOfSeries);
        outState.putBoolean(CHANGE_MADE, mIsNewChangeMade);
        outState.putSerializable(OPPONENT, mOpponent);
        outState.putSerializable(MATCH, mMatch);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_match, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_complete_match:
                //TODO
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mMatch != null && mIsNewChangeMade) {
            showConfirmationDialog();
        } else {
            super.onBackPressed();
        }
    }

    private void showConfirmationDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setMessage(getString(R.string.create_match_exit_confirmation));
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.keep_editing), (d, w) -> dialog.dismiss());
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.discard), (d, w) -> finishAfterTransition());
        dialog.show();
    }

    @Override
    public void onMatchUpdate(Match match) {
        mIsNewChangeMade = true;
        mMatch = match;
    }
}
