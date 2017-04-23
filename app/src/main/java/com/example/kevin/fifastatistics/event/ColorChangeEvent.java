package com.example.kevin.fifastatistics.event;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;

public class ColorChangeEvent implements Event {

    public final int color;

    public ColorChangeEvent(@ColorInt int color) {
        this.color = color;
    }
}
