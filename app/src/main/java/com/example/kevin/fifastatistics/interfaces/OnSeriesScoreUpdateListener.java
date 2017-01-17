package com.example.kevin.fifastatistics.interfaces;

public interface OnSeriesScoreUpdateListener {

    void onUserScoreUpdate(int oldScore, int newScore);
    void onOpponentScoreUpdate(int oldScore, int newScore);
}
