package com.example.kevin.fifastatistics.utils;

import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SnackbarUtils {

    private static final String TAG = "Snackbar";

    public static void show(View container, int snackbarMessageResId, int length) {
        if (container != null) {
            container.post(() -> {
                    try {
                        Snackbar snackbar = Snackbar.make(container, snackbarMessageResId, length);
                        snackbar.show();
                    } catch (Exception ex) {
                        Log.e(TAG, "Failed to show snackbar: " + ex.getMessage());
                    }
                });
        }
    }
}
