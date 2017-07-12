package com.example.kevin.fifastatistics.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

import lombok.experimental.UtilityClass;

/**
 * Utility class for creating toasts.
 */
@UtilityClass
public class ToastUtils {

    public static void showShortToast(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    public static void showShortToast(Context context, @StringRes int message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    public static void showLongToast(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
