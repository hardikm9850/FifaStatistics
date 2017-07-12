package com.example.kevin.fifastatistics.event;

import android.support.annotation.ColorInt;

public class ColorChangeEvent implements Event {

    public final int color;

    public ColorChangeEvent(@ColorInt int color) {
        this.color = color;
    }
}
