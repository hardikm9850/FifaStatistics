package com.example.kevin.fifastatistics.views.databinding;

import android.databinding.BindingAdapter;
import android.support.design.widget.AppBarLayout;

public class AppBarBindingAdapter {

    @BindingAdapter("onOffsetChangedListener")
    public static void onOffsetChanged(AppBarLayout appBar, AppBarLayout.OnOffsetChangedListener listener) {
        appBar.addOnOffsetChangedListener(listener);
    }
}
