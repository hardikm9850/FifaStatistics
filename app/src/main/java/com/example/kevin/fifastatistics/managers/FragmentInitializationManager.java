package com.example.kevin.fifastatistics.managers;

import android.view.View;

import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.fragments.FifaFragment;
import com.example.kevin.fifastatistics.fragments.OverviewFragment;
import com.example.kevin.fifastatistics.fragments.FriendsFragment;
import com.example.kevin.fifastatistics.models.Constants;
import com.example.kevin.fifastatistics.views.adapters.ViewPagerAdapter;

/**
 * Manager for setting the current fragment.
 */
public class FragmentInitializationManager
{
    private static final String PAGE_EXTRA = "page";
    private static final String FRAGMENT_EXTRA = "fragment";

    /**
     * Initializes the appropriate fragment, based on the fragment extra passed
     * to the activity. If the extra is null, then the Overview fragment is
     * initialized.
     */
    public static void initializeAppropriateFragment(FifaActivity activity)
    {
        String fragment = getFragmentExtra(activity);
        switch (fragment) {
            case (Constants.OVERVIEW_FRAGMENT):
                initializeOverviewFragment(activity);
                break;
            case (Constants.FRIENDS_FRAGMENT):
                activity.getDrawer().closeDrawer();
                initializeFriendsFragment(activity);
                break;
            default:
                throw new IllegalStateException(fragment + " is not a valid" +
                        " fragment name!");
        }
    }

    private static String getFragmentExtra(FifaActivity activity)
    {
        String fragment = activity.getIntent().getStringExtra(FRAGMENT_EXTRA);
        activity.getIntent().removeExtra(FRAGMENT_EXTRA);
        if (fragment == null) {
            fragment = Constants.OVERVIEW_FRAGMENT;
        }

        return fragment;
    }

    public static void initializeOverviewFragment(FifaActivity activity)
    {
        activity.getToolbar().setTitle(Constants.OVERVIEW_FRAGMENT);
        setOverviewFragmentTabState(activity);
    }

    private static void setOverviewFragmentTabState(FifaActivity activity)
    {
        OverviewFragment overviewFragment = new OverviewFragment();
        activity.getViewPagerAdapter().clear();
        activity.getViewPagerAdapter().addFragment(overviewFragment,
                Constants.OVERVIEW_FRAGMENT);
        activity.getViewPagerAdapter().notifyDataSetChanged();
        activity.getTabLayout().setVisibility(View.GONE);

        activity.setCurrentFragment(overviewFragment);
    }

    public static void initializeFriendsFragment(FifaActivity activity)
    {
        activity.getToolbar().setTitle(Constants.FRIENDS_FRAGMENT);
        addFriendsFragmentsToAdapter(activity);
        setFriendsFragmentTabState(activity);
    }

    private static void addFriendsFragmentsToAdapter(FifaActivity activity)
    {
        ViewPagerAdapter adapter = activity.getViewPagerAdapter();
        adapter.clear();
        adapter.addFragment(
                FriendsFragment.newInstance(FriendsFragment.friendsView),
                Constants.FRIENDS_FRAGMENT);
        adapter.addFragment(
                FriendsFragment.newInstance(FriendsFragment.requestsView),
                "Requests");
        adapter.notifyDataSetChanged();
    }

    private static void setFriendsFragmentTabState(FifaActivity activity)
    {
        int currentPage = activity.getIntent().getIntExtra(PAGE_EXTRA, 0);
        activity.getViewPager().setCurrentItem(currentPage);

        activity.getIntent().removeExtra(PAGE_EXTRA);
        activity.getTabLayout().setVisibility(View.VISIBLE);

        activity.setCurrentFragment(
                (FifaFragment) activity.getViewPagerAdapter().getItem(currentPage));
    }
}
