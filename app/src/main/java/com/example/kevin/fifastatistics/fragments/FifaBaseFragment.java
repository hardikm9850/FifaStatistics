package com.example.kevin.fifastatistics.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.event.ColorChangeEvent;
import com.example.kevin.fifastatistics.event.EventBus;
import com.example.kevin.fifastatistics.event.SnackbarEvent;
import com.example.kevin.fifastatistics.interfaces.ActivityLauncher;
import com.example.kevin.fifastatistics.interfaces.FragmentArguments;
import com.example.kevin.fifastatistics.interfaces.TransitionStarter;
import com.example.kevin.fifastatistics.utils.ObservableUtils;
import com.example.kevin.fifastatistics.utils.SnackbarUtils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class FifaBaseFragment extends Fragment implements ActivityLauncher, FragmentArguments {

    protected static final String ARG_ERROR = "errorMessage";
    protected static final String ARG_USER = "user";

    protected TransitionStarter mTransitionStarter;
    protected int mColor;
    private CompositeSubscription mCompositeSubscription;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TransitionStarter) {
            mTransitionStarter = (TransitionStarter) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTransitionStarter = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompositeSubscription = new CompositeSubscription();
        observerColorEvents();
        observeMessageEvents();
    }

    private void observerColorEvents() {
        mColor = FifaApplication.getAccentColor();
        EventBus.getInstance().observeEvents(ColorChangeEvent.class).subscribe(event -> {
            mColor = event.color;
            onColorUpdated();
        });
    }

    protected void onColorUpdated() {}

    private void observeMessageEvents() {
        EventBus.getInstance().observeEvents(SnackbarEvent.class).subscribe(event -> {
            SnackbarUtils.show(getView(), event.message, event.length);
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        ObservableUtils.unsubscribeCompositeSubscription(mCompositeSubscription);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void launchActivity(Intent intent, int requestCode, Bundle options) {
        if (isAdded()) {
            if (requestCode > 0) {
                startActivityForResult(intent, requestCode, options);
            } else {
                startActivity(intent, options);
            }
        } else {
            SnackbarUtils.show(getView(), R.string.error_generic, Snackbar.LENGTH_SHORT);
        }
    }

    public void addSubscription(Subscription subscription) {
        if (mCompositeSubscription != null && subscription != null) {
            mCompositeSubscription.add(subscription);
        }
    }
}
