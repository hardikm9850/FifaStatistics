package com.example.kevin.fifastatistics.utils;

import com.example.kevin.fifastatistics.BuildConfig;

public class BuildUtils {

    private static final String RELEASE = "release";

    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    public static boolean isRelease() {
        return RELEASE.equals(BuildConfig.BUILD_TYPE);
    }
}
