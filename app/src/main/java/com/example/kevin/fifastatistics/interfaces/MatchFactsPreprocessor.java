package com.example.kevin.fifastatistics.interfaces;

import android.graphics.Bitmap;

import java.io.Serializable;

import rx.Observable;

public interface MatchFactsPreprocessor extends Serializable {

    /**
     * Apply preprocessing to a photo of match facts to make the image more suitable for OCR.
     */
    Observable<Bitmap> processBitmap(Bitmap matchFactsBitmap);
}
