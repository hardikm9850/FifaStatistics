package com.example.kevin.fifastatistics.models;

/**
 * Created by Kevin on 3/7/2016.
 */
public final class Constants
{
    public static final String APP_NAME = "FifaStatistics";

    // REST
    public static final String FIFA_API_ENDPOINT =
            "https://fifastatisticsapi.azurewebsites.net/";

    public static final String NOTIFICATIONS_API_ENDPOINT =
            "https://gcm-http.googleapis.com/gcm/send";

    public static final String NOTIFICATION_KEY =
            "AIzaSyDjCHksoGamhWxeNsaDN-DW5v3p9IcJNFE";

    // NOTIFICATION TAGS
    public static final String FRIEND_REQUEST_TAG = "FRIEND_REQUEST";
    public static final String SERIES_ADDED_TAG   = "SERIES_ADDED";

    // FRAGMENT NAMES
    public static final String OVERVIEW_FRAGMENT   = "Overview";
    public static final String FRIENDS_FRAGMENT    = "Friends";
    public static final String STATISTICS_FRAGMENT = "Statistics";
    public static final String STARRED_FRAGMENT    = "Starred";
    public static final String SETTINGS_FRAGMENT   = "Settings";
}
