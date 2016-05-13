package com.example.kevin.fifastatistics;

import android.app.Application;

import com.example.kevin.fifastatistics.managers.ImageLoaderManager;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;

public class FifaApplication extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderManager.initializeDefaultImageLoader(this);
        SharedPreferencesManager.initialize(this);
    }
}
