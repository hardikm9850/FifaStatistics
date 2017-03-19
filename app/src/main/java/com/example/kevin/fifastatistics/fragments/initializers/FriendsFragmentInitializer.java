package com.example.kevin.fifastatistics.fragments.initializers;

import android.support.design.widget.TabLayout;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.FifaBaseActivity;
import com.example.kevin.fifastatistics.adapters.FragmentAdapter;
import com.example.kevin.fifastatistics.fragments.FriendsFragment;
import com.example.kevin.fifastatistics.models.Constants;

/**
 * Implementation of the abstract FragmentInitializer class for FriendFragments.
 * <p>
 * See {@link FragmentInitializer} and {@link FriendsFragment} for more detailed information on
 * those classes.
 */
public class FriendsFragmentInitializer implements FragmentInitializer
{
    @Override
    public void setActivityTitle(FifaBaseActivity activity) {
        activity.setTitle(R.string.players);
    }

    @Override
    public void changeAdapterDataSet(FragmentAdapter adapter) {
        adapter.clear();
        adapter.addFragment(
                FriendsFragment.newInstance(),
                Constants.FRIENDS_FRAGMENT);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setTabLayoutVisibility(TabLayout tabLayout) {
        tabLayout.setVisibility(View.GONE);
    }

    @Override
    public void setFabVisibility(View menu) {
        menu.setVisibility(View.GONE);
    }
}
