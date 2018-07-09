package com.example.kevin.fifastatistics.managers;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.listeners.SimpleObserver;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import java.io.IOException;

import rx.Observable;
import rx.Observer;

public class StatsImageHandler {

    private final Context mContext;
    private final StatsImageHandlerInteraction mInteraction;

    public StatsImageHandler(Context context, StatsImageHandlerInteraction interaction) {
        mContext = context;
        mInteraction = interaction;
    }

    public void processImage(final byte[] picture) {
        final MatchFactsPreprocessor preprocessor = new MatchFactsPreprocessor();
        ProgressDialog dialog = showProcessingDialog();
        Observable.<Bitmap>create(s -> {
            Bitmap result = BitmapFactory.decodeByteArray(picture, 0, picture.length);
            s.onNext(result);
        })
                .flatMap(preprocessor::processBitmap)
                .map(this::getMatchFacts)
                .compose(ObservableUtils.applySchedulers())
                .subscribe(getStatsObserver(dialog));
    }

    private ProgressDialog showProcessingDialog() {
        ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setTitle(mContext.getString(R.string.processing_image));
        dialog.setMessage(mContext.getString(R.string.please_wait));
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    private User.StatsPair getMatchFacts(Bitmap b) {
        OcrManager manager = OcrManager.getInstance(b);
        try {
            return manager.retrieveFacts(mContext);
        } catch (IOException e) {
            return null;
        }
    }

    private Observer<User.StatsPair> getStatsObserver(ProgressDialog dialog) {
        return new SimpleObserver<User.StatsPair>() {
            @Override
            public void onError(Throwable e) {
                onOcrResultReceived(dialog);
                mInteraction.onStatsRetrievalError();
            }

            @Override
            public void onNext(User.StatsPair statsPair) {
                onOcrResultReceived(dialog);
                mInteraction.onStatsRetrieved(statsPair);
            }
        };
    }

    private void onOcrResultReceived(ProgressDialog dialog) {
        dialog.cancel();
        System.gc();
    }

    public interface StatsImageHandlerInteraction {
        void onStatsRetrieved(User.StatsPair statsPair);
        void onStatsRetrievalError();
    }
}
