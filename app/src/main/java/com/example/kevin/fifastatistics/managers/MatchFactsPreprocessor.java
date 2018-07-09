package com.example.kevin.fifastatistics.managers;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.kevin.fifastatistics.utils.BitmapUtils;

import java.io.Serializable;

import rx.Observable;

public class MatchFactsPreprocessor implements Serializable {

    private static final float SCALE_LEVEL = 4.8f;

    public final Observable<Bitmap> processBitmap(Bitmap matchFactsBitmap) {
        Log.d("ORIGINAL SIZE", "w: " + matchFactsBitmap.getWidth());
        return Observable.just(matchFactsBitmap)
                .map(BitmapUtils::getMutableBitmap)
                .map(b -> BitmapUtils.scaleDown(b, SCALE_LEVEL))
                .map(this::makeMonochrome)
                .map(this::invertColors)
                .map(this::increaseContrast)
                .map(this::invertColorsInHighlightedSection);
    }

    private Bitmap sharpen(Bitmap bitmap) {
        return BitmapUtils.sharpenBitmap(bitmap);
    }

    private Bitmap invertColors(Bitmap bitmap) {
        if (isDark(bitmap)) {
            Log.d("PREPROCESSOR", "inverting!!");
            return BitmapUtils.getInvertedBitmap(bitmap);
        } else {
            return bitmap;
        }
    }

    private boolean isDark(Bitmap bitmap) {
        final int width = bitmap.getWidth()/20;
        final int height = bitmap.getHeight()/10;
        int[] pixels = new int[width*height];
        int startX = width * 13;
        int startY = height * 5;
        bitmap.getPixels(pixels, 0, width, startX, startY, width, height);

        int darkPixels = 0;
        int lightPixels = 0;
        int darkThreshold = 0xff9b9b9b;
        for (int pixel : pixels) {
            if (pixel > darkThreshold) {
                lightPixels++;
            } else {
                darkPixels++;
            }
        }

        Log.d("PREPROCESSOR","dark pixels: " + darkPixels + ", light pixels: " + lightPixels);
        return darkPixels > lightPixels;
    }

    private Bitmap makeMonochrome(Bitmap bitmap) {
        return BitmapUtils.grayscale(bitmap);
    }

    private Bitmap increaseContrast(Bitmap bitmap) {
        return BitmapUtils.contrast(bitmap, 10);
    }

    private Bitmap invertColorsInHighlightedSection(Bitmap bitmap) {

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // GET LEFTMOST PIXELS

        int[] yPixels = new int[height];
        bitmap.getPixels(yPixels, 0, 1, width / 3, 0, 1, height);

        int blackThreshold = 0xffc4bcac;
        int whiteThreshold = 0xfff2efef;

        // GET END Y
        int endY = -1;
        for (int i = (yPixels.length - 1) / 3; i >= 0; i--){
            if (yPixels[i] < blackThreshold){
                endY = i;
                break;
            }
        }

        Log.d("processor", "end y: " + endY);

        if (endY == -1) return bitmap;

        // GET START Y
        int startY = -1;
        for (int i = endY - 1; i >= 0; i--){
            if (yPixels[i] > whiteThreshold){
                startY = i;
                break;
            }
        }

        Log.d("processor", "start y: " + startY);

        if (startY == -1) return bitmap;

        // UPDATE PIXELS IN BOUNDS

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int end = endY * width;
        int start = startY * width;
        for (int i = start; i < end; i++){
            if (pixels[i] > whiteThreshold) {
                pixels[i] = 0xff000000;
            } else {
                pixels[i] = 0xffffffff;
            }
        }

        // MAKE EVERYTHING ABOVE BOUNDS WHITE
        end = start - 3*width;
        for (int i = 0; i < end; i++) {
            pixels[i] = 0xffffffff;
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
