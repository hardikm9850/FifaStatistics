package com.example.kevin.fifastatistics.interfaces;

public interface OnBackPressedHandler {

    /**
     * return true if the handler will handle the back press, or false if handling should
     * fall through to the activity.
     */
    boolean handleBackPress();
}
