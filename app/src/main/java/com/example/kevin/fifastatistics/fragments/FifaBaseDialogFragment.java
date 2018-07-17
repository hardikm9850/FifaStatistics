package com.example.kevin.fifastatistics.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;

import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.interfaces.FragmentArguments;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class FifaBaseDialogFragment extends DialogFragment implements FragmentArguments, ActivityLauncher {

    private CompositeSubscription mCompositeSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onStop() {
        super.onStop();
        ObservableUtils.unsubscribeCompositeSubscription(mCompositeSubscription);
        mCompositeSubscription = new CompositeSubscription();
    }

    public void addSubscription(Subscription subscription) {
        if (mCompositeSubscription != null && subscription != null) {
            mCompositeSubscription.add(subscription);
        }
    }

    @Override
    public void launchActivity(Intent intent, int requestCode, Bundle options) {
        startActivityForResult(intent, requestCode, options);
    }
}
