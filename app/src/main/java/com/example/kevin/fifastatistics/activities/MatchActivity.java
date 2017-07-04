package com.example.kevin.fifastatistics.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ActivityMatchBinding;
import com.example.kevin.fifastatistics.fragments.MatchFragment;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchProjection;

public class MatchActivity extends FifaBaseActivity {

    private ActivityMatchBinding mBinding;

    public static Intent getLaunchIntent(Context context, MatchProjection projection) {
        Intent intent = new Intent(context, MatchActivity.class);
        intent.putExtra(FifaBaseActivity.EXTRA_HASH_CODE, context.hashCode());
        intent.putExtra(MATCH_PROJECTION, projection);
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
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initFragment() {
        RetrievalManager.getCurrentUser().subscribe(user -> {
            MatchProjection p = (MatchProjection) getIntent().getSerializableExtra(MATCH_PROJECTION);
            Fragment f = MatchFragment.newInstance(p, user);
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
