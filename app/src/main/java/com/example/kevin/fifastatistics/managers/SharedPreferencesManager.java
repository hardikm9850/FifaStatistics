package com.example.kevin.fifastatistics.managers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.kevin.fifastatistics.models.user.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;

/**
 * Utility class for handling all Shared Preference needs.
 * Implemented as a singleton. Application Context must be passed to the
 * instance when retrieving it.
 */
public class SharedPreferencesManager
{
    private static SharedPreferencesManager instance = new SharedPreferencesManager();
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public static SharedPreferencesManager getInstance(Context context)
    {
        instance.context = context;
        instance.preferences = context.getSharedPreferences(
                PreferenceNames.PREFERENCES.name(), Context.MODE_PRIVATE);
        return instance;
    }

    private SharedPreferencesManager() {
    }

    /**
     * Returns true if the user is currently signed in to their Google account, false otherwise
     */
    public boolean isSignedIn()
    {
        return preferences.getBoolean(PreferenceNames.SIGNED_IN.name(), false);
    }

    /**
     * Sets the SIGNED_IN value in shared preferences. Should be set to true if logged in to Google
     * account, and false if not.
     * @param signedIn  Whether or not the user is signed in
     */
    public void setSignedIn(boolean signedIn)
    {
        editor = preferences.edit();
        editor.putBoolean(PreferenceNames.SIGNED_IN.name(), signedIn);
        editor.apply();
    }

    public boolean didSendRegistrationToken()
    {
        return preferences.getBoolean(PreferenceNames.SENT_TOKEN_TO_SERVER.name(), false);
    }

    public void setDidSendRegistrationToken(boolean didSendToken)
    {
        editor = preferences.edit();
        editor.putBoolean(PreferenceNames.SENT_TOKEN_TO_SERVER.name(), didSendToken);
        editor.apply();
    }

    public String getRegistrationToken()
    {
        return preferences.getString(PreferenceNames.REGISTRATION_TOKEN.name(), null);
    }

    public void setRegistrationToken(String registrationToken)
    {
        editor = preferences.edit();
        editor.putString(PreferenceNames.REGISTRATION_TOKEN.name(), registrationToken);
        editor.apply();
    }

    public boolean getRegistrationFailed()
    {
        return preferences.getBoolean(PreferenceNames.REGISTRATION_FAILED.name(), false);
    }

    public void setRegistrationFailed(boolean registrationFailed)
    {
        editor = preferences.edit();
        editor.putBoolean(PreferenceNames.REGISTRATION_FAILED.name(), registrationFailed);
        editor.apply();
    }

    @SuppressLint("CommitPrefEdits")
    public void storeUser(User user)
    {
        editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(PreferenceNames.CURRENT_USER.name(), json);
        editor.commit();
    }

    public void storeUserAsync(User user)
    {
        editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(PreferenceNames.CURRENT_USER.name(), json);
        editor.apply();
    }

    public User getUser()
    {
        Gson gson = new Gson();
        String user = preferences.getString(PreferenceNames.CURRENT_USER.name(), null);
        return gson.fromJson(user, User.class);
    }

    private enum PreferenceNames
    {
        PREFERENCES,          // String, Default Shared Preference name
        SIGNED_IN,            // Boolean, true if user is signed in to Google
        CURRENT_USER_NAME,    // String, the current signed in user's name
        CURRENT_USER_EMAIL,   // String
        SENT_TOKEN_TO_SERVER, // Boolean, true if registration token successfully sent to server
        REGISTRATION_FAILED,
        REGISTRATION_TOKEN,
        CURRENT_USER_IMAGE_URL,
        CURRENT_USER_GOOGLE_ID,
        CURRENT_USER,
    }
}
