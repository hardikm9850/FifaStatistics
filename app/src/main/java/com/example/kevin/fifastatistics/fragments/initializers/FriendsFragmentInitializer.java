package com.example.kevin.fifastatistics.fragments.initializers;

import android.view.View;

import com.example.kevin.fifastatistics.activities.FifaActivity;
import com.example.kevin.fifastatistics.fragments.FifaFragment;
import com.example.kevin.fifastatistics.fragments.FriendsFragment;
import com.example.kevin.fifastatistics.models.Constants;
import com.example.kevin.fifastatistics.views.adapters.ViewPagerAdapter;

/**
 * Created by Kevin on 5/3/2016.
 */
public class FriendsFragmentInitializer extends FragmentInitializer
{
    FifaActivity activity;
    int currentPage;

    public FriendsFragmentInitializer(FifaActivity activity) {
        this.activity = activity;
        currentPage = activity.getIntent().getIntExtra(PAGE_EXTRA, 0);
    }

    @Override
    public void setToolbarTitle() {
        activity.setTitle(Constants.FRIENDS_FRAGMENT);
    }

    @Override
    public void initializeViewPagerFragments() {
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

    @Override
    public void initializeTabLayout() {
        activity.getViewPager().setCurrentItem(currentPage);
        activity.getIntent().removeExtra(PAGE_EXTRA);

        activity.getTabLayout().setVisibility(View.VISIBLE);

        activity.setCurrentFragment(
                (FifaFragment) activity.getViewPagerAdapter().getItem
                        (currentPage));
    }

    @Override
    public void setCurrentFragmentForActivity() {
        activity.setCurrentFragment(
                (FifaFragment) activity.getViewPagerAdapter().getItem(currentPage));
    }
}
