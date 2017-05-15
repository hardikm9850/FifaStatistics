package com.example.kevin.fifastatistics.utils;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.widget.Toast;

import lombok.experimental.UtilityClass;

/**
 * Utility class for creating toasts.
 */
@UtilityClass
public class ToastUtils {

    public static void showShortToast(Activity activity, String message) {
        if (activity != null) {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
        }
    }

    public static void showShortToast(Activity activity, @StringRes int message) {
        if (activity != null) {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
        }
    }

    public static void showLongToast(Activity activity, String message) {
        if (activity != null) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        }
    }
}
