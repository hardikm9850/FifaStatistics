package com.example.kevin.fifastatistics.views.adapters.chartviewpagers;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.model.BarSet;
import com.db.chart.view.BarChartView;
import com.db.chart.view.ChartView;
import com.db.chart.view.XController;
import com.db.chart.view.YController;
import com.db.chart.view.animation.Animation;
import com.example.kevin.fifastatistics.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Implementation of ChartViewPagerAdapter for BarCharts.
 */
public class BarChartViewPagerAdapter extends ChartViewPagerAdapter {

    public BarChartViewPagerAdapter(Context context, ArrayList<BarChartView> charts) {
        super(context, charts);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout =
                (ViewGroup) inflater.inflate(R.layout.card_bar_chart_view, collection, false);
        TextView title = (TextView) layout.findViewById(R.id.bar_chart_text_view);
        title.setText("Chart");
        BarChartView chart = __initChart((BarChartView) layout.findViewById(R.id.stats_chart_view));
        collection.addView(layout);
        return layout;
    }

    private BarChartView __initChart(BarChartView mChart) {

        final String[] mLabels= {"A", "B", "C", "D"};
        final float [][] mValues = {{6.5f, 8.5f, 2.5f, 10f}, {7.5f, 3.5f, 5.5f, 4f}};

        BarSet barSet = new BarSet(mLabels, mValues[0]);
        barSet.setColor(Color.parseColor("#fc2a53"));
        mChart.addData(barSet);

        mChart.setBarSpacing(Tools.fromDpToPx(40));
        mChart.setRoundCorners(Tools.fromDpToPx(2));
        mChart.setBarBackgroundColor(Color.parseColor("#592932"));

        // Chart
        mChart.setXAxis(false)
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
        mChart.show(new Animation()
                .setOverlap(.7f, order)
                .setEndAction(chartOneAction));

        return mChart;
    }
}
