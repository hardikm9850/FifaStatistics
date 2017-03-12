package com.example.kevin.fifastatistics.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.kevin.fifastatistics.models.databasemodels.league.Team;
import com.example.kevin.fifastatistics.models.databasemodels.match.Match;
import com.example.kevin.fifastatistics.models.databasemodels.match.Series;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.SerializationUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class SharedPreferencesManager {

    private static final String PREFERENCES = "PREFERENCES";
    private static final String SIGNED_IN = "SIGNED_IN";
    private static final String SENT_TOKEN_TO_SERVER = "SENT_TOKEN_TO_SERVER";
    private static final String REGISTRATION_FAILED = "REGISTRATION_FAILED";
    private static final String REGISTRATION_TOKEN = "REGISTRATION_TOKEN";
    private static final String CURRENT_USER = "CURRENT_USER";
    private static final String CURRENT_SERIES = "CURRENT_SERIES";
    private static final String FAVORITE_TEAM = "FAVORITE_TEAM";
    private static final String RECENT_TEAMS = "RECENT_TEAMS";

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

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
        editor.commit();
    }

    public static void storeUser(User user) {
        editor = preferences.edit();
        editor.putString(CURRENT_USER, user.toString());
        editor.apply();
    }

    public static void storeCurrentSeries(List<Match> matches) {
        editor = preferences.edit();
        editor.putString(CURRENT_SERIES, SerializationUtils.toJson(matches));
        editor.apply();
    }

    public static void removeCurrentSeries() {
        editor = preferences.edit();
        editor.remove(CURRENT_SERIES);
        editor.apply();
    }

    public static void setFavoriteTeam(Team team) {
        editor = preferences.edit();
        editor.putString(FAVORITE_TEAM, SerializationUtils.toJson(team));
        editor.apply();
    }

    public static void setRecentTeams(List<Team> teams) {
        editor = preferences.edit();
        editor.putString(RECENT_TEAMS, SerializationUtils.toJson(teams));
        editor.apply();
    }

    public static User getUser() {
        return getObject(User.class, CURRENT_USER);
    }

    public static List<Match> getCurrentSeries() {
        return getObject(new TypeReference<List<Match>>() {}, CURRENT_SERIES);
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
