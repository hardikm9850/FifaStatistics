package com.example.kevin.fifastatistics.managers;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.kevin.fifastatistics.utils.BitmapUtils;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import rx.Observable;

/**
 * Processor for FIFA 15 Match Facts.
 */
public class MatchFactsPreprocessor15 implements MatchFactsPreprocessor {

    @Override
    public Observable<Bitmap> processBitmap(Bitmap matchFactsBitmap) {
        Log.d("ORIGINAL SIZE", "w: " + matchFactsBitmap.getWidth());
        return Observable.just(matchFactsBitmap)
                .map(BitmapUtils::getMutableBitmap)
                .map(this::makeMonochrome)
                .map(this::invertColors)
                .map(this::increaseContrast)
                .map(this::invertColorsInHighlightedSection)
                .map(b -> BitmapUtils.scaleDown(b, 4.0f));
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
        bitmap.getPixels(yPixels, 0, 1, 0, 0, 1, height);

        int blackThreshold = 0xff494844;
        int whiteThreshold = 0xfffdfdfd;

        // GET START Y
        int startY = -1;
        for (int i = 0; i < yPixels.length; i++){
            if (yPixels[i] < blackThreshold){
                startY = i;
                break;
            }
        }

        if (startY == -1) return bitmap;

        // GET END Y
        int endY = -1;
        for (int i = startY + 1; i < yPixels.length; i++){
            if (yPixels[i] > whiteThreshold){
                endY = i;
                break;
            }
        }

        if (endY == -1) return bitmap;

        // UPDATE PIXELS IN BOUNDS

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int end = endY * width;
        for (int i = startY * width; i < end; i++){
            if (pixels[i] < blackThreshold){
                pixels[i] = 0xffffffff;
            } else if (pixels[i] > whiteThreshold) {
                pixels[i] = 0xff000000;
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
