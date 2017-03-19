package com.example.kevin.fifastatistics.activities;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.HashSet;
import java.util.Set;

public class FifaActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private static final Set<Integer> DESTROYED_ACTIVITIES = new HashSet<>();

    private int status;
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity != null) {
            DESTROYED_ACTIVITIES.add(activity.hashCode());
        }
    }

    static boolean isActivityDestroyed(int activityHashCode) {
        return DESTROYED_ACTIVITIES.remove(activityHashCode);
    }
}
