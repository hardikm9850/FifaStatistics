package com.example.kevin.fifastatistics.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.animation.CircularAnimationHelper;
import com.github.clans.fab.FloatingActionMenu;

/**
 * Created by kevin on 2017-06-30.
 */

public class FifaActionMenu extends FloatingActionMenu {

    private View mGradient;

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
    }

    @Override
    public void open(boolean animate) {
        CircularAnimationHelper.revealOpenBottomRight(mGradient, getContext(), null);
        super.open(animate);
    }

    @Override
    public void close(boolean animate) {
        CircularAnimationHelper.revealCloseBottomRight(mGradient, getContext(), null);
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
