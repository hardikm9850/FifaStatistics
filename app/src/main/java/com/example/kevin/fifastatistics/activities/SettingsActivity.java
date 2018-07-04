package com.example.kevin.fifastatistics.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ActivitySettingsBinding;
import com.example.kevin.fifastatistics.fragments.SettingsFragment;

public class SettingsActivity extends FifaBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySettingsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        initToolbar(binding);
        initFragment();
    }

    @SuppressWarnings("ConstantConditions")
    private void initToolbar(ActivitySettingsBinding binding) {
        setSupportActionBar(binding.toolbarLayout.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.toolbarLayout.toolbarTitle.setText(getString(R.string.settings));
    }

    private void initFragment() {
        SettingsFragment fragment = SettingsFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
