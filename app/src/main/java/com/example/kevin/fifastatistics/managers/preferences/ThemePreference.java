package com.example.kevin.fifastatistics.managers.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;

import com.example.kevin.fifastatistics.R;

public class ThemePreference extends DialogPreference {

    private int mTheme;

    public ThemePreference(Context context) {
        this(context, null);
    }

    public ThemePreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.preferenceStyle);
    }

    public ThemePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, defStyleAttr);
    }

    public ThemePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public int getTheme() {
        return mTheme;
    }

    public void setTheme(int theme) {
        mTheme = theme;
        persistInt(theme);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getResourceId(index, R.style.AppTheme_Cobalt);
    }

    @Override
    public int getDialogLayoutResource() {
        return R.layout.pref_dialog_theme;
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        setTheme(restorePersistedValue ? getPersistedInt(mTheme) : (int) defaultValue);
    }

}