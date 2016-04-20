package com.example.kevin.fifastatistics.managers;

import android.view.View;

import com.example.kevin.fifastatistics.activities.FifaActivity;
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

    public static void initializeMainFragment(FifaActivity activity)
    {
        activity.getToolbar().setTitle(Constants.OVERVIEW_FRAGMENT);
        setMainFragmentTabState(activity);
    }

    private static void setMainFragmentTabState(FifaActivity activity)
    {
        activity.getViewPagerAdapter().clear();
        activity.getViewPagerAdapter().addFragment(new OverviewFragment(),
                Constants.OVERVIEW_FRAGMENT);
        activity.getViewPagerAdapter().notifyDataSetChanged();
        activity.getTabLayout().setVisibility(View.GONE);
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
        activity.getViewPager().setCurrentItem(
                activity.getIntent().getIntExtra(PAGE_EXTRA, 0));

        activity.getIntent().removeExtra(PAGE_EXTRA);
        activity.getTabLayout().setVisibility(View.VISIBLE);
    }

}
