package com.example.kevin.fifastatistics.fragments.initializers;

import android.support.design.widget.TabLayout;
import android.view.View;

import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.fragments.UserOverviewFragment;
import com.example.kevin.fifastatistics.models.Constants;
import com.example.kevin.fifastatistics.views.adapters.ViewPagerAdapter;

/**
 * Implementation of the abstract FragmentInitializer class for OverviewFragments.
 * <p>
 * See {@link FragmentInitializer} and {@link UserOverviewFragment} for more detailed information on
 * those classes.
 */
public class OverviewFragmentInitializer extends FragmentInitializer {

    @Override
    public void setActivityTitle(FifaActivity activity) {
        activity.setTitle(Constants.OVERVIEW_FRAGMENT);
    }

    @Override
    public void changeAdapterDataSet(ViewPagerAdapter adapter) {
        adapter.clear();
        adapter.addFragment(new UserOverviewFragment(), Constants.OVERVIEW_FRAGMENT);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setTabLayoutVisibility(TabLayout tabLayout) {
        tabLayout.setVisibility(View.GONE);
    }
}
