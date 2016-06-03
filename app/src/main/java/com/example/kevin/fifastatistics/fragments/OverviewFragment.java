package com.example.kevin.fifastatistics.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.db.chart.view.BarChartView;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.managers.SharedPreferencesManager;
import com.example.kevin.fifastatistics.models.user.Stats;
import com.example.kevin.fifastatistics.models.user.User;
import com.example.kevin.fifastatistics.views.adapters.chartviewpagers.BarChartViewPagerAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends Fragment implements FifaFragment{

    private User mUser;

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mUser = SharedPreferencesManager.getUser();
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_overview, container, false);

        TextView name = (TextView) view.findViewById(R.id.user_header_name_text);
        name.setText(mUser.getName());

        ImageView profileImage = (ImageView) view.findViewById(R.id.user_header_profile_image);
        ImageLoader.getInstance().displayImage(mUser.getImageUrl(), profileImage);

        ViewPager chartPager = (ViewPager) view.findViewById(R.id.stats_card_view_pager);
        ArrayList<Stats> stats = new ArrayList<>();
        stats.add(mUser.getAverages());
        stats.add(mUser.getRecords());
        chartPager.setAdapter(new BarChartViewPagerAdapter(getContext(), stats));
        chartPager.setCurrentItem(0);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.main, menu);
        menu.findItem(R.id.action_search).setVisible(false);
    }

    @Override
    public boolean handledBackPress() {
        return false;
    }
}
