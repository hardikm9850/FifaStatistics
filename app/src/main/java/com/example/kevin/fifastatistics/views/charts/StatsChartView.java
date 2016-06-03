package com.example.kevin.fifastatistics.views.charts;

import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.model.BarSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.HorizontalStackBarChartView;
import com.db.chart.view.Tooltip;
import com.db.chart.view.animation.Animation;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.user.Stats;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Compound view for presenting Statistics and Match Facts. It consists of a TextView for the
 * title, and a {@link HorizontalStackBarChartView} for the chart.
 */
public class StatsChartView extends LinearLayout {

    private Context mContext;
    private TextView tooltipTextView;
    private float[] valuesAgainst;
    private final TextView chartTitle;
    private final HorizontalStackBarChartView chart;

    public StatsChartView(Context c, AttributeSet attributeSet) {
        super(c, attributeSet);
        mContext = c;

        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.stats_chart_view_layout, this);
        chartTitle = (TextView) findViewById(R.id.stats_chart_title);

        chart = (HorizontalStackBarChartView) findViewById(R.id.stats_chart);
        chart.setBarSpacing(130);
        chart.setRoundCorners(5);

        chart.setXAxis(false);
        chart.setStep(11);
        chart.setAxisBorderValues(0, 120);
        chart.setYAxis(false);
        chart.setYLabels(AxisController.LabelPosition.OUTSIDE);
        chart.setXLabels(AxisController.LabelPosition.NONE);
        chart.setLabelsColor(Color.WHITE);
        chart.setFontSize(50);
    }

    public void setTitle(String title) {
        chartTitle.setText(title);
    }

    public void setChartData(Stats stats) {
        getStats(stats);
        setAnimation();
    }

    private void getStats(Stats stats) {
        float[] valuesFor =
                {
                        stats.getGoalsFor(), stats.getShotsFor(), stats.getShotsOnTarget(),
                        stats.getPossession(), stats.getTacklesFor(), stats.getFouls(),
                        stats.getRedCards(), stats.getOffsides(), stats.getShotAccuracy(),
                        stats.getPassAccuracy()
                };
        float[] valuesAgainst =
                {
                        stats.getGoalsAgainst(), stats.getShotsAgainst(), stats.getShotsAgainst() - 1,
                        100 - stats.getPossession(), stats.getTacklesAgainst(), stats.getFouls(),
                        stats.getRedCards() + 1, stats.getOffsides() + 1, stats.getShotAccuracy(),
                        stats.getPassAccuracy()
                };
        this.valuesAgainst = valuesAgainst.clone();

        String[] labels = new String[valuesFor.length];
        for (int i = 0; i < valuesFor.length; i++) {
            labels[i] = String.format("%.0f", valuesFor[i]);
        }

        for (int i = 0; i < 10; i++) {
            valuesFor[i] = (valuesFor[i] / (valuesFor[i] + valuesAgainst[i])) * 100;
            valuesAgainst[i] = 100 - valuesFor[i];
        }

        BarSet bsFor = new BarSet(labels, valuesFor);
        bsFor.setColor(getResources().getColor(R.color.colorAccent));

        BarSet bsAgainst = new BarSet(labels, valuesAgainst);
        bsAgainst.setColor(Color.GRAY);

        chart.addData(bsFor);
        chart.addData(bsAgainst);
    }

    private void setAnimation() {
        int[] order = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        final Runnable auxAction = () -> new Handler().postDelayed(() -> {}, 500);
        Runnable chartOneAction = () -> {
            auxAction.run();
            tooltipsOn();
        };
        chart.show(new Animation()
                .setOverlap(.9f, order)
                .setEndAction(chartOneAction));
    }

    private void tooltipsOn(){

        for (int i = 0; i < chart.getEntriesArea(1).size(); i++) {
            Tooltip tooltip = new Tooltip(mContext, R.layout.stats_tooltip, R.id.value);
            tooltip.setDimensions(150, 80);
            tooltip.setHorizontalAlignment(Tooltip.Alignment.LEFT_RIGHT);
            tooltip.prepare(chart.getEntriesArea(1).get(i), valuesAgainst[i]);
            chart.showTooltip(tooltip, true);

//            Tooltip title = new Tooltip(mContext, R.layout.stats_tooltip);
//            title.setDimensions(400, 80);
//            title.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP);
//            title.setHorizontalAlignment(Tooltip.Alignment.CENTER);
//            TextView tv = (TextView) title.findViewById(R.id.value);
//            tv.setGravity(Gravity.CENTER);
//            tv.setText("Title");
//            title.prepare(chart.getEntriesArea(0).get(i), 0);
//            chart.showTooltip(title, true);
        }

    }

}
