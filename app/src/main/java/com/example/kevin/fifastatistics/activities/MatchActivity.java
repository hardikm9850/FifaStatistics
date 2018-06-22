package com.example.kevin.fifastatistics.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ActivityMatchBinding;
import com.example.kevin.fifastatistics.fragments.MatchFragment;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.FifaEvent;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchProjection;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.viewmodels.EventResultHeaderViewModel;
import com.example.kevin.fifastatistics.viewmodels.EventToolbarViewModel;

public class MatchActivity extends FifaBaseActivity implements MatchFragment.OnMatchLoadSuccessListener {

    private ActivityMatchBinding mBinding;

    public static Intent getLaunchIntent(Context context, MatchProjection projection) {
        Intent intent = new Intent(context, MatchActivity.class);
        intent.putExtra(FifaBaseActivity.EXTRA_HASH_CODE, context.hashCode());
        intent.putExtra(MATCH_PROJECTION, projection);
        return intent;
    }

    public static Intent getLaunchIntent(Context context, String matchId) {
        Intent intent = new Intent(context, MatchActivity.class);
        intent.putExtra(FifaBaseActivity.EXTRA_HASH_CODE, context.hashCode());
        intent.putExtra(EVENT_ID, matchId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_match);
        initToolbar();
        initFragment();
    }

    @SuppressWarnings("ConstantConditions")
    private void initToolbar() {
        setSupportActionBar(mBinding.toolbarLayout.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initFragment() {
        RetrievalManager.getCurrentUser().subscribe(user -> {
            MatchProjection p = (MatchProjection) getIntent().getSerializableExtra(MATCH_PROJECTION);
            Fragment f;
            if (p != null) {
                f = MatchFragment.newInstance(p, user);
                initToolbarData(p, user);
            } else {
                f = MatchFragment.newInstance(getIntent().getStringExtra(EVENT_ID), user);
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.content, f).commit();
        });
    }

    private void initToolbarData(FifaEvent projection, User currentUser) {
        EventToolbarViewModel viewModel = new EventToolbarViewModel(projection, currentUser);
        mBinding.toolbarLayout.setViewModel(viewModel);
        View toolbarTitle = mBinding.toolbarLayout.toolbarContent;
        EventResultHeaderViewModel headerViewModel = new EventResultHeaderViewModel(projection, currentUser, toolbarTitle);
        mBinding.setHeaderViewModel(headerViewModel);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMatchLoad(Match match) {
        RetrievalManager.getCurrentUser().subscribe(user -> {
            initToolbarData(match, user);
        });
    }
}
