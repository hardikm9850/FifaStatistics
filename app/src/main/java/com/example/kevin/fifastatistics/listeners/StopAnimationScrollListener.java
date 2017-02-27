package com.example.kevin.fifastatistics.listeners;

import android.support.v7.widget.RecyclerView;

import it.gmariotti.recyclerview.adapter.helper.ViewAnimator;

public class StopAnimationScrollListener extends RecyclerView.OnScrollListener {

    private boolean mIsAnimationStopped = false;
    private final ViewAnimator mViewAnimator;

    public StopAnimationScrollListener(ViewAnimator viewAnimator) {
        mViewAnimator = viewAnimator;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (!mIsAnimationStopped && newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            mViewAnimator.disableAnimations();
            mIsAnimationStopped = true;
        }
    }
}
