package com.example.kevin.fifastatistics.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.kevin.fifastatistics.FifaApplication;

import lombok.experimental.UtilityClass;

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
}
