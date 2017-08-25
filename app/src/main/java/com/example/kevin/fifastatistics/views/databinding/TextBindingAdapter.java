package com.example.kevin.fifastatistics.views.databinding;

import android.graphics.Typeface;
import android.widget.TextView;

public class TextBindingAdapter {

    @android.databinding.BindingAdapter("android:typeface")
    public static void setTypeface(TextView v, String style) {
        switch (style) {
            case "bold":
                v.setTypeface(null, Typeface.BOLD);
                break;
            case "italic":
                v.setTypeface(null, Typeface.ITALIC);
                break;
            default:
                v.setTypeface(null, Typeface.NORMAL);
        }
    }
}
