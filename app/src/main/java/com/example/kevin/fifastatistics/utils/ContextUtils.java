package com.example.kevin.fifastatistics.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class ContextUtils {

    @Nullable
    public static AppCompatActivity getActivityFromContext(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof AppCompatActivity) {
                return (AppCompatActivity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }
}
