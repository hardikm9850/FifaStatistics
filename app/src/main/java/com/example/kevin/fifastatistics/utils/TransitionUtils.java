package com.example.kevin.fifastatistics.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;

import com.example.kevin.fifastatistics.interfaces.ImageCallback;
import com.example.kevin.fifastatistics.interfaces.TransitionStarter;
import com.nostra13.universalimageloader.core.assist.FailReason;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TransitionUtils {

    /**
     * Return transition options for the given view and its associated transition name.
     */
    @NonNull
    public static ActivityOptionsCompat getSceneTransitionOptions(Context context, View transitionView,
                                                                  @StringRes int transitionId) {
        AppCompatActivity activity = ContextUtils.getActivityFromContext(context);
        if (activity != null && transitionId > 0 && transitionView != null) {
            String transition = activity.getString(transitionId);
            return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, transitionView, transition);
        } else {
            return ActivityOptionsCompat.makeBasic();
        }
    }

    /**
     * Return transition options for the pairs of views and their associated transition names.
     */
    @SafeVarargs
    @NonNull
    public static ActivityOptionsCompat getSceneTransitionOptions(Context context, Pair<View, String>... sharedElements) {
        AppCompatActivity activity = ContextUtils.getActivityFromContext(context);
        if (activity != null && sharedElements != null) {
            return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedElements);
        } else {
            return ActivityOptionsCompat.makeBasic();
        }
    }

    /**
     * Return a predraw listener for a given view that starts the activity transition once the view is ready.
     */
    @Nullable
    public static ViewTreeObserver.OnPreDrawListener getTransitionPreDrawListenerForView(final View view, final TransitionStarter transitionStarter) {
        if (transitionStarter != null && view != null) {
            return new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    transitionStarter.startTransition();
                    return true;
                }
            };
        } else {
            return null;
        }
    }

    /**
     * Return a callback that will wait for the image loading to be completed before starting the
     * activity transition.
     */
    @NonNull
    public static ImageCallback getTransitionStartingImageCallback(final TransitionStarter transitionStarter) {
        return new ImageCallback() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                addPreDrawListenerToProfileImage(view);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                addPreDrawListenerToProfileImage(view);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                addPreDrawListenerToProfileImage(view);
            }

            private void addPreDrawListenerToProfileImage(View view) {
                view.getViewTreeObserver().addOnPreDrawListener(
                        getTransitionPreDrawListenerForView(view, transitionStarter));
            }
        };
    }

    /**
     * Return a callback that will wait for all required images to be loaded before starting the
     * activity transition.
     */
    @NonNull
    public static ImageCallback getCountedTransitionStartingImageCallback(final TransitionStarter transitionStarter, final int requiredCallbacks) {
        return new ImageCallback() {

            final AtomicInteger mReceivedCallbacks = new AtomicInteger(0);
            final int mRequiredCallbacksBeforeTransitioning = requiredCallbacks;

            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                addPreDrawListenerToProfileImageIfCallbacksReceived(view);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                addPreDrawListenerToProfileImageIfCallbacksReceived(view);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                addPreDrawListenerToProfileImageIfCallbacksReceived(view);
            }

            private void addPreDrawListenerToProfileImageIfCallbacksReceived(View view) {
                int callbackCount = mReceivedCallbacks.incrementAndGet();
                if (callbackCount >= mRequiredCallbacksBeforeTransitioning) {
                    view.getViewTreeObserver().addOnPreDrawListener(
                            getTransitionPreDrawListenerForView(view, transitionStarter));
                }
            }
        };
    }

    public static Pair<View, String>[] createSafeTransitionParticipants(@NonNull Activity activity,
                                                                        boolean includeStatusBar, @Nullable Pair... otherParticipants) {
        View decor = activity.getWindow().getDecorView();
        View statusBar = null;
        if (includeStatusBar) {
            statusBar = decor.findViewById(android.R.id.statusBarBackground);
        }
        View navBar = decor.findViewById(android.R.id.navigationBarBackground);

        // Create pair of transition participants.
        List<Pair> participants = new ArrayList<>(3);
        addNonNullViewToTransitionParticipants(statusBar, participants);
        addNonNullViewToTransitionParticipants(navBar, participants);
        // only add transition participants if there's at least one none-null element
        if (otherParticipants != null && !(otherParticipants.length == 1
                && otherParticipants[0] == null)) {
            participants.addAll(Arrays.asList(otherParticipants));
        }
        return participants.toArray(new Pair[participants.size()]);
    }

    private static void addNonNullViewToTransitionParticipants(View view, List<Pair> participants) {
        if (view == null) {
            return;
        }
        participants.add(new Pair<>(view, view.getTransitionName()));
    }
}