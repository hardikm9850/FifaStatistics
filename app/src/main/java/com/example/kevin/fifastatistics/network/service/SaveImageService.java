package com.example.kevin.fifastatistics.network.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;

import com.example.kevin.fifastatistics.managers.OcrManager;
import com.example.kevin.fifastatistics.utils.ByteHolder;
import com.example.kevin.fifastatistics.utils.PhotoUtils;

import java.util.Date;

public class SaveImageService extends IntentService {

    private static final String NAME = "SAVE_IMAGE_SERVICE";

    public SaveImageService() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bitmap image = ByteHolder.getImage();
        saveImage(image);
        ByteHolder.dispose();
        OcrManager.clear();
    }

    private void saveImage(Bitmap image) {
        Date now = new Date();
        String title = "facts_" + now.toString();
        PhotoUtils.saveToGallery(this, image, title, null);
    }
}
