package com.example.kevin.fifastatistics.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.widget.ProgressBar;

import com.example.kevin.fifastatistics.FifaApplication;

public class ColorUtils {

    public static void setProgressBarColor(ProgressBar progressBar, @ColorInt int color) {
        progressBar.getIndeterminateDrawable().setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_ATOP);
    }

    public static boolean isColorDark(int color){
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness > 0.5;
    }

    public static Drawable getTintedDrawable(@DrawableRes int drawable, @ColorInt int color) {
        Context context = FifaApplication.getContext();
        Drawable icon = context.getDrawable(drawable);
        if (icon != null) {
            if (!ColorUtils.isColorDark(color)) {
                icon.setTint(ContextCompat.getColor(context, android.R.color.secondary_text_light));
            } else {
                icon.setTint(ContextCompat.getColor(context, android.R.color.white));
            }
        }
        return icon;
    }
}
