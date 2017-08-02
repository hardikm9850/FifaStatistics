package com.example.kevin.fifastatistics.managers.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.kevin.fifastatistics.models.databasemodels.match.CurrentSeries;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.user.Friend;
import com.example.kevin.fifastatistics.utils.SerializationUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CurrentSeriesPrefs extends AbstractPrefs {

    private static final String NAME = "CURRENT_SERIES_PREFS";

    public CurrentSeriesPrefs(Context context) {
        super(context);
    }

    @Override
    String name() {
        return NAME;
    }

    public void saveCurrentSeries(List<Match> matches, String userId, Friend opponent) {
        CurrentSeries s = new CurrentSeries(userId, opponent, matches);
        saveCurrentSeries(s);
    }

    public void saveCurrentSeries(CurrentSeries series) {
        if (series != null) {
            storeAsString(series, series.getOpponentId());
        }
    }

    public void setCurrentSeries(List<CurrentSeries> currentSeries) {
        if (currentSeries != null) {
            SharedPreferences.Editor editor = mPrefs.edit();
            for (CurrentSeries s : currentSeries) {
                editor.putString(s.getOpponentId(), SerializationUtils.toJson(s));
            }
            editor.apply();
        }
    }

    public void removeCurrentSeries(String opponentId) {
        mPrefs.edit().remove(opponentId).apply();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public Collection<CurrentSeries> getCurrentSeries() {
        Map<String, CurrentSeries> series = (Map<String, CurrentSeries>) mPrefs.getAll();
        return series == null ? new ArrayList<>() : series.values();
    }

    @Nullable
    public CurrentSeries getCurrentSeriesForOpponent(String opponentId) {
        return getObject(CurrentSeries.class, opponentId);
    }
}