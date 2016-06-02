package com.example.kevin.fifastatistics.views.adapters.chartviewpagers;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.view.BarChartView;
import com.db.chart.view.ChartView;

import java.util.ArrayList;


/**
 * Abstract viewpager for ChartViews.
 */
public abstract class ChartViewPagerAdapter extends PagerAdapter {

    protected Context mContext;
    protected ArrayList<BarChartView> charts;

    public ChartViewPagerAdapter(Context context, ArrayList<BarChartView> charts) {
        mContext = context;
        this.charts = charts;
    }

    @Override
    public int getCount() {
        return charts.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public abstract Object instantiateItem(ViewGroup container, int position);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

