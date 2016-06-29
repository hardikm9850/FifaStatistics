package com.example.kevin.fifastatistics.views.adapters.chartviewpagers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.views.cards.StatsCardView;

import java.util.ArrayList;

/**
 * Implementation of ChartViewPagerAdapter for BarCharts.
 */
public class BarChartViewPagerAdapter extends ChartViewPagerAdapter {

    public BarChartViewPagerAdapter(Context context, ArrayList<User.StatsPair> stats) {
        super(context, stats);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        StatsCardView chartView =
                (StatsCardView) inflater.inflate(R.layout.stats_chart_view, collection, false);
        chartView.setTitle((position == 0) ? "Averages" : "Records");
        chartView.setChartData(stats.get(position));
        collection.addView(chartView);
        return chartView;
    }

    private void setChartAnimation(StatsCardView chartView) {


//        int[] order = {1, 0, 2, 3};
//        final Runnable auxAction = () -> new Handler().postDelayed(() -> {}, 500);
//        Runnable chartOneAction = () -> auxAction.run();
//        mChart.show(new Animation()
//                .setOverlap(.7f, order)
//                .setEndAction(chartOneAction));
//
//        return mChart;
    }
}