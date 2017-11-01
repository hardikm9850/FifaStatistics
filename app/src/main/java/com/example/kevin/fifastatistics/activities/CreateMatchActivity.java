package com.example.kevin.fifastatistics.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ActivityCreateMatchBinding;
import com.example.kevin.fifastatistics.fragments.CreateMatchFragment;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;

public class CreateMatchActivity extends FifaBaseActivity implements CreateMatchFragment.OnMatchUpdatedListener {

    private static final String CHANGE_MADE = "changeMade";

    private Match mMatch;
    private Player mOpponent;
    private boolean mIsPartOfSeries;
    private boolean mIsNewChangeMade;

    // Only used when part of series
    private Team mUserTeam;
    private Team mOpponentTeam;

    public static Intent getPartOfSeriesIntent(Context context, Player opponent, @Nullable Match match, Team team1, Team team2) {
        return getLaunchIntent(context, opponent, true, match, team1, team2);
    }

    public static Intent getIndividualMatchIntent(Context context, Player opponent, @Nullable Match match) {
        return getLaunchIntent(context, opponent, false, match, null, null);
    }

    private static Intent getLaunchIntent(Context context, Player opponent, boolean
            isPartOfSeries, Match match, Team team1, Team team2) {
        Intent intent = new Intent(context, CreateMatchActivity.class);
        intent.putExtra(OPPONENT, opponent);
        intent.putExtra(SERIES, isPartOfSeries);
        intent.putExtra(MATCH, match);
        intent.putExtra(USER_TEAM, team1);
        intent.putExtra(OPPONENT_TEAM, team2);
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
        mUserTeam = (Team) savedInstanceState.getSerializable(USER_TEAM);
        mOpponentTeam = (Team) savedInstanceState.getSerializable(OPPONENT_TEAM);
    }

    @SuppressWarnings("ConstantConditions")
    private void initToolbar(ActivityCreateMatchBinding binding) {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initFragment() {
        RetrievalManager.getCurrentUser().subscribe(user -> {
            CreateMatchFragment f = CreateMatchFragment.newInstance(user, mOpponent, mMatch,
                    mIsPartOfSeries, mUserTeam, mOpponentTeam);
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
        outState.putSerializable(USER_TEAM, mUserTeam);
        outState.putSerializable(OPPONENT_TEAM, mOpponentTeam);
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
