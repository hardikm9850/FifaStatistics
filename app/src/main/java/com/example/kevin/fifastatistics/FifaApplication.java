package com.example.kevin.fifastatistics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.example.kevin.fifastatistics.activities.FifaActivityLifecycleCallbacks;
import com.example.kevin.fifastatistics.managers.ImageLoaderManager;
import com.example.kevin.fifastatistics.managers.preferences.PrefsManager;
import com.example.kevin.fifastatistics.utils.FileUtils;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import java.io.File;

import rx.Observable;

public class FifaApplication extends MultiDexApplication {

    @SuppressLint("StaticFieldLeak")
    private static Context instance;
    private static int colorAccent;

    public static Context getContext() {
        return instance;
    }

    public static int getAccentColor() {
        if (colorAccent == 0) {
            colorAccent = PrefsManager.getColorAccent();
        }
        return colorAccent;
    }

    public static void setAccentColor(int color) {
        colorAccent = color;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = getApplicationContext();
        ImageLoaderManager.initializeDefaultImageLoader(this);
        PrefsManager.initialize(this);
        ensureTrainedDataExists();
        registerActivityLifecycleCallbacks(new FifaActivityLifecycleCallbacks());
    }

    private void ensureTrainedDataExists() {
        Observable.create(subscriber -> {
            File file = getFileStreamPath("eng.traineddata");
            if (!file.exists()) {
                FileUtils.copyAsset(getApplicationContext(), "tessdata");
            }
        }).compose(ObservableUtils.applyBackground()).subscribe();
    }
}
