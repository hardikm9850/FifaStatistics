package com.example.kevin.fifastatistics.utils;

import com.example.kevin.fifastatistics.FifaApplication;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResourceUtils {

    public static String getStringFromResourceId(int id) {
        return FifaApplication.getContext().getResources().getString(id);
    }

    public static float getDimensionFromResourceId(int id) {
        return FifaApplication.getContext().getResources().getDimension(id);
    }
}
