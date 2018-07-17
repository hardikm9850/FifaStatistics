package com.example.kevin.fifastatistics.fragments;

import android.os.Bundle;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.util.SparseIntArray;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.managers.preferences.ThemePreference;
import com.example.kevin.fifastatistics.views.ThemeRadioButton;
import com.example.kevin.fifastatistics.views.ThemeRadioGroup;

public class ThemePreferenceDialogFragment extends PreferenceDialogFragmentCompat {

    private static final SparseIntArray THEMES_TO_IDS;

    static {
        THEMES_TO_IDS = new SparseIntArray();
        THEMES_TO_IDS.put(R.style.AppTheme, R.id.button_dark_theme);
        THEMES_TO_IDS.put(R.style.AppTheme_Cobalt, R.id.button_cobalt_theme);
        THEMES_TO_IDS.put(R.style.AppTheme_Light, R.id.button_light_theme);
    }

    private ThemeRadioGroup mRadioGroup;

    public static ThemePreferenceDialogFragment newInstance(String key) {
        final ThemePreferenceDialogFragment fragment = new ThemePreferenceDialogFragment();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mRadioGroup = view.findViewById(R.id.theme_radio_group);

        int theme = R.style.AppTheme_Cobalt;
        DialogPreference preference = getPreference();
        preference.setDialogTitle(R.string.select_theme);
        if (preference instanceof ThemePreference) {
            theme = ((ThemePreference) preference).getTheme();
        }
        mRadioGroup.check(THEMES_TO_IDS.get(theme));
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            int selectedId = mRadioGroup.getCheckedRadioButtonId();
            ThemeRadioButton selectedButton = mRadioGroup.findViewById(selectedId);
            int theme = selectedButton.getTheme();
            DialogPreference preference = getPreference();
            if (preference instanceof ThemePreference) {
                ThemePreference ThemePreference = ((ThemePreference) preference);
                if (ThemePreference.callChangeListener(theme)) {
                    ThemePreference.setTheme(theme);
                }
            }
        }
    }
}