package com.example.kevin.fifastatistics.fragments.initializers;

import android.view.View;

import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.fragments.FifaFragment;
import com.example.kevin.fifastatistics.fragments.OverviewFragment;
import com.example.kevin.fifastatistics.models.Constants;

/**
 * Implementation of the abstract FragmentInitializer class for OverviewFragments.
 * <p>
 * See {@link FragmentInitializer} and {@link OverviewFragment} for more detailed information on
 * those classes.
 */
public class OverviewFragmentInitializer extends FragmentInitializer {

    FifaActivity activity;

    public OverviewFragmentInitializer(FifaActivity activity) {
        this.activity = activity;
    }

    @Override
    public void setActivityTitle() {
        activity.setTitle(Constants.OVERVIEW_FRAGMENT);
    }

    @Override
    public void initializeViewPagerFragments() {
        activity.getViewPagerAdapter().clear();
        activity.getViewPagerAdapter().addFragment(
                new OverviewFragment(),
                Constants.OVERVIEW_FRAGMENT);

        activity.getViewPagerAdapter().notifyDataSetChanged();
    }

    @Override
    public void prepareTabLayout() {
        activity.getTabLayout().setVisibility(View.GONE);
    }

    @Override
    public void setCurrentFragmentForActivity() {
        activity.setCurrentFragment((FifaFragment) activity.getViewPagerAdapter().getItem(0));
    }
}
