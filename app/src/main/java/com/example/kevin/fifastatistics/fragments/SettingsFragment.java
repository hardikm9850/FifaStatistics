package com.example.kevin.fifastatistics.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.TwoStatePreference;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.PickTeamActivity;
import com.example.kevin.fifastatistics.event.ColorChangeEvent;
import com.example.kevin.fifastatistics.event.EventBus;
import com.example.kevin.fifastatistics.event.ThemeChangeEvent;
import com.example.kevin.fifastatistics.interfaces.OnTeamSelectedListener;
import com.example.kevin.fifastatistics.managers.RetrievalManager;
import com.example.kevin.fifastatistics.managers.preferences.PrefsManager;
import com.example.kevin.fifastatistics.managers.preferences.ThemePreference;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.network.service.SyncPlayerCacheService;
import com.example.kevin.fifastatistics.network.service.UpdateTokenService;
import com.example.kevin.fifastatistics.utils.ObservableUtils;
import com.example.kevin.fifastatistics.utils.ToastUtils;
import com.example.kevin.fifastatistics.utils.UserUtils;

import rx.Observable;
import rx.Subscription;

public class SettingsFragment extends PreferenceFragmentCompat implements OnTeamSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int SETTINGS_REQUEST_CODE = 5377;

    private Preference mTeamPreference;
    private TwoStatePreference mSaveFactsPreference;
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
        getPreferenceManager().setSharedPreferencesName(PrefsManager.name());
        addPreferencesFromResource(R.xml.preferences);
        initFavoriteTeamPreference();
//        initTeamAsColorAccentPreferenceChangeListener();
        initResendRegTokenPreference();
        initUpdatePlayerCachePreference();
        initSaveToGalleryPreference();
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
        Observable.<Team>create(subscriber -> subscriber.onNext(PrefsManager.getFavoriteTeam()))
                .compose(ObservableUtils.applySchedulers())
                .subscribe(team -> mTeamPreference.setSummary(team != null ? team.getShortName() : null));
    }

    private void initTeamAsColorAccentPreferenceChangeListener() {
        Preference colorPreference = findPreference(getString(R.string.teamAsColor));
        colorPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue instanceof Boolean) {
                boolean useTeamForColor = (Boolean) newValue;
                Team favTeam = PrefsManager.getFavoriteTeam();
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

    private void initResendRegTokenPreference() {
        Preference tokenPref = findPreference(getString(R.string.resendRegToken));
        tokenPref.setOnPreferenceClickListener(preference -> {
            ToastUtils.showShortToast(getContext(), R.string.updating_token);
            PrefsManager.setDidSendRegistrationToken(false);
            Intent intent = new Intent(getContext(), UpdateTokenService.class);
            getActivity().startService(intent);
            return true;
        });
    }

    private void initUpdatePlayerCachePreference() {
        Preference cachePref = findPreference(getString(R.string.syncFootballerCache));
        cachePref.setOnPreferenceClickListener(preference -> {
            ToastUtils.showShortToast(getContext(), R.string.updating_player_cache);
            Intent intent = SyncPlayerCacheService.getPlayersIntent();
            getActivity().startService(intent);
            return true;
        });
    }

    private void initSaveToGalleryPreference() {
        mSaveFactsPreference = (TwoStatePreference) findPreference(getString(R.string.saveFactBitmaps));
        mSaveFactsPreference.setOnPreferenceChangeListener(((preference, newValue) -> {
            if (newValue instanceof Boolean) {
                boolean doSaveFacts = (Boolean) newValue;
                if (doSaveFacts) {
                    requestWriteExternalStoragePermission();
                }
            }
            return true;
        }));
    }

    private void requestWriteExternalStoragePermission() {
        final String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
           requestPermissions(new String[]{permission}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
            mSaveFactsPreference.setChecked(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
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
        PrefsManager.setFavoriteTeam(team);
        syncFavoriteTeamWithServer(team);
        setFavoriteTeamSummary();
        if (PrefsManager.doUseTeamColorAsAccent()) {
            updateColors(team);
        }
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        if (preference instanceof ThemePreference) {
            DialogFragment dialogFragment = ThemePreferenceDialogFragment.newInstance(preference.getKey());
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(getFragmentManager(), "android.support.v7.preference.PreferenceFragment.DIALOG");
        } else {
            super.onDisplayPreferenceDialog(preference);
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.themePref))) {
            int theme = sharedPreferences.getInt(key, R.style.AppTheme);
            FifaApplication.setSelectedTheme(theme);
            mEventBus.post(new ThemeChangeEvent(theme));
        }
    }
}
