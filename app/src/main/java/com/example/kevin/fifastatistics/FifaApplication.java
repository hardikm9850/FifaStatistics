package com.example.kevin.fifastatistics;

import android.app.Application;
import android.content.Context;

import com.example.kevin.fifastatistics.managers.ImageLoaderManager;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;

/**
 * Application class used for global initialization, run before any activity is started.
 * <p>
 * Makes the application context available from any class via {@link #getContext()}.
 */
public class FifaApplication extends Application {

    private static Context instance;

    /**
     * Retrieve the application context.
     * @return  the application context
     */
    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = getApplicationContext();
        ImageLoaderManager.initializeDefaultImageLoader(this);
        SharedPreferencesManager.initialize(this);
    }
}
