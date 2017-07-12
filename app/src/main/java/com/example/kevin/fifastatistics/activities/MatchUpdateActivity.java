package com.example.kevin.fifastatistics.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ActivityMatchUpdateBinding;
import com.example.kevin.fifastatistics.fragments.MatchUpdateFragment;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;

public class MatchUpdateActivity extends FifaBaseActivity {

    private ActivityMatchUpdateBinding mBinding;

    public static Intent getLaunchIntent(Context context, @Nullable MatchUpdate update) {
        return getLaunchIntent(context, update, null);
    }

    public static Intent getLaunchIntent(Context context, @Nullable MatchUpdate update,
                                         @Nullable Match match) {
        Intent intent = new Intent(context, MatchUpdateActivity.class);
        intent.putExtra(FifaBaseActivity.EXTRA_HASH_CODE, context.hashCode());
        intent.putExtra(MATCH, match);
        intent.putExtra(MATCH_UPDATE, update);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_match_update);
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
            Match match = (Match) getIntent().getSerializableExtra(MATCH);
            MatchUpdate update = (MatchUpdate) getIntent().getSerializableExtra(MATCH_UPDATE);
            MatchUpdateFragment f = MatchUpdateFragment.newInstance(update, user, match);
            setOnBackPressHandler(f);
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
