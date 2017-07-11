package com.example.kevin.fifastatistics.animation;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.interfaces.AnimationListener;

/**
 * Created by kevin on 2017-06-30.
 */

public class AnimationBindingAdapter {

    private static final int CIRCLE_ANIMATION_DURATION_MILLIS = 900;
    private static final int ALPHA_ANIMATION_DURATION_MILLIS = 300;

    @BindingAdapter("animatedAlpha")
    public static void setAlpha(@NonNull final View view, float alpha) {
        view.animate().alpha(alpha).setDuration(ALPHA_ANIMATION_DURATION_MILLIS).start();
    }

    @BindingAdapter(value = {"circularVisibility", "animationDuration"}, requireAll = false)
    public static void setVisbilityWithCircularAnimation(final View view, final int visibility, int duration) {
        final Context context = view.getContext();
        if (context == null) {
            view.setVisibility(visibility);
            return;
        }
        Integer endAnimVisibility = (Integer) view.getTag(R.id.finalVisibility);
        boolean isAlreadyAnimating = endAnimVisibility != null && endAnimVisibility == visibility;
        boolean isSameVisibility = view.getVisibility() == visibility;
        if (isAlreadyAnimating || isSameVisibility) {
            return;
        }
        if (duration <= 0) {
            duration = CIRCLE_ANIMATION_DURATION_MILLIS;
        }
        if (visibility == View.VISIBLE) {
            CircularAnimationHelper.revealOpen(view, view.getWidth() / 2, view.getHeight() / 2, duration, context, getVisibilityListener(view, visibility));
        } else {
            CircularAnimationHelper.revealClose(view, view.getWidth() / 2, view.getHeight() / 2, duration, context, getVisibilityListener(view, visibility));
        }
    }

    private static AnimationListener getVisibilityListener(final View view, final int visibility) {
        return new AnimationListener() {
            boolean isCanceled;
            @Override
            public void onAnimationStarted() {
                view.setTag(R.id.finalVisibility, visibility);
            }

            @Override
            public void onAnimationCompleted() {
                view.setTag(R.id.finalVisibility, null);
                if (!isCanceled) {
                    view.setVisibility(visibility);
                }
            }

            @Override
            public void onAnimationCanceled() {
                isCanceled = true;
            }
        };
    }
}
