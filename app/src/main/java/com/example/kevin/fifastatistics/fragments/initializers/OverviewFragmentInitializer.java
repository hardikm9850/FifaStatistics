package com.example.kevin.fifastatistics.fragments.initializers;

import android.support.design.widget.TabLayout;
import android.view.View;

import com.example.kevin.fifastatistics.activities.FifaBaseActivity;
import com.example.kevin.fifastatistics.fragments.UserOverviewFragment;
import com.example.kevin.fifastatistics.models.Constants;
import com.example.kevin.fifastatistics.views.adapters.ViewPagerAdapter;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

/**
 * Implementation of the abstract FragmentInitializer class for OverviewFragments.
 * <p>
 * See {@link FragmentInitializer} and {@link UserOverviewFragment} for more detailed information on
 * those classes.
 */
public class OverviewFragmentInitializer extends FragmentInitializer {

    @Override
    public void setActivityTitle(FifaBaseActivity activity) {
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

    @Override
    public void setFabVisibility(FloatingActionsMenu menu) {
        menu.setVisibility(View.VISIBLE);
    }
}
