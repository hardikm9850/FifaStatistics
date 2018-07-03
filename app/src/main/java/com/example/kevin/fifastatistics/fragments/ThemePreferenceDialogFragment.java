package com.example.kevin.fifastatistics.fragments;

import android.os.Bundle;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceDialogFragmentCompat;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.managers.preferences.ThemePreference;
import com.example.kevin.fifastatistics.views.ThemePicker;

public class ThemePreferenceDialogFragment extends PreferenceDialogFragmentCompat {

    private ThemePicker mThemePicker;

    public static ThemePreferenceDialogFragment newInstance(String key) {
        final ThemePreferenceDialogFragment
                fragment = new ThemePreferenceDialogFragment();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        mThemePicker = view.findViewById(R.id.theme_picker);

        int theme = 0;
        DialogPreference preference = getPreference();
        if (preference instanceof ThemePreference) {
            theme = ((ThemePreference) preference).getTheme();
        }

        // Set the theme to the theme picker
        if (theme != 0) {
            // TODO
        }
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            int theme = mThemePicker.getTheme();
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