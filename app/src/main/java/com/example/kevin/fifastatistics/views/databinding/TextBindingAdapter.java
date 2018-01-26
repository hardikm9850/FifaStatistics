package com.example.kevin.fifastatistics.views.databinding;

import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.TextView;

public class TextBindingAdapter {

    @BindingAdapter("android:typeface")
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

    @BindingAdapter("inputFilter")
    public static void setInputFilter(EditText editText, InputFilter filter) {
        if (editText != null) {
            editText.setFilters(new InputFilter[]{filter});
        }
    }
}
