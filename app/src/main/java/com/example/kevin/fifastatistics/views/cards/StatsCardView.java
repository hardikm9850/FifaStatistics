package com.example.kevin.fifastatistics.views.cards;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.databasemodels.user.User;
import com.example.kevin.fifastatistics.adapters.StatsRecyclerViewAdapter;


/**
 * Compound view for presenting Statistics and Match Facts
 */
public class StatsCardView extends LinearLayout {

    private static final String DEFAULT_LEFT_HEADER_TEXT = "You";
    private static final String DEFAULT_RIGHT_HEADER_TEXT = "Opp";

    private final TextView title;
    private final RecyclerView statsList;

    private TextView leftHeader;
    private TextView rightHeader;

    public StatsCardView(Context c, AttributeSet attributeSet) {
        super(c, attributeSet);

        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.stats_list_view_layout, this);

        title = (TextView) findViewById(R.id.stats_chart_title);
        statsList = (RecyclerView) findViewById(R.id.stats_recycler_view);
        statsList.setLayoutManager(new LinearLayoutManager(c));
        statsList.setHasFixedSize(true);
        statsList.setNestedScrollingEnabled(false);

        View header = findViewById(R.id.header);
        leftHeader = (TextView) header.findViewById(R.id.header_left_text);
        rightHeader = (TextView) header.findViewById(R.id.header_right_text);

        leftHeader.setText(DEFAULT_LEFT_HEADER_TEXT);
        rightHeader.setText(DEFAULT_RIGHT_HEADER_TEXT);
    }

    public void setTitle(String text) {
        title.setText(text);
    }

    public void setLeftHeaderText(String text) {
        leftHeader.setText(text);
    }

    public void setRightHeaderText(String text) {
        rightHeader.setText(text);
    }

    public void setChartData(User.StatsPair stats, boolean doShowDecimals) {
        statsList.setAdapter(new StatsRecyclerViewAdapter(stats, doShowDecimals));
    }

}
