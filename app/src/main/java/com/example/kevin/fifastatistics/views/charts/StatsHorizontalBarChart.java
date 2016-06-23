package com.example.kevin.fifastatistics.views.charts;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;

import com.db.chart.Tools;
import com.db.chart.view.HorizontalBarChartView;
import com.db.chart.view.XController;
import com.db.chart.view.YController;
import com.example.kevin.fifastatistics.models.databasemodels.user.Stats;

/**
 * Created by kevin on 2016-06-02.
 */
public class StatsHorizontalBarChart extends HorizontalBarChartView {

    public StatsHorizontalBarChart(Context context, Stats stats) {
        super(context);
        initializeChart(stats);
    }

    private void initializeChart(Stats stats) {

        setBarSpacing(Tools.fromDpToPx(40));
        setRoundCorners(Tools.fromDpToPx(2));
        setBarBackgroundColor(Color.parseColor("#592932"));

        // Chart
        setXAxis(false)
                .setYAxis(false)
                .setXLabels(XController.LabelPosition.OUTSIDE)
                .setYLabels(YController.LabelPosition.NONE)
                .setLabelsColor(Color.parseColor("#86705c"))
                .setAxisColor(Color.parseColor("#86705c"));

        int[] order = {1, 0, 2, 3};
        final Runnable auxAction = new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                    }
                }, 500);
            }
        };;
        Runnable chartOneAction = new Runnable() {
            @Override
            public void run() {
                auxAction.run();
            }
        };
//        mChart.show(new Animation()
//                .setOverlap(.7f, order)
//                .setEndAction(chartOneAction));
    }
}
