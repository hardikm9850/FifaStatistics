package com.example.kevin.fifastatistics.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ProgressBar;

import com.example.kevin.fifastatistics.FifaApplication;

public class ColorUtils {

    private static final int LIGHT_TINT;
    private static final int DARK_TINT;

    static {
        Context context = FifaApplication.getContext();
        LIGHT_TINT = ContextCompat.getColor(context, android.R.color.white);
        DARK_TINT = ContextCompat.getColor(context, android.R.color.secondary_text_light);
    }

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
            icon = DrawableCompat.wrap(icon);
            if (!ColorUtils.isColorDark(color)) {
                DrawableCompat.setTint(icon.mutate(), DARK_TINT);
            } else {
                DrawableCompat.setTint(icon.mutate(), LIGHT_TINT);
            }
            return icon;
        } else {
            return null;
        }
    }
}
