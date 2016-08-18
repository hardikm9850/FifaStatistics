package com.example.kevin.fifastatistics.utils;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.example.kevin.fifastatistics.activities.FifaActivity;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SnackbarUtils {

    public static Snackbar getShortSnackbar(FifaActivity activity, String message) {
        return Snackbar.make(activity.getParentLayout(), message, Snackbar.LENGTH_SHORT);
    }

    public static Snackbar getLongSnackbar(FifaActivity activity, String message) {
        return Snackbar.make(activity.getParentLayout(), message, Snackbar.LENGTH_LONG);
    }

    public static Snackbar getRetrySnackbar(FifaActivity activity, String message,
                                            View.OnClickListener listener) {
        return getLongSnackbar(activity, message)
                .setAction("RETRY", listener)
                .setActionTextColor(Color.RED);
    }
}
