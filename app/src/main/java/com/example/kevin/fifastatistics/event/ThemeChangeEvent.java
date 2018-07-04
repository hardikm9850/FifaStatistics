package com.example.kevin.fifastatistics.event;

public class ThemeChangeEvent implements Event {

    public final int theme;

    public ThemeChangeEvent(int theme) {
        this.theme = theme;
    }
}
