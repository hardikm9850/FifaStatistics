package com.example.kevin.fifastatistics;

import android.app.Application;
import android.content.Context;

import com.example.kevin.fifastatistics.managers.ImageLoaderManager;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;

public class FifaApplication extends Application
{
    private static Context instance;

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
