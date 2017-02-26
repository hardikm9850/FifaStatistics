package com.example.kevin.fifastatistics.viewmodels;

import android.databinding.BaseObservable;

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
}