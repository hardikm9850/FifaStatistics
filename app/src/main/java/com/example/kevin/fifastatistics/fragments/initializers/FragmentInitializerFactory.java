package com.example.kevin.fifastatistics.fragments.initializers;

import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.models.Constants;

/**
 * Created by Kevin on 5/3/2016.
 */
public class FragmentInitializerFactory
{
    private static final String FRAGMENT_EXTRA = "fragment";

    /**
     * Initializes the appropriate fragment, based on the fragment extra passed
     * to the activity. If the extra is null, then the Overview fragment is
     * initialized.
     */
    public static FragmentInitializer createFragmentInitializer(FifaActivity activity)
    {
        String fragment = getFragmentExtra(activity);
        switch (fragment) {
            case (Constants.OVERVIEW_FRAGMENT):
                return new OverviewFragmentInitializer(activity);
            case (Constants.FRIENDS_FRAGMENT):
                activity.getDrawer().closeDrawer();
                return new FriendsFragmentInitializer(activity);
            default:
                throw new IllegalStateException(fragment + " is not a valid" +
                        " fragment name!");
        }
    }

    private static String getFragmentExtra(FifaActivity activity)
    {
        String extra = activity.getIntent().getStringExtra(FRAGMENT_EXTRA);
        activity.getIntent().removeExtra(FRAGMENT_EXTRA);
        if (extra == null) {
            extra = Constants.OVERVIEW_FRAGMENT;
        }

        return extra;
    }

    public static FragmentInitializer createFriendsFragmentInitializer(
            FifaActivity activity) {
        return new FriendsFragmentInitializer(activity);
    }

    public static FragmentInitializer createOverviewFragmentInitializer(
            FifaActivity activity) {
        return new OverviewFragmentInitializer(activity);
    }
}
