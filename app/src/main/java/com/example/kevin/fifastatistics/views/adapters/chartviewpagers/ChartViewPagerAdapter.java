package com.example.kevin.fifastatistics.views.adapters.chartviewpagers;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.models.databasemodels.user.User;

import java.util.List;


/**
 * Abstract viewpager for ChartViews.
 */
public abstract class ChartViewPagerAdapter extends PagerAdapter {

    protected Context mContext;
    protected List<User.StatsPair> stats;

    public ChartViewPagerAdapter(Context context, List<User.StatsPair> stats) {
        mContext = context;
        this.stats = stats;
    }

    @Override
    public int getCount() {
        return stats.size();
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

