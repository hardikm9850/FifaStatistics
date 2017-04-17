package com.example.kevin.fifastatistics.managers;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.models.databasemodels.user.User.StatsPair;
import com.example.kevin.fifastatistics.utils.OcrResultParser;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.IOException;

public class OcrManager {

    private static final OcrManager INSTANCE = new OcrManager();
    private static TessBaseAPI api;

    private static final String OCR_DATA_FILE = "eng";
    private static final String OCR_PATH = FifaApplication.getContext().getExternalFilesDir(null).getPath();

    private Bitmap mBitmap;

    public static OcrManager getInstance(Bitmap bitmap) {
        INSTANCE.mBitmap = bitmap;
        return INSTANCE;
    }

    public StatsPair retrieveFacts() throws IOException {
        String result = getText();
        mBitmap.recycle();
        return OcrResultParser.newInstance(result).parse();
    }

    private String getText() {
        initApi();
        api.setImage(mBitmap);
        String text = api.getUTF8Text();
        Log.d("OCR", text);
        api.clear();
        return text;
    }

    private void initApi() {
        if (api == null) {
            api = new TessBaseAPI();
            api.init(OCR_PATH, OCR_DATA_FILE);
        }
    }
}
