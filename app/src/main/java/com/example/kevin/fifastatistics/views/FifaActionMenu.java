package com.example.kevin.fifastatistics.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.animation.CircularAnimationHelper;
import com.example.kevin.fifastatistics.utils.UiUtils;
import com.github.clans.fab.FloatingActionMenu;

public class FifaActionMenu extends FloatingActionMenu {

    private View mGradient;
    private int[] mCenterCoordinates;

    public FifaActionMenu(Context context) {
        this(context, null);
    }

    public FifaActionMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FifaActionMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setMenuButtonShowAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom));
        setMenuButtonHideAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_bottom));
        calculateCenterCoordinates();
    }

    private void calculateCenterCoordinates() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mCenterCoordinates = UiUtils.getCenterCoordinates(FifaActionMenu.this);
            }
        });
    }

    @Override
    public void open(boolean animate) {
        CircularAnimationHelper.revealOpenFrom(mGradient, mCenterCoordinates, getContext());
        super.open(animate);
    }

    @Override
    public void close(boolean animate) {
        CircularAnimationHelper.revealCloseTo(mGradient, mCenterCoordinates, getContext());
        super.close(animate);
    }

    public void setGradient(View gradient) {
        mGradient = gradient;
        initGradient();
    }

    private void initGradient() {
        mGradient.setOnClickListener(l -> close(true));
    }
}
