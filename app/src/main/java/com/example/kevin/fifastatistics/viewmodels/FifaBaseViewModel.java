package com.example.kevin.fifastatistics.viewmodels;

import android.databinding.BaseObservable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class FifaBaseViewModel extends BaseObservable {

    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public void addSubscription(Subscription subscription) {
        if (mCompositeSubscription != null && subscription != null) {
            mCompositeSubscription.add(subscription);
        }
    }

    public void unsubscribeAll() {
        ObservableUtils.unsubscribeCompositeSubscription(mCompositeSubscription);
    }

    public void destroy() {
        unsubscribeAll();
    }

    @ColorInt
    protected int getColor(int colorRes) {
        if (colorRes > 0) {
            return ContextCompat.getColor(FifaApplication.getContext(), colorRes);
        } else {
            return 0;
        }
    }

    protected Drawable getDrawable(@DrawableRes int drawable) {
        if (drawable > 0) {
            return ContextCompat.getDrawable(FifaApplication.getContext(), drawable);
        } else {
            return null;
        }
    }
}
