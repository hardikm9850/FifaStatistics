package com.example.kevin.fifastatistics.utils;

import com.example.kevin.fifastatistics.BuildConfig;

public class BuildUtils {

    private static final String DEBUG = "debug";
    private static final String RELEASE = "release";
    private static final String HOCKEYAPP = "hockeyapp";

    public static boolean isDebug() {
        return is(DEBUG);
    }

    public static boolean isReleaseBuildType() {
        return is(RELEASE);
    }

    public static boolean isHockey() {
        return is(HOCKEYAPP);
    }

    private static boolean is(String type) {
        return type.equals(BuildConfig.BUILD_TYPE);
    }
}
