package com.example.kevin.fifastatistics.viewmodels;

import android.databinding.Bindable;
import android.view.View;

import com.example.kevin.fifastatistics.BR;

public abstract class ProgressFragmentViewModel extends FifaBaseViewModel {

    private boolean mDoShowProgressBar = true;

    protected void notifyShowProgressBar() {
        doNotify(true);
    }

    protected void notifyHideProgressBar() {
        doNotify(false);
    }

    private void doNotify(boolean doShow) {
        mDoShowProgressBar = doShow;
        notifyPropertyChanged(BR.progressBarVisibility);
    }

    @Bindable
    public int getProgressBarVisibility() {
        return mDoShowProgressBar ? View.VISIBLE : View.GONE;
    }
}
