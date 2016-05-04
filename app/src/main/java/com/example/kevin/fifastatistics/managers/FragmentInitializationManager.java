package com.example.kevin.fifastatistics.managers;

import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.fragments.initializers.FragmentInitializer;
import com.example.kevin.fifastatistics.fragments.initializers.FragmentInitializerFactory;

/**
 * Manager for setting the current fragment.
 */
public class FragmentInitializationManager
{
    private static FragmentInitializer initializer;

    /**
     * Begin initialization process for whatever fragment is specified by the
     * activity's Extras. If no "fragment" extra is found on the activity, then
     * the Overview fragment is initialized.
     */
    public static void initializeAppropriateFragment(FifaActivity activity) {
        initializer = FragmentInitializerFactory.createFragmentInitializer(activity);
        initializer.beginInitialization();
    }

    public static void initializeOverviewFragment(FifaActivity activity) {
        initializer = FragmentInitializerFactory.createOverviewFragmentInitializer(activity);
        initializer.beginInitialization();
    }

    public static void initializeFriendsFragment(FifaActivity activity) {
        initializer = FragmentInitializerFactory.createFriendsFragmentInitializer(activity);
        initializer.beginInitialization();
    }
}
