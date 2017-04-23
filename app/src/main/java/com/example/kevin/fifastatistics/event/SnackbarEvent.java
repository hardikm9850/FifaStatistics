package com.example.kevin.fifastatistics.event;

import android.support.annotation.StringRes;

public class SnackbarEvent implements Event {

    public final int message;
    public final int length;

    public SnackbarEvent(@StringRes int message, int length) {
        this.message = message;
        this.length = length;
    }
}
