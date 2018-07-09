package com.example.kevin.fifastatistics.managers.preferences;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.SerializationUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrefsManager {

    private static final String PREFERENCES = "PREFERENCES";
    private static final String SIGNED_IN = "SIGNED_IN";
    private static final String SENT_TOKEN_TO_SERVER = "SENT_TOKEN_TO_SERVER";
    private static final String REGISTRATION_FAILED = "REGISTRATION_FAILED";
    private static final String REGISTRATION_TOKEN = "REGISTRATION_TOKEN";
    private static final String CURRENT_USER = "CURRENT_USER";
    private static final String COLOR_ACCENT = "COLOR_ACCENT";
    private static final String USERNAME = "USER_NAME";
    private static final String ID = "ID";
    private static final String MATCH_UPDATES = "MATCH_UPDATES";
    private static final String FAVORITE_TEAM;
    private static final String DIRECT_CAMERA;
    private static final String TEAM_COLOR_AS_ACCENT;
    private static final String SAVE_FACT_BITMAPS;
    private static final String THEME;

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static CurrentSeriesPrefs sSeriesPrefs;

    static {
        Context context = FifaApplication.getContext();
        FAVORITE_TEAM = context.getString(R.string.favoriteTeam);
        DIRECT_CAMERA = context.getString(R.string.openToCamera);
        TEAM_COLOR_AS_ACCENT = context.getString(R.string.teamAsColor);
        SAVE_FACT_BITMAPS = context.getString(R.string.saveFactBitmaps);
        THEME = context.getString(R.string.themePref);
    }

    /**
     * Initializes the SharedPreferences with an application context. This method must be called
     * prior to calling any other public methods.
     * @param context   the application context
     */
    @SuppressWarnings("ConstantConditions")
    public static void initialize(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
            sSeriesPrefs = new CurrentSeriesPrefs(context);
        }
    }

    public static String name() {
        return PREFERENCES;
    }

    public static CurrentSeriesPrefs getSeriesPrefs() {
        return sSeriesPrefs;
    }

    /**
     * Returns true if the user is currently signed in to their Google account, false otherwise
     */
    public static boolean isSignedIn() {
        return preferences.getBoolean(SIGNED_IN, false);
    }

    /**
     * Set whether or not the user is signed in.
     */
    public static void setSignedIn(boolean signedIn) {
        editor = preferences.edit();
        editor.putBoolean(SIGNED_IN, signedIn);
        editor.apply();
    }

    public static boolean didSendRegistrationToken() {
        return preferences.getBoolean(SENT_TOKEN_TO_SERVER, false);
    }

    public static void setDidSendRegistrationToken(boolean didSendToken) {
        editor = preferences.edit();
        editor.putBoolean(SENT_TOKEN_TO_SERVER, didSendToken);
        editor.apply();
    }

    public static String getRegistrationToken() {
        return preferences.getString(REGISTRATION_TOKEN, null);
    }

    public static void setRegistrationToken(String registrationToken) {
        editor = preferences.edit();
        editor.putString(REGISTRATION_TOKEN, registrationToken);
        editor.apply();
    }

    /**
     * True if the retrieval of the registration token failed.
     */
    public static boolean getRegistrationFailed() {
        return preferences.getBoolean(REGISTRATION_FAILED, false);
    }

    public static void setRegistrationFailed(boolean registrationFailed) {
        editor = preferences.edit();
        editor.putBoolean(REGISTRATION_FAILED, registrationFailed);
        editor.apply();
    }

    /**
     * Synchronously stores the current user to shared preferences.
     * <p>
     * {@link #storeUser(User)} should be used in most cases rather than this method.
     * @param user  The current user
     */
    @SuppressLint("CommitPrefEdits")
    public static void storeUserSync(User user) {
        editor = preferences.edit();
        editor.putString(CURRENT_USER, user.toString());
        editor.putString(USERNAME, user.getName());
        editor.putString(ID, user.getId());
        editor.commit();
    }

    public static void storeUser(User user) {
        editor = preferences.edit();
        editor.putString(CURRENT_USER, user.toString());
        editor.putString(USERNAME, user.getName());
        editor.apply();
    }

    public static String getUserName() {
        return preferences.getString(USERNAME, null);
    }

    public static String getUserId() {
        String id = preferences.getString(ID, null);
        return id == null ? getUser().getId() : id;
    }

    public static void setFavoriteTeam(Team team) {
        editor = preferences.edit();
        editor.putString(FAVORITE_TEAM, SerializationUtils.toJson(team));
        editor.putString(COLOR_ACCENT, team.getColor());
        editor.apply();
    }

    @ColorInt
    public static int getColorAccent() {
        Context context = FifaApplication.getContext();
        TypedArray a = context.getTheme().obtainStyledAttributes(getTheme(), new int[] { R.attr.colorAccent });
        int defaultColor = ContextCompat.getColor(context, R.color.colorAccent);
        int accentColor = defaultColor;
        try {
            accentColor = a.getColor(0, defaultColor);
        } finally {
            a.recycle();
        }
        return accentColor;
//        if (doUseTeamColorAsAccent()) {
//            String color = preferences.getString(COLOR_ACCENT, null);
//            return color != null ? Color.parseColor(color) : accentColor;
//        } else {
//            return accentColor;
//        }
    }

    public static boolean doUseTeamColorAsAccent() {
        // Removal of functionality
        return false;
//        return preferences.getBoolean(TEAM_COLOR_AS_ACCENT, false);
    }

    public static boolean openCameraImmediately() {
        return preferences.getBoolean(DIRECT_CAMERA, false);
    }

    public static boolean doSaveMatchFactsBitmap() {
        return preferences.getBoolean(SAVE_FACT_BITMAPS, false);
    }

    public static int getTheme() {
        return preferences.getInt(THEME, R.style.AppTheme_Cobalt);
    }

    public static User getUser() {
        return getObject(User.class, CURRENT_USER);
    }

    public static void addMatchUpdate(MatchUpdate update) {
        List<MatchUpdate> updates = getMatchUpdates();
        updates.add(update);
        setMatchUpdates(updates);
    }

    public static void setMatchUpdates(List<MatchUpdate> updates) {
        editor = preferences.edit();
        editor.putString(MATCH_UPDATES, SerializationUtils.toJson(updates));
        editor.apply();
    }

    public static List<MatchUpdate> removeMatchUpdate(MatchUpdate update) {
        List<MatchUpdate> updates = getMatchUpdates();
        updates.remove(update);
        setMatchUpdates(updates);
        return updates;
    }

    @NonNull
    public static List<MatchUpdate> getMatchUpdates() {
        List<MatchUpdate> updates = getObject(new TypeReference<List<MatchUpdate>>() {}, MATCH_UPDATES);
        return updates == null ? new ArrayList<>() : updates;
    }

    public static Team getFavoriteTeam() {
        return getObject(Team.class, FAVORITE_TEAM);
    }

    private static <T> T getObject(TypeReference<T> typeReference, String key) {
        ObjectMapper mapper = new ObjectMapper();
        String object = preferences.getString(key, null);
        try {
            return object == null ? null : mapper.readValue(object, typeReference);
        } catch (IOException e) {
            Log.e("ERROR", e.getMessage());
            return null;
        }
    }

    private static <T> T getObject(Class<T> clazz, String key) {
        ObjectMapper mapper = new ObjectMapper();
        String object = preferences.getString(key, null);
        try {
            return object == null ? null : mapper.readValue(object, clazz);
        } catch (IOException e) {
            Log.e("ERROR", e.getMessage());
            return null;
        }
    }
}
