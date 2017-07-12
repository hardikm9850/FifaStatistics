package com.example.kevin.fifastatistics.animation;

import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class AnimationUtils {

    public static final int DURATION_MED = 300;

    public static void slideInBottom(@NonNull View view) {
        if (view.getVisibility() != View.VISIBLE) {
            ObjectAnimator a = ObjectAnimator.ofInt(view, "bottom", 0, view.getHeight());
            a.setDuration(DURATION_MED);
            a.setInterpolator(new LinearInterpolator());
            view.setVisibility(View.VISIBLE);
            a.start();
        }
    }
}
