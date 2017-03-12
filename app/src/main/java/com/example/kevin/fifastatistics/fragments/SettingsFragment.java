package com.example.kevin.fifastatistics.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.PickTeamActivity;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.utils.EventBus;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import rx.Observable;

public class SettingsFragment extends PreferenceFragmentCompat {

    private static final int SETTINGS_REQUEST_CODE = 5377;

    private Preference mTeamPreference;
    private EventBus mEventBus;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEventBus = EventBus.getInstance();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
        initFavoriteTeamPreference();
    }

    private void initFavoriteTeamPreference() {
        mTeamPreference = findPreference(getString(R.string.favoriteTeam));
        mTeamPreference.setOnPreferenceClickListener(preference -> {
            startActivityForResult(preference.getIntent(), SETTINGS_REQUEST_CODE);
            return true;
        });
        setFavoriteTeamSummary();
    }

    private void setFavoriteTeamSummary() {
        Observable.just(SharedPreferencesManager.getFavoriteTeam())
                .compose(ObservableUtils.applySchedulers())
                .subscribe(team -> mTeamPreference.setSummary(team != null ? team.getShortName() : null));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTINGS_REQUEST_CODE && resultCode == PickTeamActivity.RESULT_TEAM_PICKED) {
            Team team = (Team) data.getExtras().getSerializable(PickTeamActivity.EXTRA_TEAM);
            SharedPreferencesManager.setFavoriteTeam(team);
            setFavoriteTeamSummary();
            updateColors(team);
        }
    }

    private void updateColors(Team team) {
        int newColor = Color.parseColor(team.getColor());
        FifaApplication.setAccentColor(newColor);
        updateSwitchColors(newColor);
        mEventBus.post(newColor);
    }

    private void updateSwitchColors(int color) {
        Preference themePref = findPreference(getString(R.string.darkTheme));
    }
}
