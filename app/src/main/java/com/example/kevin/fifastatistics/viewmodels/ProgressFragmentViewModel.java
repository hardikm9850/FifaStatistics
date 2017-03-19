package com.example.kevin.fifastatistics.viewmodels;

import android.databinding.Bindable;
import android.support.annotation.ColorRes;
import android.view.View;

import com.example.kevin.fifastatistics.BR;
import com.example.kevin.fifastatistics.FifaApplication;

public abstract class ProgressFragmentViewModel extends FifaBaseViewModel {

    private boolean mIsProgressBarShown = true;

    public void showProgressBar() {
        if (!mIsProgressBarShown) {
            doNotify(true);
        }
    }

    public void hideProgressBar() {
        if (mIsProgressBarShown) {
            doNotify(false);
        }
    }

    private void doNotify(boolean doShow) {
        mIsProgressBarShown = doShow;
        notifyPropertyChanged(BR.progressBarVisibility);
    }

    @Bindable
    public int getProgressBarVisibility() {
        return mIsProgressBarShown ? View.VISIBLE : View.GONE;
    }

    @ColorRes
    public int getColor() {
        return FifaApplication.getAccentColor();
    }
}
