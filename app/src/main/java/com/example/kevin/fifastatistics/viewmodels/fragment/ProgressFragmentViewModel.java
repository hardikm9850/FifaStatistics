package com.example.kevin.fifastatistics.viewmodels.fragment;

import android.databinding.Bindable;
import android.support.annotation.ColorRes;
import android.view.View;

import com.example.kevin.fifastatistics.BR;
import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;

public abstract class ProgressFragmentViewModel extends FifaBaseViewModel {

    private boolean mIsProgressBarVisible = true;
    private boolean mIsRetryButtonVisible = false;

    public void showProgressBar() {
        if (!mIsProgressBarVisible) {
            doNotifyProgressChanged(true);
            doNotifyRetryChanged(false);
        }
    }

    public void hideProgressBar() {
        if (mIsProgressBarVisible) {
            doNotifyProgressChanged(false);
        }
    }

    public void showRetryButton() {
        doNotifyRetryChanged(true);
    }

    private void doNotifyProgressChanged(boolean doShow) {
        mIsProgressBarVisible = doShow;
        notifyPropertyChanged(BR.progressBarVisibility);
    }

    private void doNotifyRetryChanged(boolean doShow) {
        if (mIsRetryButtonVisible != doShow) {
            mIsRetryButtonVisible = doShow;
            notifyPropertyChanged(BR.retryButtonVisibility);
        }
    }

    @Bindable
    public int getProgressBarVisibility() {
        return mIsProgressBarVisible ? View.VISIBLE : View.GONE;
    }

    @Bindable
    public int getRetryButtonVisibility() {
        return mIsRetryButtonVisible ? View.VISIBLE : View.GONE;
    }

    @ColorRes
    public int getColor() {
        return FifaApplication.getAccentColor();
    }

    public abstract void onRetryButtonClick();
}
