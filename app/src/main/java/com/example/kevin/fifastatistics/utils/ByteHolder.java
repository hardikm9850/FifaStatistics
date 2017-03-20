package com.example.kevin.fifastatistics.utils;

import android.support.annotation.Nullable;

public class ByteHolder {

    private static byte[] image;

    public static void setImage(@Nullable byte[] image) {
        ByteHolder.image = image;
    }

    @Nullable
    public static byte[] getImage() {
        return image;
    }

    public static void dispose() {
        image = null;
    }
}
