package com.example.kevin.fifastatistics.utils;

import android.content.Context;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResourceUtils {

    private static final Context mContext = FifaApplication.getContext();

    public static String getStringFromResourceId(int id) {
        return mContext.getResources().getString(id);
    }

    public static float getDimensionFromResourceId(int id) {
        return mContext.getResources().getDimension(id);
    }
}
