package com.example.kevin.fifastatistics.managers;

import android.graphics.Bitmap;

import rx.Observable;

/**
 * Processor for FIFA 15 Match Facts.
 */
public class MatchFactsPreprocessor15 implements MatchFactsPreprocessor {

    @Override
    public Observable<Bitmap> processBitmap(Bitmap matchFactsBitmap) {
        return Observable.empty();
    }
}
