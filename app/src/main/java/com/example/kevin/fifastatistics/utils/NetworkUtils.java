package com.example.kevin.fifastatistics.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.kevin.fifastatistics.FifaApplication;

import lombok.experimental.UtilityClass;
import retrofit2.Response;

@UtilityClass
public class NetworkUtils {

    /**
     * @return true if currently connected or are connecting to a network, false if no connection
     */
    public static boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) FifaApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * @return true if currently there is no network connection
     */
    public static boolean isNotConnected() {
        return !isConnected();
    }

    /** Retrieve the id of the created object from the location header of the response. */
    public static String getIdFromResponse(Response<?> response) {
        String header = response.headers().get("Location");
        return getIdFromUrl(header);
    }

    public static String getIdFromUrl(String url) {
        int startIndex = url.lastIndexOf("/");
        return url.substring(startIndex + 1);
    }
}
