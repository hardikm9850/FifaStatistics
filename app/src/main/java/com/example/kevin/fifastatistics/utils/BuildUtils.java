package com.example.kevin.fifastatistics.utils;

import com.example.kevin.fifastatistics.BuildConfig;

public class BuildUtils {

    private static final String DEBUG = "debug";
    private static final String RELEASE = "release";
    private static final String HOCKEYAPP = "hockeyapp";

    public static boolean isDebug() {
        return DEBUG.equals(BuildConfig.BUILD_TYPE);
    }

    public static boolean isReleaseBuildType() {
        return RELEASE.equals(BuildConfig.BUILD_TYPE);
    }

    public static boolean isHockey() {
        return HOCKEYAPP.equals(BuildConfig.BUILD_TYPE);
    }
}
