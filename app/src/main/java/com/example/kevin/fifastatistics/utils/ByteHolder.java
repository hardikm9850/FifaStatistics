package com.example.kevin.fifastatistics.utils;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;

public class ByteHolder {

    private static byte[] image;
    private static Bitmap bitmap;

    public static void setData(@Nullable byte[] image) {
        ByteHolder.image = image;
    }

    @Nullable
    public static byte[] getData() {
        return image;
    }

    public static void setImage(Bitmap bitmap) {
        ByteHolder.bitmap = bitmap;
    }

    public static Bitmap getImage() {
        return bitmap;
    }

    public static void dispose() {
        image = null;
        bitmap.recycle();
        bitmap = null;
    }
}
