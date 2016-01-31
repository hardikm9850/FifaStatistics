package com.example.kevin.fifastatistics.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.kevin.fifastatistics.User.User;

/**
 * Created by Kevin on 1/23/2016.
 */
public class PreferenceHandler
{
    private static PreferenceHandler instance = new PreferenceHandler();
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public static PreferenceHandler getInstance(Context context) {
        instance.preferences = context.getSharedPreferences(
                PreferenceNames.PREFERENCES.name(), Context.MODE_PRIVATE);
        return instance;
    }

    private PreferenceHandler() {
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

    public User getCurrentUser()
    {
        String name = preferences.getString(PreferenceNames.CURRENT_USER_NAME.name(), "");
        String email = preferences.getString(PreferenceNames.CURRENT_USER_EMAIL.name(), "");
        String imageUrl = preferences.getString(PreferenceNames.CURRENT_USER_IMAGE_URL.name(), "");
        String googleId = preferences.getString(PreferenceNames.CURRENT_USER_GOOGLE_ID.name(), "");
        return new User(name, email, googleId, imageUrl);
    }

    public void setCurrentUser(String name, String googleId, String email, String imageUrl)
    {
        editor = preferences.edit();
        editor.putString(PreferenceNames.CURRENT_USER_NAME.name(), name);
        editor.putString(PreferenceNames.CURRENT_USER_EMAIL.name(), email);
        editor.putString(PreferenceNames.CURRENT_USER_GOOGLE_ID.name(), googleId);
        editor.putString(PreferenceNames.CURRENT_USER_IMAGE_URL.name(), imageUrl);
        editor.commit();
    }

    private enum PreferenceNames
    {
        PREFERENCES,         // String, Default Shared Preference name
        SIGNED_IN,           // Boolean, true if user is signed in to Google
        CURRENT_USER_NAME,   // String, the current signed in user's name
        CURRENT_USER_EMAIL,  // String
        CURRENT_USER_IMAGE_URL,
        CURRENT_USER_GOOGLE_ID
    }
}
