package com.example.kevin.fifastatistics.animation;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.interfaces.AnimationListener;

/**
 * Created by kevin on 2017-06-30.
 */

public class AnimationBindingAdapter {

    private static final int CIRCLE_ANIMATION_DURATION_MILLIS = 900;
    private static final int ALPHA_ANIMATION_DURATION_MILLIS = 300;

    private static final Animation SLIDE_IN_ANIMATION;
    private static final Animation SLIDE_OUT_ANIMATION;

    static {
        Context c = FifaApplication.getContext();
        SLIDE_IN_ANIMATION = AnimationUtils.loadAnimation(c, R.anim.slide_in_bottom);
        SLIDE_IN_ANIMATION.setFillAfter(true);
        SLIDE_OUT_ANIMATION = AnimationUtils.loadAnimation(c, R.anim.slide_out_bottom);
        SLIDE_IN_ANIMATION.setFillAfter(true);
    }

    @BindingAdapter(value = "alphaScaleVisibility")
    public static void alphaScale(final View view, int visible) {
        doAnimation(view, R.anim.match_update_animation, R.anim.shrink_fade_out_bottom, visible, 0L);
    }

    @BindingAdapter(value = {"slideVisibility", "slideDuration"}, requireAll = false)
    public static void slideInOut(final View view, int visible, long duration) {
        doAnimation(view, R.anim.slide_in_bottom, R.anim.slide_out_bottom, visible, duration);
    }

    private static void doAnimation(final View view, int showAnimation, int hideAnimation, int visible, long duration) {
        Animation animation;
        if (view.getVisibility() == View.VISIBLE && visible != View.VISIBLE) {
            animation = AnimationUtils.loadAnimation(view.getContext(), hideAnimation);
        } else {
            animation = AnimationUtils.loadAnimation(view.getContext(), showAnimation);
        }
        if (duration > 0L) {
            animation.setDuration(duration);
        }
        view.setAnimation(animation);
        view.setVisibility(visible);
        view.animate();
    }

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
