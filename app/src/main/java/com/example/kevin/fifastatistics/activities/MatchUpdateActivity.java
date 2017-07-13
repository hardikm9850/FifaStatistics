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

    private static final String EXTRA_TYPE = "type";

    public enum MatchEditType {
        UPDATE, REVIEW, CREATE;
    }

    private ActivityMatchUpdateBinding mBinding;
    private MatchEditType mType;

    public static Intent getNotificationIntent(Context context, String updateId) {
        Intent intent = getLaunchIntent(context, MatchEditType.REVIEW, null, null);
        intent.putExtra(UPDATE_ID, updateId);
        return intent;
    }

    public static Intent getLaunchIntent(Context context, MatchEditType type, @Nullable MatchUpdate update,
                                         @Nullable Match match) {
        Intent intent = new Intent(context, MatchUpdateActivity.class);
        intent.putExtra(FifaBaseActivity.EXTRA_HASH_CODE, context.hashCode());
        intent.putExtra(MATCH, match);
        intent.putExtra(MATCH_UPDATE, update);
        intent.putExtra(EXTRA_TYPE, type);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_match_update);
        mType = (MatchEditType) getIntent().getSerializableExtra(EXTRA_TYPE);
        initToolbar();
        initFragment();
    }

    @SuppressWarnings("ConstantConditions")
    private void initToolbar() {
        mBinding.toolbar.setTitle(mType == MatchEditType.CREATE ? R.string.title_activity_match_update : R.string.review_update);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initFragment() {
        RetrievalManager.getCurrentUser().subscribe(user -> {
            Match match = (Match) getIntent().getSerializableExtra(MATCH);
            MatchUpdate update = (MatchUpdate) getIntent().getSerializableExtra(MATCH_UPDATE);
            String updateId = getIntent().getStringExtra(UPDATE_ID);
            MatchUpdateFragment f = MatchUpdateFragment.newInstance(update, user, match, mType, updateId);
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
