package com.example.kevin.fifastatistics.managers;

import android.graphics.Bitmap;

import com.example.kevin.fifastatistics.utils.BitmapUtils;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import rx.Observable;

/**
 * Processor for FIFA 15 Match Facts.
 */
public class MatchFactsPreprocessor15 implements MatchFactsPreprocessor {

    @Override
    public Observable<Bitmap> processBitmap(Bitmap matchFactsBitmap) {
        return Observable.just(matchFactsBitmap)
                .compose(ObservableUtils.applySchedulers())
                .map(BitmapUtils::getMutableBitmap)
                .map(this::sharpen)
                .map(this::invertColors)
                .map(this::increaseContrast)
                .map(this::invertColorsInHighlightedSection);
    }

    private Bitmap sharpen(Bitmap bitmap) {
        return bitmap;
    }

    private Bitmap invertColors(Bitmap bitmap) {
        return BitmapUtils.getInvertedBitmap(bitmap);
    }

    private Bitmap increaseContrast(Bitmap bitmap) {
        return bitmap;
    }

    private Bitmap invertColorsInHighlightedSection(Bitmap bitmap) {
        return bitmap;
    }
    
}
