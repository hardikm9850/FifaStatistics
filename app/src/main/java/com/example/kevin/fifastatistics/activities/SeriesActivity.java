package com.example.kevin.fifastatistics.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ActivitySeriesBinding;
import com.example.kevin.fifastatistics.fragments.SeriesFragment;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.SeriesProjection;

public class SeriesActivity extends FifaBaseActivity {

    private ActivitySeriesBinding mBinding;

    public static Intent getLaunchIntent(Context context, SeriesProjection projection) {
        Intent intent = new Intent(context, SeriesActivity.class);
        intent.putExtra(FifaBaseActivity.EXTRA_HASH_CODE, context.hashCode());
        intent.putExtra(SERIES_PROJECTION, projection);
        return intent;
    }

    public static Intent getLaunchIntent(Context context, String seriesId) {
        Intent intent = new Intent(context, SeriesActivity.class);
        intent.putExtra(FifaBaseActivity.EXTRA_HASH_CODE, context.hashCode());
        intent.putExtra(EVENT_ID, seriesId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_series);
        initToolbar();
        initFragment();
    }

    @SuppressWarnings("ConstantConditions")
    private void initToolbar() {
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initFragment() {
        RetrievalManager.getCurrentUser().subscribe(user -> {
            SeriesProjection p = (SeriesProjection) getIntent().getSerializableExtra(SERIES_PROJECTION);
            Fragment f;
            if (p != null) {
                f = SeriesFragment.newInstance(p, user);
            } else {
                f = SeriesFragment.newInstance(getIntent().getStringExtra(EVENT_ID), user);
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.content, f).commit();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
