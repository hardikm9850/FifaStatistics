package com.example.kevin.fifastatistics.interfaces;

import com.example.kevin.fifastatistics.models.databasemodels.match.Series;

public interface OnSeriesCompletedListener {

    void onSeriesCompleted(Series series);
    void onSeriesContinued();
}
