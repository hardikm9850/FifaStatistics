package com.example.kevin.fifastatistics.views.charts;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.user.Stats;
import com.example.kevin.fifastatistics.models.user.User;
import com.example.kevin.fifastatistics.views.adapters.StatsRecyclerViewAdapter;


/**
 * Compound view for presenting Statistics and Match Facts
 */
public class StatsChartView extends LinearLayout {

    private final Context mContext;
    private final TextView title;
    private final RecyclerView statsList;

    private float[] valuesAgainst;

    public StatsChartView(Context c, AttributeSet attributeSet) {
        super(c, attributeSet);
        mContext = c;

        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.stats_chart_view_layout, this);

        title = (TextView) findViewById(R.id.stats_chart_title);
        statsList = (RecyclerView) findViewById(R.id.stats_recycler_view);
        statsList.setLayoutManager(new LinearLayoutManager(mContext));
        statsList.setHasFixedSize(true);
        statsList.setNestedScrollingEnabled(false);
    }

    public void setTitle(String text) {
        title.setText(text);
    }

    public void setChartData(User.StatsPair stats) {
        statsList.setAdapter(new StatsRecyclerViewAdapter(stats));
    }

}
