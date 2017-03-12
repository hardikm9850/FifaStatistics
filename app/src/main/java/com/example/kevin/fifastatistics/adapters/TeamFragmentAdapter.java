package com.example.kevin.fifastatistics.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.kevin.fifastatistics.fragments.initializers.TeamListFragment;
import com.example.kevin.fifastatistics.models.apiresponses.ApiListResponse;
import com.example.kevin.fifastatistics.models.databasemodels.league.League;

import java.util.ArrayList;
import java.util.List;

public class TeamFragmentAdapter extends FragmentStatePagerAdapter {

    private final List<League> mLeagues;
    private final List<Fragment> mFragments;

    public TeamFragmentAdapter(FragmentManager manager, ApiListResponse<League> leagueData) {
        super(manager);
        mLeagues = initLeagues(leagueData);
        mFragments = initFragments();
    }

    private List<League> initLeagues(ApiListResponse<League> leagueData) {
        return leagueData != null ? leagueData.getItems() : null;
    }

    private List<Fragment> initFragments() {
        if (mLeagues != null) {
            List<Fragment> fragments = new ArrayList<>(getCount());
            for (League league : mLeagues) {
                fragments.add(TeamListFragment.newInstance(league));
            }
            return fragments;
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return mLeagues != null ? mLeagues.size() : 0;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments != null ? mFragments.get(position) : null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mLeagues != null) {
            League league = mLeagues.get(position);
            return league != null ? league.getName() : null;
        } else {
            return null;
        }
    }
}
