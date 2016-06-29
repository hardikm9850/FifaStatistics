package com.example.kevin.fifastatistics.views.cards;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.models.databasemodels.user.records.UserRecords;

public class RecordsCardView extends LinearLayout {

    private final RelativeLayout matchRecords;
    private final RelativeLayout seriesRecords;

    public RecordsCardView(Context c, AttributeSet attributeSet) {
        super(c, attributeSet);

        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.records_card_view_layout, this);

        matchRecords = (RelativeLayout) findViewById(R.id.match_records);
        seriesRecords = (RelativeLayout) findViewById(R.id.series_records);
    }

    public void setSeriesRecords(UserRecords records) {
        setSpecificRecord(records, seriesRecords, "Series");
    }

    public void setMatchRecords(UserRecords records) {
        setSpecificRecord(records, matchRecords, "Matches");
    }

    private void setSpecificRecord(UserRecords record, RelativeLayout layout, String title) {
        setHeaderText(layout, title);

        setRecordValueItem(
                (LinearLayout) layout.findViewById(R.id.overall_item),
                record.getOverallRecord().toString(),
                "OVERALL");

        setRecordValueItem(
                (LinearLayout) layout.findViewById(R.id.last_ten_item),
                record.getLastTenRecord().toString(),
                "LAST 10");

        setRecordValueItem(
                (LinearLayout) layout.findViewById(R.id.streak_item),
                record.getStreak().toString(),
                "STREAK");

    }

    private void setHeaderText(RelativeLayout layout, String title) {
        View header = layout.findViewById(R.id.header);
        TextView headerTitle = (TextView) header.findViewById(R.id.header_text);
        headerTitle.setText(title);
    }

    private void setRecordValueItem(LinearLayout layout, String value, String title) {
        TextView valueTextView = (TextView) layout.findViewById(R.id.value);
        valueTextView.setText(value);

        TextView titleTextView = (TextView) layout.findViewById(R.id.title);
        titleTextView.setText(title);
    }


}
