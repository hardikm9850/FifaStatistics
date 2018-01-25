package com.example.kevin.fifastatistics.views.databinding;

import android.databinding.BindingAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * To use this adapter for width and height, you must specify both android:layout_width and app:layout_width
 * (or height) attributes. This adapter will override the android attribute. If you only set the app attribute,
 * a runtime exception will be thrown. <p>
 * If you add binding adapters for margins, etc. You can used the android namespace, e.g.
 * <code>@BindingAdapter("android:layout_marginTop")</code>
 * <p>
 */
public class LayoutParamsBindingAdapter {

    @BindingAdapter("layout_height")
    public static void setLayoutHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams.height != height) {
            layoutParams.height = height;
            view.setLayoutParams(layoutParams);
        }
    }

    @BindingAdapter("layout_width")
    public static void setLayoutWidth(View view, int width) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams.width != width) {
            layoutParams.width = width;
            view.setLayoutParams(layoutParams);
        }
    }

    @BindingAdapter("android:layout_weight")
    public static void setLayoutWeight(View view, float weight) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.weight = weight;
        view.setLayoutParams(params);
    }

    @BindingAdapter("android:layout_marginEnd")
    public static void setEndMargin(View view, float endMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (endMargin != layoutParams.getMarginEnd()) {
            layoutParams.setMargins(layoutParams.leftMargin, layoutParams.topMargin, Math.round(endMargin), layoutParams.bottomMargin);
        }
    }

    @BindingAdapter("android:layout_marginTop")
    public static void setTopMargin(View view, float topMargin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (topMargin != layoutParams.topMargin) {
            layoutParams.setMargins(layoutParams.leftMargin, Math.round(topMargin), layoutParams.rightMargin, layoutParams.bottomMargin);
        }
    }

    @BindingAdapter("android:layout_below")
    public static void setLayoutBelow(View view, int id) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, id);
    }
}