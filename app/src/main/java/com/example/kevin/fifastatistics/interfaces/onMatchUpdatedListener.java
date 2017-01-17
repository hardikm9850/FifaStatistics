package com.example.kevin.fifastatistics.interfaces;

import com.example.kevin.fifastatistics.models.databasemodels.match.Match;

public interface OnMatchUpdatedListener {

    void onMatchUpdated(Match oldMatch, Match newMatch);
}
