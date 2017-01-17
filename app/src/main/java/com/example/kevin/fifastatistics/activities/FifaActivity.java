package com.example.kevin.fifastatistics.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.kevin.fifastatistics.interfaces.OnBackPressedHandler;
import com.example.kevin.fifastatistics.utils.ObservableUtils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class FifaActivity extends AppCompatActivity {

    private OnBackPressedHandler mBackPressHandler;
    private CompositeSubscription mCompositeSubscription;

    public abstract Toolbar getToolbar();
    public abstract View getParentLayout();

    public FifaActivity() {
        mCompositeSubscription = new CompositeSubscription();
    }

    public void setNavigationLocked(boolean locked) {};

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
