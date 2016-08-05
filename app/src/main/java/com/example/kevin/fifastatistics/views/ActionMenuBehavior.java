package com.example.kevin.fifastatistics.views;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.List;

@SuppressWarnings("unused")
public class ActionMenuBehavior extends CoordinatorLayout.Behavior<FloatingActionsMenu> {

    private float mTranslationY;

    public ActionMenuBehavior() {
        super();
    }

    public ActionMenuBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private static float getFabTranslationYForSnackbar(CoordinatorLayout parent, FloatingActionsMenu fab) {
        float minOffset = 0.0F;
        List<View> dependencies = parent.getDependencies(fab);
        int i = 0;

        for (int z = dependencies.size(); i < z; ++i) {
            View view = dependencies.get(i);
            if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(fab, view)) {
                minOffset = Math.min(minOffset, ViewCompat.getTranslationY(view) - (float) view.getHeight());
            }
        }

        return minOffset;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionsMenu child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionsMenu child, View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            this.updateFabTranslationForSnackbar(parent, child, dependency);
        }

        return false;
    }

    private void updateFabTranslationForSnackbar(CoordinatorLayout parent, FloatingActionsMenu fab, View snackbar) {
        float translationY = ActionMenuBehavior.getFabTranslationYForSnackbar(parent, fab);
        if (translationY != this.mTranslationY) {
            ViewCompat.animate(fab).cancel();
            if (Math.abs(translationY - this.mTranslationY) == (float) snackbar.getHeight()) {
                ViewCompat.animate(fab).translationY(translationY).setInterpolator(new FastOutSlowInInterpolator()).setListener(null);
            } else {
                ViewCompat.setTranslationY(fab, translationY);
            }

            this.mTranslationY = translationY;
        }

    }
}
