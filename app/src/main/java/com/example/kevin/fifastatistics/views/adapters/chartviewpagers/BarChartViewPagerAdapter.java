package com.example.kevin.fifastatistics.views.adapters.chartviewpagers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.views.cards.StatsCardView;

import java.util.List;

/**
 * Implementation of ChartViewPagerAdapter for BarCharts.
 */
public class BarChartViewPagerAdapter extends ChartViewPagerAdapter {

    private String mUserName;

    /**
     * @param userName Value to set for the left header text of the StatsCardView. Set as null if
     *                 you want the default value to be used.
     */
    public BarChartViewPagerAdapter(Context context, List<User.StatsPair> stats, String userName) {
        super(context, stats);
        this.mUserName = userName;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        StatsCardView chartView = (StatsCardView) inflater.inflate(R.layout.stats_chart_view, collection, false);

        chartView.setTitle((position == 0) ? "Averages" : "Records");
        chartView.setChartData(stats.get(position), position == 0);

        if (mUserName != null) {
            chartView.setLeftHeaderText(mUserName);
        }

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
