package com.example.kevin.fifastatistics;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.example.kevin.fifastatistics.activities.FifaActivityLifecycleCallbacks;
import com.example.kevin.fifastatistics.managers.ImageLoaderManager;
import com.example.kevin.fifastatistics.managers.preferences.PrefsManager;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import rx.Observable;

public class FifaApplication extends MultiDexApplication {

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
                copyAsset("tessdata");
            }
        }).compose(ObservableUtils.applyBackground()).subscribe();
    }

    /**
     * Copy the asset at the specified path to this app's data directory. If the
     * asset is a directory, its contents are also copied.
     *
     * @param path
     * Path to asset, relative to app's assets directory.
     */
    private void copyAsset(String path) {
        AssetManager manager = getAssets();

        // If we have a directory, we make it and recurse. If a file, we copy its
        // contents.
        try {
            String[] contents = manager.list(path);

            // The documentation suggests that list throws an IOException, but doesn't
            // say under what conditions. It'd be nice if it did so when the path was
            // to a file. That doesn't appear to be the case. If the returned array is
            // null or has 0 length, we assume the path is to a file. This means empty
            // directories will get turned into files.
            if (contents == null || contents.length == 0)
                throw new IOException();

            // Make the directory.
            File dir = new File(getExternalFilesDir(null), path);
            dir.mkdirs();

            // Recurse on the contents.
            for (String entry : contents) {
                copyAsset(path + "/" + entry);
            }
        } catch (IOException e) {
            copyFileAsset(path);
        }
    }

    /**
     * Copy the asset file specified by path to app's data directory. Assumes
     * parent directories have already been created.
     *
     * @param path
     * Path to asset, relative to app's assets directory.
     */
    private void copyFileAsset(String path) {
        File file = new File(getExternalFilesDir(null), path);
        try {
            InputStream in = getAssets().open(path);
            OutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read = in.read(buffer);
            while (read != -1) {
                out.write(buffer, 0, read);
                read = in.read(buffer);
            }
            out.close();
            in.close();
        } catch (IOException e) {
            Log.e("ERROR", e.getMessage());
        }
    }
}
