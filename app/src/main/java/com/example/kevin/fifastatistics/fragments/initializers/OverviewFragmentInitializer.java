package com.example.kevin.fifastatistics.fragments.initializers;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.fragments.FifaFragment;
import com.example.kevin.fifastatistics.fragments.OverviewFragment;
import com.example.kevin.fifastatistics.models.Constants;
import com.example.kevin.fifastatistics.views.adapters.ViewPagerAdapter;

/**
 * Implementation of the abstract FragmentInitializer class for OverviewFragments.
 * <p>
 * See {@link FragmentInitializer} and {@link OverviewFragment} for more detailed information on
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
        adapter.addFragment(new OverviewFragment(), Constants.OVERVIEW_FRAGMENT);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setTabLayoutVisibility(TabLayout tabLayout) {
        tabLayout.setVisibility(View.GONE);
    }
}
