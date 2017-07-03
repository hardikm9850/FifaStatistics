package com.example.kevin.fifastatistics.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.event.ColorChangeEvent;
import com.example.kevin.fifastatistics.event.EventBus;
import com.example.kevin.fifastatistics.interfaces.OnBackPressedHandler;
import com.example.kevin.fifastatistics.interfaces.TransitionStarter;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class FifaBaseActivity extends AppCompatActivity implements TransitionStarter {

    public static final String EXTRA_HASH_CODE = "extraHashCode";

    protected int mColor;
    private OnBackPressedHandler mBackPressHandler;
    private CompositeSubscription mCompositeSubscription;
    private int mCallingActivityHashCode;

    public FifaBaseActivity() {
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initColors();
        mCallingActivityHashCode = getIntent() != null ? getIntent().getIntExtra(EXTRA_HASH_CODE, 0) : 0;
    }

    private void initColors() {
        mColor = FifaApplication.getAccentColor();
        EventBus.getInstance().observeEvents(ColorChangeEvent.class).subscribe(event -> {
            mColor = event.color;
            onColorUpdated();
        });
    }

    protected void onColorUpdated() {}

    public void setNavigationLocked(boolean locked) {}

    @Override
    public void onBackPressed() {
        if (performHandlerBackPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ObservableUtils.unsubscribeCompositeSubscription(mCompositeSubscription);
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finishAfterTransition() {
        if (FifaActivityLifecycleCallbacks.isActivityDestroyed(mCallingActivityHashCode)) {
            finish();
        } else {
            super.finishAfterTransition();
        }
    }

    @Override
    public void startTransition() {
        supportStartPostponedEnterTransition();
    }

    public void addSubscription(Subscription subscription) {
        if (mCompositeSubscription != null && subscription != null) {
            mCompositeSubscription.add(subscription);
        }
    }

    public boolean performHandlerBackPress() {
        return mBackPressHandler != null && mBackPressHandler.handleBackPress();
    }

    public void setOnBackPressHandler(OnBackPressedHandler handler) {
        mBackPressHandler = handler;
    }
}
