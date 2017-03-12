package com.example.kevin.fifastatistics.fragments.initializers;

import android.support.design.widget.TabLayout;
import android.view.View;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.activities.FifaBaseActivity;
import com.example.kevin.fifastatistics.adapters.FragmentAdapter;
import com.example.kevin.fifastatistics.fragments.EventStreamFragment;
import com.example.kevin.fifastatistics.fragments.MatchStreamFragment;
import com.example.kevin.fifastatistics.models.databasemodels.user.Player;
import com.example.kevin.fifastatistics.utils.ResourceUtils;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class MatchesFragmentInitializer implements FragmentInitializer {

    private Player mUser;

    public MatchesFragmentInitializer(Player user) {
        mUser = user;
    }

    @Override
    public void setActivityTitle(FifaBaseActivity activity) {
        activity.setTitle(R.string.matches);
    }

    @Override
    public void changeAdapterDataSet(FragmentAdapter adapter) {
        adapter.clear();
        adapter.addFragment(
                EventStreamFragment.newInstance(MatchStreamFragment.class, mUser, R.string.matches_load_failed),
                ResourceUtils.getStringFromResourceId(R.string.matches));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setTabLayoutVisibility(TabLayout tabLayout) {
        tabLayout.setVisibility(View.GONE);
    }

    @Override
    public void setFabVisibility(FloatingActionsMenu menu) {
        menu.setVisibility(View.GONE);
    }
}
