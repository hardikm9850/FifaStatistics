package com.example.kevin.fifastatistics.views.databinding;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.utils.ColorUtils;

public class ToolbarBindingAdapter {

    @BindingAdapter("initToolbar")
    public static void style(Toolbar toolbar, int ignore) {
        int color = ColorUtils.getPrimaryTextColor(toolbar.getContext());
        setTitleColor(toolbar, color);
    }

    private static void setTitleColor(Toolbar toolbar, @ColorInt int color) {
        toolbar.setTitleTextColor(color);
        int children = toolbar.getChildCount();
        for (int i = 0; i < children; i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                Log.d("toolbar", "Setting text color!");
                ((TextView) view).setTextColor(color);
            }
        }
    }
}
