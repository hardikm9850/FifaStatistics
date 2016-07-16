package com.example.kevin.fifastatistics.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.views.adapters.chartviewpagers.BarChartViewPagerAdapter;
import com.example.kevin.fifastatistics.views.cards.RecordsCardView;

import java.util.ArrayList;
import java.util.List;

public class UserOverview extends LinearLayout {

    private final RecordsCardView mRecordsCardView;
    private final ViewPager mStatsViewPager;
    private final Context mContext;

    public UserOverview(Context c, AttributeSet attributeSet) {
        super(c, attributeSet);

        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.user_overview_data_layout, this);

        mRecordsCardView = (RecordsCardView) findViewById(R.id.recordscardview);
        mStatsViewPager = (ViewPager) findViewById(R.id.card_view_pager);
        mContext = c;
    }

    public void setUser(User user) {
        mRecordsCardView.setMatchRecords(user.getMatchRecords());
        mRecordsCardView.setSeriesRecords(user.getSeriesRecords());

        List<User.StatsPair> stats = new ArrayList<>();
        stats.add(user.getAverageStats());
        stats.add(user.getRecordStats());
        mStatsViewPager.setAdapter(new BarChartViewPagerAdapter(mContext, stats));
        mStatsViewPager.setCurrentItem(0);
    }
}
