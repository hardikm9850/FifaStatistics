package com.example.kevin.fifastatistics.animation;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.kevin.fifastatistics.interfaces.AnimationListener;
import com.example.kevin.fifastatistics.utils.UiUtils;

public class CircularAnimationHelper {

    public static void revealOpenCenter(View view, Context context) {
        int[] c = UiUtils.getCenterCoordinates(view);
        revealOpen(view, c[0], c[1], 200, context, null);
    }

    public static void revealCloseCenter(View view, Context context) {
        int[] c = UiUtils.getCenterCoordinates(view);
        revealClose(view, c[0], c[1], 200, context, null);
    }

    public static void revealOpenBottomRight(View view, Context context, final AnimationListener listener) {
        int[] br = UiUtils.getViewBottomRight(view);
        revealOpen(view, br[0], br[1], 200, context, listener);
    }

    public static void revealCloseBottomRight(View view, Context context, final AnimationListener listener) {
        int[] br = UiUtils.getViewBottomRight(view);
        revealClose(view, br[0], br[1], 200, context, listener);
    }

    /**
     * Perform a circular reveal for a view
     * @param view      the view to reveal
     * @param cx        the starting x position of the animation
     * @param cy        the starting y position of the animation
     * @param duration  the duration of the animation (millis)
     * @param context   the current context
     * @param listener  animation callback
     */
    public static void revealOpen(View view, int cx, int cy, int duration, Context context, final AnimationListener listener) {
        if (cx != 0 && cy != 0) {
            float finalRadius = getRadiusForCenters(cx, cy, context);
            Animator anim = getCircularAnimator(view, cx, cy, 0.0f, finalRadius, duration);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (listener != null) {
                        listener.onAnimationStarted();
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (listener != null) {
                        listener.onAnimationCompleted();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            view.setVisibility(View.VISIBLE);
            anim.start();
        } else {
            if (listener != null) {
                listener.onAnimationCompleted();
            }
        }
    }

    /**
     * Perform a circular conceal for a view
     * @param view      the view to conceal
     * @param cx        the finishing x position of the animation
     * @param cy        the finishing y position of the animation
     * @param duration  the duration of the animation (millis)
     * @param context   the current context
     * @param listener  animation callback
     */
    public static void revealClose(final View view, int cx, int cy, int duration, Context context, final AnimationListener listener) {
        if (cx != 0 && cy != 0) {
            float initialRadius = getRadiusForCenters(cx, cy, context);
            Animator anim = getCircularAnimator(view, cx, cy, initialRadius, 0.0f, duration);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (listener != null) {
                        listener.onAnimationStarted();
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.GONE);
                    if (listener != null) {
                        listener.onAnimationCompleted();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            anim.start();
        } else {
            if (listener != null) {
                listener.onAnimationCompleted();
            }
        }
    }

    private static float getRadiusForCenters(int centerX, int centerY, Context context) {
        Point displaySize = new Point();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(displaySize);
        return (float) Math.hypot(Math.max(centerX, displaySize.x - centerX), centerY);
    }

    private static Animator getCircularAnimator(View view, int cx, int cy, float startRadius, float endRadius, int duration) {
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, startRadius, endRadius);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);
        return anim;
    }
}
