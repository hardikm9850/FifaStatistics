package com.example.kevin.fifastatistics.interfaces;

import android.support.annotation.StringRes;

public interface ErrorHandler {

    void handleError(String message, Throwable throwable);
}
