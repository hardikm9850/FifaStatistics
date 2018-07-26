package com.example.kevin.fifastatistics.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.kevin.fifastatistics.R;

public class AnimationHelper {

    public static void scaleIn(View view) {
        Animation anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_in);
        view.startAnimation(anim);
        anim.setFillAfter(true);
    }

    public static void scaleOut(View view) {
        Animation anim = AnimationUtils.loadAnimation(view.getContext(), R.anim.scale_out);
        view.startAnimation(anim);
        anim.setFillAfter(true);
    }
}
