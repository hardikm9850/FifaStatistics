package com.example.kevin.fifastatistics.managers;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.kevin.fifastatistics.interfaces.MatchFactsPreprocessor;
import com.example.kevin.fifastatistics.utils.BitmapUtils;

import rx.Observable;

/**
 * Processor for FIFA 15 Match Facts.
 */
public class MatchFactsPreprocessor15 implements MatchFactsPreprocessor {

    public static final float SCALE_LEVEL = 4.8f;

    @Override
    public Observable<Bitmap> processBitmap(Bitmap matchFactsBitmap) {
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
        return BitmapUtils.getInvertedBitmap(bitmap);
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
