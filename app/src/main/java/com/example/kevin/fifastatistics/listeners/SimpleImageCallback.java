package com.example.kevin.fifastatistics.listeners;

import android.graphics.Bitmap;
import android.view.View;

import com.example.kevin.fifastatistics.interfaces.ImageCallback;
import com.nostra13.universalimageloader.core.assist.FailReason;

public class SimpleImageCallback implements ImageCallback {

    @Override
    public void onLoadingStarted(String imageUri, View view) {

    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {

    }
}
