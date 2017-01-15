package com.example.kevin.fifastatistics.activities;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.kevin.fifastatistics.utils.ObservableUtils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class FifaActivity extends AppCompatActivity {

    protected OnBackPressedHandler mBackPressHandler;
    private CompositeSubscription mCompositeSubscription;

    public abstract Toolbar getToolbar();
    public abstract void setNavigationLocked(boolean locked);
    public abstract View getParentLayout();

    public FifaActivity() {
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onBackPressed() {
        if (mBackPressHandler != null && mBackPressHandler.handleBackPress()) {
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

    public interface OnBackPressedHandler {

        /**
         * return true if the handler will handle the back press, or false if handling should
         * fall through to the activity.
         */
        boolean handleBackPress();
    }
}
