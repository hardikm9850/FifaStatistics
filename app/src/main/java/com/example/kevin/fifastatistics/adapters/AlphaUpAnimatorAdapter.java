package com.example.kevin.fifastatistics.adapters;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.kevin.fifastatistics.listeners.StopAnimationScrollListener;

public class AlphaUpAnimatorAdapter<T extends RecyclerView.ViewHolder> extends it.gmariotti.recyclerview.adapter.AnimatorAdapter<T> {

    private static final String TRANSLATION_Y = "translationY";
    private static final String ALPHA = "alpha";
    private static final int START_Y_POSITION = 100;
    private static final int ANIMATION_DELAY_MILLIS = 50;

    public AlphaUpAnimatorAdapter(RecyclerView.Adapter<T> adapter, RecyclerView recyclerView) {
        super(adapter, recyclerView);
        setAnimationDelay();
        disableAnimationsWhenScrolling();
    }

    private void setAnimationDelay() {
        if (getViewAnimator() != null) {
            getViewAnimator().setAnimationDelayMillis(ANIMATION_DELAY_MILLIS);
        }
    }

    private void disableAnimationsWhenScrolling() {
        mRecyclerView.addOnScrollListener(new StopAnimationScrollListener(getViewAnimator()));
    }

    @NonNull
    @Override
    public Animator[] getAnimators(@NonNull View view) {
        Animator yAnimator = ObjectAnimator.ofFloat(view, TRANSLATION_Y, START_Y_POSITION, 0);
        Animator alphaAnimator = ObjectAnimator.ofFloat(view, ALPHA, 0, 1);
        return new Animator[]{yAnimator, alphaAnimator};
    }
}
