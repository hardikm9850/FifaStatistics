package com.example.kevin.fifastatistics.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.SerializationUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesManager {

    private static final String PREFERENCES = "PREFERENCES";
    private static final String SIGNED_IN = "SIGNED_IN";
    private static final String SENT_TOKEN_TO_SERVER = "SENT_TOKEN_TO_SERVER";
    private static final String REGISTRATION_FAILED = "REGISTRATION_FAILED";
    private static final String REGISTRATION_TOKEN = "REGISTRATION_TOKEN";
    private static final String CURRENT_USER = "CURRENT_USER";
    private static final String CURRENT_SERIES = "CURRENT_SERIES";
    private static final String RECENT_TEAMS = "RECENT_TEAMS";
    private static final String COLOR_ACCENT = "COLOR_ACCENT";
    private static final String USERNAME = "USER_NAME";
    private static final String MATCH_UPDATES = "MATCH_UPDATES";
    private static final String FAVORITE_TEAM;
    private static final String DIRECT_CAMERA;
    private static final String DARK_THEME;
    private static final String TEAM_COLOR_AS_ACCENT;

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    static {
        Context context = FifaApplication.getContext();
        FAVORITE_TEAM = context.getString(R.string.favoriteTeam);
        DIRECT_CAMERA = context.getString(R.string.openToCamera);
        DARK_THEME = context.getString(R.string.darkTheme);
        TEAM_COLOR_AS_ACCENT = context.getString(R.string.teamAsColor);
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
        }
    }

    public static String name() {
        return PREFERENCES;
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

    public static void storeCurrentSeries(List<Match> matches, String opponentId) {
        editor = preferences.edit();
        editor.putString(CURRENT_SERIES + opponentId, SerializationUtils.toJson(matches));
        editor.apply();
    }

    public static void removeCurrentSeries(String id) {
        editor = preferences.edit();
        editor.remove(CURRENT_SERIES + id);
        editor.apply();
    }

    public static void setFavoriteTeam(Team team) {
        editor = preferences.edit();
        editor.putString(FAVORITE_TEAM, SerializationUtils.toJson(team));
        editor.putString(COLOR_ACCENT, team.getColor());
        editor.apply();
    }

    public static void setRecentTeams(List<Team> teams) {
        editor = preferences.edit();
        editor.putString(RECENT_TEAMS, SerializationUtils.toJson(teams));
        editor.apply();
    }

    @ColorInt
    public static int getColorAccent() {
        int accentColor = ContextCompat.getColor(FifaApplication.getContext(), R.color.colorAccent);
        if (doUseTeamColorAsAccent()) {
            String color = preferences.getString(COLOR_ACCENT, null);
            return color != null ? Color.parseColor(color) : accentColor;
        } else {
            return accentColor;
        }
    }

    public static boolean doUseTeamColorAsAccent() {
        return preferences.getBoolean(TEAM_COLOR_AS_ACCENT, false);
    }

    public static boolean openCameraImmediately() {
        return preferences.getBoolean(DIRECT_CAMERA, false);
    }

    public static User getUser() {
        return getObject(User.class, CURRENT_USER);
    }

    public static List<Match> getCurrentSeries(String opponentId) {
        return getObject(new TypeReference<List<Match>>() {}, CURRENT_SERIES + opponentId);
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

    public static List<Team> getRecentTeams() {
        return getObject(new TypeReference<List<Team>>() {}, RECENT_TEAMS);
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
