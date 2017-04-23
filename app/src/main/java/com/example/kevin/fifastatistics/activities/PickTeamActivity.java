package com.example.kevin.fifastatistics.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.adapters.TeamFragmentAdapter;
import com.example.kevin.fifastatistics.databinding.ActivityPickTeamBinding;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.models.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.league.League;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.utils.ColorUtils;
import com.example.kevin.fifastatistics.utils.ObservableUtils;
import com.example.kevin.fifastatistics.utils.ToastUtils;
import com.example.kevin.fifastatistics.viewmodels.TeamItemViewModel;

import rx.Subscription;

public class PickTeamActivity extends FifaBaseActivity implements TeamItemViewModel.OnTeamClickListener {

    public static final int RESULT_TEAM_PICKED = 101;
    public static final String EXTRA_TEAM = "teamExtra";
    private static final String TAG = "PickTeamActivity";

    private ActivityPickTeamBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_pick_team);
        initToolbar();
        initColors();
        getLeagues();
    }

    @SuppressWarnings("ConstantConditions")
    private void initToolbar() {
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initColors() {
        mBinding.tabs.setSelectedTabIndicatorColor(mColor);
        ColorUtils.setProgressBarColor(mBinding.progressBar, mColor);
    }

    private void getLeagues() {
        Subscription s = RetrievalManager.getLeagues().subscribe(
                new ObservableUtils.EmptyOnCompleteObserver<ApiListResponse<League>>() {
            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "error loading leagues: " + e.getMessage());
                ToastUtils.showShortToast(PickTeamActivity.this, getString(R.string.error_loading_teams));
                finishLoading();
            }

            @Override
            public void onNext(ApiListResponse<League> response) {
                finishLoading();
                initTabsWithResponse(response);
            }
        });
        addSubscription(s);
    }

    private void finishLoading() {
        mBinding.progressBar.setVisibility(View.GONE);
    }

    private void initTabsWithResponse(ApiListResponse<League> response) {
        TeamFragmentAdapter adapter = new TeamFragmentAdapter(getSupportFragmentManager(), response);
        mBinding.viewpager.setAdapter(adapter);
        mBinding.tabs.setupWithViewPager(mBinding.viewpager);
        mBinding.tabs.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTeamClick(Team team) {
        addTeamToActivityResultData(team);
        finish();
    }

    private void addTeamToActivityResultData(Team team) {
        Bundle teamBundle = new Bundle();
        teamBundle.putSerializable(EXTRA_TEAM, team);
        Intent intent = new Intent();
        intent.putExtras(teamBundle);
        setResult(RESULT_TEAM_PICKED, intent);
    }
}
