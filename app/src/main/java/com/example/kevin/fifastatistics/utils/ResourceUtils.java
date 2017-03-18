package com.example.kevin.fifastatistics.utils;

import android.support.annotation.NonNull;

import com.example.kevin.fifastatistics.FifaApplication;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResourceUtils {

    public static String getStringFromResourceId(int id) {
        return FifaApplication.getContext().getResources().getString(id);
    }

    public static String getStringFromResourceId(int id, @NonNull String... placeholders) {
        return FifaApplication.getContext().getResources().getString(id, placeholders);
    }

    public static float getDimensionFromResourceId(int id) {
        return FifaApplication.getContext().getResources().getDimension(id);
    }

    public static int spToPixels(int sp) {
        float scaledDensity = FifaApplication.getContext().getResources().getDisplayMetrics().scaledDensity;
        return Math.round(sp * scaledDensity);
    }
}
