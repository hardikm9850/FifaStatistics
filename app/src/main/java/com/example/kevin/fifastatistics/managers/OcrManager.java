package com.example.kevin.fifastatistics.managers;

import android.graphics.Bitmap;

import com.example.kevin.fifastatistics.models.databasemodels.user.User.StatsPair;

import rx.Observable;

public class OcrManager {

    private static final OcrManager mInstance = new OcrManager();

    private Bitmap mBitmap;

    public static OcrManager getInstance(Bitmap bitmap) {
        mInstance.mBitmap = bitmap;
        return mInstance;
    }

    public Observable<StatsPair> retrieveFacts() {
        return Observable.empty();
    }
}
