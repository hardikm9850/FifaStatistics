package com.example.kevin.fifastatistics.fragments.initializers;

import android.support.design.widget.TabLayout;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.FifaBaseActivity;
import com.example.kevin.fifastatistics.adapters.FragmentAdapter;
import com.example.kevin.fifastatistics.fragments.EventStreamFragment;
import com.example.kevin.fifastatistics.fragments.SeriesStreamFragment;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.utils.ResourceUtils;

public class SeriesFragmentInitializer implements FragmentInitializer {

    private Player mUser;

    public SeriesFragmentInitializer(Player user) {
        mUser = user;
    }

    @Override
    public void setActivityTitle(FifaBaseActivity activity) {
        activity.setTitle(R.string.series);
    }

    @Override
    public void changeAdapterDataSet(FragmentAdapter adapter) {
        adapter.clear();
        adapter.addFragment(
                EventStreamFragment.newInstance(SeriesStreamFragment.class, mUser, R.string.error_load_series),
                ResourceUtils.getStringFromResourceId(R.string.series));
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
