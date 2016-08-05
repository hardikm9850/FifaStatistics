package com.example.kevin.fifastatistics.utils;

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
}
