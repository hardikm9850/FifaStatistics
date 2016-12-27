package com.example.kevin.fifastatistics.fragments.initializers;

import android.support.design.widget.TabLayout;
import android.view.View;

import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.fragments.FriendsFragment;
import com.example.kevin.fifastatistics.models.Constants;
import com.example.kevin.fifastatistics.views.adapters.ViewPagerAdapter;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

/**
 * Implementation of the abstract FragmentInitializer class for FriendFragments.
 * <p>
 * See {@link FragmentInitializer} and {@link FriendsFragment} for more detailed information on
 * those classes.
 */
public class FriendsFragmentInitializer extends FragmentInitializer
{
    @Override
    public void setActivityTitle(FifaActivity activity) {
        activity.setTitle("Players");
    }

    @Override
    public void changeAdapterDataSet(ViewPagerAdapter adapter) {
        adapter.clear();
        adapter.addFragment(
                FriendsFragment.newInstance(FriendsFragment.FRIENDS_VIEW),
                Constants.FRIENDS_FRAGMENT);
        adapter.addFragment(
                FriendsFragment.newInstance(FriendsFragment.REQUESTS_VIEW),
                "Friend Requests");

        adapter.notifyDataSetChanged();
    }

    @Override
    public void setTabLayoutVisibility(TabLayout tabLayout) {
        tabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void setFabVisibility(FloatingActionsMenu menu) {
        menu.setVisibility(View.GONE);
    }
}
