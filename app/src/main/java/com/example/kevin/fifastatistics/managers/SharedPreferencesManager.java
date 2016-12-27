package com.example.kevin.fifastatistics.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Utility for handling all Shared Preference needs. Any requests to Shared Preferences should be
 * delegated to this class.
 * <p>
 * All methods write to SharedPreferences asynchronously, unless they are suffixed with 'Sync'.
 * <p>
 * Ensure that {@link #initialize(Context)} is called in the onCreate() method of
 * {@link com.example.kevin.fifastatistics.FifaApplication} before this class is used.
 */
public class SharedPreferencesManager {

    private static final String PREFERENCES = "PREFERENCES";
    private static final String SIGNED_IN = "SIGNED_IN";
    private static final String SENT_TOKEN_TO_SERVER = "SENT_TOKEN_TO_SERVER";
    private static final String REGISTRATION_FAILED = "REGISTRATION_FAILED";
    private static final String REGISTRATION_TOKEN = "REGISTRATION_TOKEN";
    private static final String CURRENT_USER = "CURRENT_USER";

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

    /**
     * Stores the current user to shared preferences.
     * @param user  The current user
     */
    public static void storeUser(User user) {
        editor = preferences.edit();
        editor.putString(CURRENT_USER, user.toString());
        editor.apply();
    }

    /**
     * Retrieves the current user from Shared Preferences.
     * @return  the curernt user
     */
    public static User getUser() {
        ObjectMapper mapper = new ObjectMapper();
        String user = preferences.getString(CURRENT_USER, null);
        try {
            return user == null ? null : mapper.readValue(user, User.class);
        } catch (IOException e) {
            return null;
        }
    }
}
