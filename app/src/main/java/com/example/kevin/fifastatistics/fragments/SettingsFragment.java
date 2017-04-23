package com.example.kevin.fifastatistics.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.PickTeamActivity;
import com.example.kevin.fifastatistics.event.ColorChangeEvent;
import com.example.kevin.fifastatistics.interfaces.OnTeamSelectedListener;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.event.EventBus;
import com.example.kevin.fifastatistics.utils.ObservableUtils;
import com.example.kevin.fifastatistics.utils.UserUtils;

import rx.Observable;
import rx.Subscription;

public class SettingsFragment extends PreferenceFragmentCompat implements OnTeamSelectedListener {

    private static final int SETTINGS_REQUEST_CODE = 5377;

    private Preference mTeamPreference;
    private EventBus mEventBus;
    private Subscription mUpdateTeamSubscription;

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
        getPreferenceManager().setSharedPreferencesName(SharedPreferencesManager.name());
        addPreferencesFromResource(R.xml.preferences);
        initFavoriteTeamPreference();
        initTeamAsColorAccentPreferenceChangeListener();
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
        Observable.<Team>create(subscriber -> subscriber.onNext(SharedPreferencesManager.getFavoriteTeam()))
                .compose(ObservableUtils.applySchedulers())
                .subscribe(team -> mTeamPreference.setSummary(team != null ? team.getShortName() : null));
    }

    private void initTeamAsColorAccentPreferenceChangeListener() {
        Preference colorPreference = findPreference(getString(R.string.teamAsColor));
        colorPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue instanceof Boolean) {
                boolean useTeamForColor = (Boolean) newValue;
                Team favTeam = SharedPreferencesManager.getFavoriteTeam();
                if (favTeam != null) {
                    if (useTeamForColor) {
                        updateColors(favTeam);
                    } else {
                        int color = ContextCompat.getColor(getContext(), R.color.colorAccent);
                        FifaApplication.setAccentColor(color);
                        mEventBus.post(new ColorChangeEvent(color));
                    }
                }
            }
            return true;
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mUpdateTeamSubscription != null) {
            mUpdateTeamSubscription.unsubscribe();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SETTINGS_REQUEST_CODE && resultCode == PickTeamActivity.RESULT_TEAM_PICKED) {
            Team team = (Team) data.getExtras().getSerializable(PickTeamActivity.EXTRA_TEAM);
            onTeamSelected(team);
        }
    }

    @Override
    public void onTeamSelected(Team team) {
        SharedPreferencesManager.setFavoriteTeam(team);
        syncFavoriteTeamWithServer(team);
        setFavoriteTeamSummary();
        if (SharedPreferencesManager.doUseTeamColorAsAccent()) {
            updateColors(team);
        }
    }

    @SuppressWarnings("unchecked")
    private void syncFavoriteTeamWithServer(final Team team) {
        mUpdateTeamSubscription = RetrievalManager.getCurrentUser()
                .flatMap(user -> UserUtils.patchTeam(user, team))
                .subscribe(ObservableUtils.EMPTY_OBSERVER);
    }

    private void updateColors(Team team) {
        int newColor = Color.parseColor(team.getColor());
        FifaApplication.setAccentColor(newColor);
        mEventBus.post(new ColorChangeEvent(newColor));
    }
}
